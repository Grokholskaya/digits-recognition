package levels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class BigNetMain {
    public static BigNeuronNet net;
    public static Mnist mnist;
    //public static final String dataPathFolder = "D:\\work\\test\\data";
    public static final String dataPathFolder = "D:\\work\\test\\test_samples";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //при переносе не забыть убрать цикл
        mnist = new Mnist(dataPathFolder, false);
        net = new BigNeuronNet(mnist);
        net.isDebug = true;
        while (true) {
            System.out.println("1. Learn the network");
            System.out.println("2. Guess all the numbers");
            System.out.println("3. Guess number from text file");
            System.out.println("4. Output neuron net data");
            System.out.println("5. Exit");
            System.out.println("Your choise:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int mode = Integer.parseInt(reader.readLine());
            if (mode == 1) {
                //System.out.println("Enter the sizes of the layers: ");
                //String[] sizeOfLayers = reader.readLine().split("\\s");
                String[] sizeOfLayers = "784 16 16 10".split("\\s");
                int[] iSizeOfLayers = new int[sizeOfLayers.length];
                for (int i = 0; i < sizeOfLayers.length; i++) {
                    iSizeOfLayers[i]=Integer.parseInt(sizeOfLayers[i]);
                }
                //net = new BigNeuronNet(iSizeOfLayers, 10, mnist);
                System.out.println("totalSamples(600):");
                int total = Integer.parseInt(reader.readLine());
                System.out.println("digitsInBatch(4):");
                net.initData(iSizeOfLayers, Integer.parseInt(reader.readLine()),total);
                System.out.println("Loading MNIST training data...");
                mnist.setFilesList();//при переносе для проверки раскоментировать
                //обучение
                System.out.println("Enter the count of repetitions:");
                long time =  System.currentTimeMillis();
                net.learn(Integer.parseInt(reader.readLine()));
                System.out.println("Learning...");
                net.saveToFile();
                System.out.println("Done! Saved to the file.");
                System.out.println("time="+(System.currentTimeMillis()-time));
                //return;
            } else if (mode == 4) {
                //отображение сети
                if (!net.isReady) net.loadFromFile();
                System.out.println(net);
            } else if (mode == 2) {
                //распознавание всех данных
                //net = new BigNeuronNet(mnist);
                mnist.setFilesList();
                if (!net.isReady) net.loadFromFile();
                net.recognizeAll();
            } else if (mode==3){
                //распознавание числа из файла
                System.out.println("Enter filename: ");
                String fileName = reader.readLine();
                if (!net.isReady) net.loadFromFile();
                System.out.printf("The number is %d\n", net.recognize(fileName));
            } else if (mode == 5){
                return;
            }
        }
    }
}
