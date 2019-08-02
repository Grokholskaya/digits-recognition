package levels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static NeuronNet net;
    //значения для обучения
    static double[][] input = {
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

    //идеальные выходные значения target
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //при переносе не забыть убрать цикл
        while (true) {
            System.out.println("1. Learn the network");
            System.out.println("2. Guess a number");
            System.out.println("22. Guess a number all");
            System.out.println("3. Output neuron net data");
            System.out.println("4. Exit");
            System.out.println("Your choise:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int mode = Integer.parseInt(reader.readLine());
            if (mode == 1) {
                System.out.println("Enter the sizes of the layers: ");
                String[] sizeOfLayers = "15 12 12 10".split("\\s");//reader.readLine().split("\\s");
                int[] iSizeOfLayers = new int[sizeOfLayers.length];
                for (int i = 0; i < sizeOfLayers.length; i++) {
                    iSizeOfLayers[i]=Integer.parseInt(sizeOfLayers[i]);
                }
                net = new NeuronNet(iSizeOfLayers);
                net.setInput(input);
                net.setIdealOutput(idealOutput);
                //обучение
                System.out.println("Enter the count of learning steps:");
                net.learn(Integer.parseInt(reader.readLine()));
                System.out.println("Learning...");
                //System.out.println(net);
                net.saveToFile();
                System.out.println("Done! Saved to the file.");
                //return;
            } else if (mode == 3) {
                //отображение сети
                net = new NeuronNet();
                net.loadFromFile();
                System.out.println(net);
            } else if (mode == 2) {
                //распознавание
                net = new NeuronNet();
                net.loadFromFile();
                double[] inp = new double[net.getnCnt()[0]];
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
                    //inp = input[Integer.parseInt(reader.readLine())];
                    System.out.println(Arrays.toString(inp));
                    System.out.printf("The number is %d%n", net.recognize(inp));
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else if (mode==22){
                //распознавание всех в цикле
                net = new NeuronNet();
                net.loadFromFile();
                for (int i = 0; i < input.length; i++) {
                    System.out.println(i);
                    System.out.println("Guess "+ i+"? - "+((net.recognize(input[i])==i)?"yes":"no"));
                }
            } else if (mode == 4){
                return;
            }
        }
    }
}
