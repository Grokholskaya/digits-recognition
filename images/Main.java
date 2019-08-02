package images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static levels.Mnist.saveGroupDataList;

public class Main {
    //private static List<Integer> input = new ArrayList<>();
    private int     height = 0;             // высота изображения
    private int     width = 0;              // ширина изображения
    private static int targetOut;
    private static int[]   pixels;
    private BufferedImage bi;
    /*public int getPixel(int x, int y)   {
        return pixels[y*width+x];
    }*/   // получить пиксель в формате RGB

    public void saveToImageFile(String FileName) throws IOException {
        File outputFile = new File(FileName);
        ImageIO.write( bi, "PNG", outputFile);
    }

    public void copyToBufferedImage()  {
        bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int r = 128;
                int g = 128;
                int b = pixels[i*width +j];
                int col = (r << 16) | (g << 8) | b;
                //int col = 255-pixels[i*width +j];
                bi.setRGB(j, i, col );
            }
    }

    public Main(String fileName) throws IOException {

        List<Integer> input = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName))))
        {
            String line;
            while ((line = reader.readLine()) != null) {
                height++;
                for (String s : line.split("\t")) {
                    input.add(Integer.parseInt(s));
                }
                if (width==0) width = input.size();
                System.out.println(line);
            }
            if (input.size()>784) {
                targetOut = input.remove(input.size() - 1);
                height--;
            }
            pixels = input.stream().mapToInt(i -> i).toArray();
        }
    }

    public static void main(String[] args) throws IOException {
        saveGroupDataList();
        if (true) return;
        //String fileName = "D:\\work\\test\\69935.txt";//"D:\\work\\test\\data\\69935.txt";
        String fileName = "D:\\work\\test\\data\\69933.txt";
        Main main = new Main(fileName);
        System.out.println(main.height);
        System.out.println(main.width);
        main.copyToBufferedImage();
//        main.saveToImageFile("D:\\work\\test\\69935.png");
        main.saveToImageFile("D:\\work\\test\\69933.png");
    }
}
