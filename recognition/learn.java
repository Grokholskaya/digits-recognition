package recognition;
//обучение двухуровневой сети и распознавание
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class learn {
    private int[] x = new int[15];
    public static Weights w = new Weights();
    //входные значения - представления цифр от 0 до 9 в матрице размером 5х3
    static int[][] input = {
            {+1, +1, +1, +1, 0, +1, +1, 0, +1, +1, 0, +1, +1, +1, +1},//0
            //{0, +1, 0, 0, +1, 0, 0, +1, 0, 0, +1, 0, 0, +1, 0},//1
            {0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0},//1
            {+1, +1, +1, 0, 0, +1, +1, +1, +1, +1, 0, 0, +1, +1, +1},//2
            {+1, +1, +1, 0, 0, +1, +1, +1, +1, 0, 0, +1, +1, +1, +1},//3
            {+1, 0, +1, +1, 0, +1, +1, +1, +1, 0, 0, +1, 0, 0, +1},//4
            {+1, +1, +1, +1, 0, 0, +1, +1, +1, 0, 0, +1, +1, +1, +1},//5
            {+1, +1, +1, +1, 0, 0, +1, +1, +1, +1, 0, +1, +1, +1, +1},//6
            {+1, +1, +1, 0, 0, +1, 0, 0, +1, 0, 0, +1, 0, 0, +1},//7
            {+1, +1, +1, +1, 0, +1, +1, +1, +1, +1, 0, +1, +1, +1, +1},//8
            {+1, +1, +1, +1, 0, +1, +1, +1, +1, 0, 0, +1, +1, +1, +1},//9
            //{1,1,0,0,0,1,0,1,1,1,0,0,1,1,1},//2 тест
    };

    //идеальные выходные значения
    static int[][] idealOutput = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},//0
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},//1
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},//2
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},//3
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},//4
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},//5
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},//6
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},//7
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},//8
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},//9
            //{0, 0, 1, 0, 0, 0, 0, 0, 0, 0},//2
    };

    public static void startLearn(){
        int step =0;
        double sum = 0;
        int result=0;
        while (step<=100){
            //для каждой цифры (10) считаем дельты весовых коэффициентов
            if (step > 0) {
                w.resetDelta();
                result = 0;
            }
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < w.outputCnt; j++) {
                    sum = w.getSum(input[i],j);//получаем реальное значение выходного нейрона
                    result += w.setDelta(input[i],j, idealOutput[i][j]- sum);
                }
            }
            w.recalcWeights(input.length);
            if (result==0) {
                System.out.println("step="+step);
                break;
            }
            step++;
        }
        System.out.println("result="+result+"; step="+ step);
    }

    public static int getNum(int[] input) {
        double output[] = new double[w.outputCnt];
        for (int i = 0; i < output.length; i++) {
            //bias
            output[i]=w.data[w.inputCnt][i];
            for (int j = 0; j < input.length; j++) {
                output[i]+=input[j]*w.data[j][i];
            }
            output[i] = Weights.sigmoid(output[i]);
        }
        //System.out.println(Arrays.toString(output));
        System.out.print("[");
        for (int j = 0; j < output.length-1; j++) {
            System.out.printf("%.5f, ",output[j]);
        }
        System.out.printf("%.5f]%n",output[output.length-1]);

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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        while (true) {
            System.out.println("1. Learn the network (from zero)");
            System.out.println("11. Learn the network (from random)");
            System.out.println("2. Guess a number");
            System.out.println("22. Guess a number all");
            System.out.println("3. output neuron net data");
            System.out.println("4. Exit");
            System.out.println("Your choise:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int mode = Integer.parseInt(reader.readLine());
            if (mode == 1 || mode == 11) {
                //обучение
                w.fillDataRnd(mode==11);
                System.out.println("Learning...");
                //System.out.println(w);
                startLearn();
                w.saveToFile();
                System.out.println("Done! Saved to the file.");
            } else if (mode == 3) {
                //отображение сети
                w.loadFromFile();
                System.out.println(w);
            } else if (mode == 2) {
                //распознавание
                w.loadFromFile();
                int[] inp = new int[w.inputCnt];
                System.out.println("Input grid:");
                String stroka;
                try {
                    int o = 0;
                    for (int i = 0; i < 5; i++) {
                        stroka = reader.readLine();
                        for (int j = 0; j < 3; j++) {
                            if (stroka.charAt(j) == 'X' || stroka.charAt(j) == 'x') {
                                inp[o++] = 1;
                            } else inp[o++] = 0;
                        }
                    }
                    System.out.println(Arrays.toString(inp));
                    System.out.printf("The number is %d%n", getNum(inp));
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else if (mode==22){
                //распознавание всех в цикле
                w.loadFromFile();
                for (int i = 0; i < input.length; i++) {
                    //Integer.parseInt(Arrays.toString(idealOutput[i]).replaceAll("[\\[\\]\\,\\s]",""),2))
                    System.out.println("Guess "+ i+"? - "+((getNum(input[i])==i)?"yes":"no"));
                }
            } else if (mode == 4){
                return;
            }
        }
    }
}

