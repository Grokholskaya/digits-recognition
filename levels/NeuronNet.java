package levels;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuronNet implements Serializable {
    public transient static final double NJU = 0.5;//коэффициент обучения, используется при расчете delta w
    protected transient String path = "D:\\work\\test\\file1.txt";
    protected ArrayList<Level> levels;
    protected int[] nCnt;
    protected transient int[][] idealOutput;
    protected transient double[][] learningInput;
    public static transient int learningInputLength;
    protected transient double[] testInput;
    public transient int inputIndex=0;
    protected boolean isReady=false;
    public boolean isDebug = false;

    public boolean isReady() {
        return isReady;
    }
    public void setIdealOutput(int[][] idealOutput) { this.idealOutput = idealOutput; }

    public void setInput(double[][] input) {
        this.learningInput = input;
        learningInputLength = input.length;
    }

    public void setTestInput(double[] testInput) { this.testInput = testInput; inputIndex = -1;}

    public double[] getInput() {
        if (inputIndex == -1) return testInput; else return learningInput[inputIndex];
    }

    public int[] getnCnt() { return nCnt; }
    public ArrayList<Level> getLevels() {
        return levels;
    }
    public Level getLevel(int index){
        return levels.get(index);
    }
    public OutputLevel getOutputLevel(){
        return (OutputLevel) levels.get(levels.size()-1);
    }
    public NeuronNet(){
        //пустой конструктор, используется для загрузки данных из файла
    }
    //массив с количеством нейронов в каждом слое 10, 15, 11
    public NeuronNet(int[] nCnt) {
        this.nCnt = nCnt;
        createLevels(nCnt,true);
    }

    protected void createLevels(int[] nCnt, boolean fillRnd){
        Level level = null;
        levels = new ArrayList<>();
        for (int i = 0; i < nCnt.length; i++) {
            if (i==0) {
                level = new InputLevel(this,nCnt[i]);
            } else if (i==nCnt.length-1){
                level = new OutputLevel(this, i,nCnt[i]);
            } else {
                level = new HiddenLevel(this, i,nCnt[i]);
            }
            levels.add(level);
            if (i>0) level.createLinks(fillRnd);
        }
    }

    public void learn(int iStep){
        int step =0;
        double sum = 0;
        int result=0;
        //learningInputLength = 10;
        while (step<=iStep/*1000*/){
            //System.out.println("step="+step);
            if (step > 0) {
                result = 0;
                //Level.calc = 0;
                this.getOutputLevel().resetTotalError();
                for (Level level : levels) {
                    level.resetDelta();
                }
            }

            for (int i = 0; i < learningInputLength/*learningInput.length*/; i++) {
                inputIndex=i;
                //1. Идем слева направа, считаем ошибку
                for (Level level : this.levels) {
                    level.setInput();
                    level.setOutput();
                    //для выходного уровня устанавливаем вектор идеальных значений для расчета ошибки
                    if (level.getLevelType()== LevelType.OUTPUT)
                        ((OutputLevel) level).setTarget(idealOutput[i]);
                }
                //System.out.println("total error=("+i+")"+this.getOutputLevel().getTotalError());
                this.getOutputLevel().setTotalError();
                Level level;
                //2.идем обратно считаем дельту
                for (int j = levels.size()-1; j > 0; j--) {
                    level = levels.get(j);
                    level.setSigma();
                    level.setDelta();
                    //result += level.setNewWeight();
                }
                //System.out.println(getOutputLevel());
            }
            //for (int j = levels.size()-1; j > 0; j--) {
            for (int i = 1; i < levels.size(); i++) {
                result += levels.get(i).setNewWeight();
            }
            if (result==0) {
                System.out.println("step="+step);
                break;
            }
            if (step==0)
                System.out.println("start total error="+this.getOutputLevel().getTotalError());
            step++;
            //System.out.println("calc="+Level.calc);
        }
        System.out.println("end total error="+this.getOutputLevel().getTotalError());
        System.out.println("result="+result+"; step="+ step);
        isReady = true;
        //System.out.println(printNeurons());
    }

    public String printNeurons(){
        StringBuffer sb = new StringBuffer();
        double[] data = new double[12];
        Neuron n;
        int i1;
        for (int i = 0; i < this.nCnt[0]+1; i++) {
            i1 = 0;
            for (int j = 0; j < levels.size(); j++) {
                try {
                    n = levels.get(j).neurons.get(i);
                } catch (IndexOutOfBoundsException e){
                    n=null;
                }
                if (n==null){
                    data[i1++] = 0;
                    data[i1++] = 0;
                    //if (levels.get(j).getLevelType() == LevelType.OUTPUT)
                        data[i1++]=0;
                } else {
                    data[i1++] = n.getIn();
                    data[i1++] = n.getOut();
                    //if (levels.get(j).getLevelType() == LevelType.OUTPUT)
                        data[i1++] = n.getSigma();
                }
            }
            sb.append(String.format("%2d:%.3f|%.3f(s=%.3f) %.3f|%.3f(s=%.3f) %.3f|%.3f(s=%.3f) %.3f|%.3f(s=%.3f)\n",
                    i,
                    data[0],
                    data[1],
                    data[2],
                    data[3],
                    data[4],
                    data[5],
                    data[6],
                    data[7],
                    data[8],
                    data[9],
                    data[10],
                    data[11]
                    )
            );
        }
        return sb.toString();
    }

    public int recognize(double[] input){
        this.testInput = input;
        this.inputIndex = -1;
        for (Level level : this.levels) {
            level.setInput();
            level.setOutput();
        }
        //System.out.println(printNeurons());

        double output[] = this.levels.get(levels.size()-1).getOutput();
        //System.out.println(Arrays.toString(output));
        /*if (isDebug) {
            System.out.print("[");
            for (int j = 0; j < output.length - 1; j++) {
                System.out.printf("%.5f, ", output[j]);
            }
            System.out.printf("%.5f]%n", output[output.length - 1]);
        }*/
        double max = Double.MIN_VALUE;
        int max_index=-1;
        for (int i = 0; i < output.length; i++) {
            if (Double.compare(output[i], max)>0){
                max = output[i];
                max_index = i;
            }
        }
        return max_index;
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
        NeuronNet net = (NeuronNet) objectInputStream.readObject();
        //нужно взять отсюда только значения весов предварительно проверив, совпадает
        //ли количество уровней
        try {
            if (this.nCnt != null) {
                if (this.levels.size() != net.levels.size())
                    throw new InvalidObjectException("Загрузка из файла невозможна. Количество уровней не совпадает");
                for (int i = 1; i < levels.size(); i++) {
                    if (levels.get(i).getLinks().size() != net.levels.get(i).getLinks().size())
                        throw new InvalidObjectException("Загрузка из файла невозможна. Количество нейронов в уровне " + i + " не совпадает");
                }
            } else {
                this.nCnt = net.nCnt;
                this.createLevels(this.nCnt, false);
            }
            for (int i = 1; i < levels.size(); i++) {
                for (int j = 0; j < levels.get(i).getLinks().size(); j++) {
                    levels.get(i).getLinks().get(j).setWeight(net.levels.get(i).getLinks().get(j).getWeight());
                }
            }
            isReady = true;
        } finally {
            objectInputStream.close();
            fileInputStream.close();
        }
        //this.data = weight.data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(Arrays.toString(this.nCnt)).append(System.lineSeparator());
        for (Level level : this.levels) {
            sb.append(level.toString());
        }
        return sb.toString();
    }
}
