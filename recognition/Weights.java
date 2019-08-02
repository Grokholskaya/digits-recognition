package recognition;

import java.io.*;
import java.util.Random;

public class Weights implements Serializable {
    private transient String path = "D:\\work\\test\\file.txt";
    public transient final double nju = 0.5;//коэффициент скорости обучения
    public transient final int inputCnt = /*3*/15;//количество входных нейронов +1 - вес самого нейрона -bias
    public transient final int outputCnt = 10;//количество выходных нейронов
    public double[][] data = new double[inputCnt+1][outputCnt];
    public transient double[][] delta = new double[inputCnt+1][outputCnt];
    public long serialVersionUID = 1L;

    public void resetDelta(){
        for (int i = 0; i < delta.length; i++) {
            for (int j = 0; j < delta[i].length; j++) {
                delta[i][j]=0;
            }
        }
    }

    public static double sigmoid(double x){
        //логистическая функция
        //Sigmoid(x)=S(x)=1/(1+e−x)
        return 1/(1+Math.exp(-x));
    }
    //считаем взвешеную сумму для заданного outpNom выходного нейрона,
    // нормализованную сигмоидом, массив noms - переданное представление числа
    public double getSum(int[] noms, int outpNom){
        double sum=0;
        for (int i = 0; i < inputCnt/*noms.length*/; i++) {
            sum+=noms[i]*data[i][outpNom];
        }
        //bias
        sum+=data[inputCnt][outpNom];
        return sigmoid(sum);
    }

    public int setDelta(int[] input, int /*inpNom*/outpNom, double error/*double idealOutput, double realOutput*/){
        // outpNom - индекс выходного нейрона
        // допустим рассматриваем двойку outpNom = 2
        //double error = idealOutput-realOutput;
        if (Math.abs(error) < 1e-6) return 0;
        //if (Double.compare(error,0)==0) return 0;
        for (int i = 0; i < inputCnt/*input.length*/; i++) {
                delta[i][outpNom]+=nju*input[i]*error;
        }
        /*for (int i = 0; i < *//*idealOutput.length*//*outputCnt; i++) {
            error = idealOutput[i]-sum;
            if (Double.compare(error,0)==0) continue;
            cnt++;
            for (int j = 0; j < inputCnt*//*input.length*//*; j++) {
                delta[j][i]+=nju*input[j]*error;
            }
            //bias
            delta[inputCnt][i]+=nju*error;
        }*/
        return 1;
    }

    public double getMeanOfDelta(int inpIndex, int outpLength){
        double summa=0;
        //summa = delta[inpIndex]
        /*for (int i = 0; i < delta[inpIndex].length; i++) {
            summa+=delta[inpIndex][i];
        }*/
        return summa/(outpLength==0?outputCnt:outpLength);
    }

    public void recalcWeights(int outpLength){
        double ddelta=0;
        for (int i = 0; i < data.length; i++) {
            //delta = getMeanOfDelta(i);
            for (int j = 0; j < data[i].length; j++) {
                ddelta = delta[i][j]/(outpLength==0?outputCnt:outpLength);
                data[i][j]+=ddelta;
            }
        }
    }

    public void saveToFile() throws IOException {
        File file = new File(path);
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("Cannot create the file: " + file.getPath());
            throw e;
        }
    }
    public void loadFromFile() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Weights weight = (Weights) objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        this.data = weight.data;
    }
    public void fillDataRnd(boolean initFromRnd){
       /* data[0][1] = 0.2;
        data[1][1] = 0.5;
        data[2][1] = 0.7;
        if (true) return;*/
        Random rnd = null;
       if (initFromRnd) rnd = new Random();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = initFromRnd?rnd.nextGaussian():0;
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            sb.append(i+": [");
            for (int j = 0; j < data[i].length-1; j++) {
                sb.append(String.format("%.3f, ",data[i][j]));
            }
            sb.append(String.format("%.3f]",data[i][data[i].length-1]));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
