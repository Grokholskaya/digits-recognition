package recognition;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class zennolab {
    private static int/*[]*/[] weights = new int/*[10]*/[15];
    private static int bias = 7;
    public static void main(String[] args) {
        int[][] nums = {
                {+1, +1, +1, +1, 0, +1, +1, 0, +1, +1, 0, +1, +1, +1, +1},//0  0
                {0, +1, 0, 0, +1, 0, 0, +1, 0, 0, +1, 0, 0, +1, 0},//1 +6
                {+1, +1, +1, 0, 0, +1, +1, +1, +1, +1, 0, 0, +1, +1, +1},//2 +0
                {+1, +1, +1, 0, 0, +1, +1, +1, +1, 0, 0, +1, +1, +1, +1},//3 +0
                {+1, 0, +1, +1, 0, +1, +1, +1, +1, 0, 0, +1, 0, 0, +1},//4 +2
                {+1, +1, +1, +1, 0, 0, +1, +1, +1, 0, 0, +1, +1, +1, +1},//5 +0
                {+1, +1, +1, +1, 0, 0, +1, +1, +1, +1, 0, +1, +1, +1, +1},//6 0
                {+1, +1, +1, 0, 0, +1, 0, 0, +1, 0, 0, +1, 0, 0, +1},//7 +4
                {+1, +1, +1, +1, 0, +1, +1, +1, +1, +1, 0, +1, +1, +1, +1},//8 -2
                {+1, +1, +1, +1, 0, +1, +1, +1, +1, 0, 0, +1, +1, +1, +1},//9 0
        };
        //различные представления пятерки
        /*int[][] num5 = { {1,1,1,1,0,0,1,1,1,0,0,0,1,1,1},
                {1,1,1,1,0,0,0,1,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,1,1,0,0,1,1,1,1},
                {1,1,0,1,0,0,1,1,1,0,0,1,1,1,1},
                {1,1,0,1,0,0,1,1,1,0,0,1,0,1,1},
                {1,1,1,1,0,0,1,0,1,0,0,1,1,1,1}
        };*/
        int[][] num5 = { {0,1,0,0,1,0,1,1,0,1,1,0,0,1,1}
        };
        int checkedNum = 1;
        int option=0;
        int repeatNum = 10000;
        for (int i = 0; i < repeatNum; i++) {
            option = rnd(9);
            if (option != checkedNum) {
                //# Если сеть выдала True/Да/1, то наказываем ее
                if (proceed(nums[option]))
                    decrease(nums[option]);
            } else {
                //# Если получилось число 5
                if (!proceed(nums[checkedNum])) increase(nums[checkedNum]);
            }
        }
        //# Вывод значений весов
        System.out.println(Arrays.toString(weights));
        int sum=0;
        for (int i : weights) {
            sum+=i;
        }
        System.out.println("summa="+sum);
        //# Прогон по обучающей выборке
        for (int i = 0; i < nums.length; i++) {
            System.out.println(String.format("%d это %d? %s",i,checkedNum,proceed(nums[i])));
        }
        //# Прогон по тестовой выборке
        for (int i = 0; i < num5.length; i++) {
            System.out.println(String.format("Узнал %d? %s",checkedNum,proceed(num5[i])));
        }
    }

    public static int rnd(int max)
    {
        return (int) (Math.random() * ++max);
    }

    public static boolean proceed(int[] num){
        //# Является ли число num 5 (checkedNum)
        int net = 0;
        for (int i = 0; i < num.length; i++) {
            net += num[i]*weights[i];
        }
        //# Превышен ли порог? (Да - сеть думает, что это 5. Нет - сеть думает, что это другая цифра)
        return net >=bias;
    }
    private static void changeWeights(int[] num, int koef){
        //# Уменьшение значений весов, если сеть ошиблась и выдала true
        for (int i = 0; i < num.length; i++) {
            if (num[i]==1) weights[i]+=koef;
        }
    }
    public static void increase(int[] num){
        changeWeights(num,1);
    }
    public static void decrease(int[] num){
        changeWeights(num,-1);

    }
    public static boolean saveNetworkToFile(int[] nums) throws IOException {
        File file = new File("D:\\work\\test\\file.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Cannot create the file: " + file.getPath());
            throw e;
        }
        return true;
    }
}
