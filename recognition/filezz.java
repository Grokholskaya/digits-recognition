package recognition;

import java.io.File;
import java.io.IOException;

public class filezz {
    public static void main(String[] args) {
        deleteFileOrDir();
        //createDir();
        if (true) return;
        File file = new File("D:\\work\\test\\file.txt");
        try {
            boolean createdNew = file.createNewFile();
            if (createdNew) {
                System.out.println("The file was successfully created.");
            } else {
                System.out.println("The file already exists");
            }
        } catch (IOException e) {
            System.out.println("Cannot create the file: " + file.getPath());
        }
    }
    public static void createDir(){
        File file = new File("D:\\work\\test\\2\\1");

        //boolean createdNewDirectory = file.mkdir();
        boolean createdNewDirectory = file.mkdirs();
        if (createdNewDirectory) {
            System.out.println("It was successfully created.");
        } else {
            System.out.println("It was not created.");
        }
    }
    public static void deleteFileOrDir(){
        File file = new File("D:\\work\\test\\2");
        //file.deleteOnExit();
        //if (true) return;
        if (file.delete()) {
            System.out.println("It was successfully removed.");
        } else {
            System.out.println("It was not removed.");
        }
    }
}
