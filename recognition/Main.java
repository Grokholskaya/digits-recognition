package recognition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static int get2layers(String[] sInput){
        int[][] weights = {
                {-1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1},//1 +6
                {+1, +1, +1, -1, -1, +1, +1, +1, +1, +1, -1, -1, +1, +1, +1},//2 +0
                {+1, +1, +1, -1, -1, +1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//3 +0
                {+1, -1, +1, +1, -1, +1, +1, +1, +1, -1, -1, +1, -1, -1, +1},//4 +2
                {+1, +1, +1, +1, -1, -1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//5 +0
                {+1, +1, +1, +1, -1, -1, +1, +1, +1, +1, -1, +1, +1, +1, +1},//6 -1
                {+1, +1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1, -1, -1, +1},//7 +4
                {+1, +1, +1, +1, -1, +1, +1, +1, +1, +1, -1, +1, +1, +1, +1},//8 -2
                {+1, +1, +1, +1, -1, +1, +1, +1, +1, -1, -1, +1, +1, +1, +1},//9 -1
                {+1, +1, +1, +1, -1, +1, +1, -1, +1, +1, -1, +1, +1, +1, +1}//0  -1
        };
        int[] bias = {6,0,0,2,0,-1,4,-2,-1,-1};
        int[] output = new int[10];
        int[] input = new int[15];
        int o=0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if (sInput[i].charAt(j)=='X' || sInput[i].charAt(j)=='x'){
                    input[o++] = 1;
                }
                else input[o++] = 0;
            }
        }
        for (int i = 0; i < output.length; i++) {
            output[i]=bias[i];
            for (int j = 0; j < input.length; j++) {
                output[i]+=input[j]*weights[i][j];
            }
        }
        System.out.println(Arrays.toString(output));
        int max = Integer.MIN_VALUE;
        int max_index=-1;
        for (int i = 0; i < output.length; i++) {
            if (output[i]>max){
                max = output[i];
                max_index = i;
            }
        }
        if (max_index==output.length-1) return 0;
        else return ++max_index;
    }

    public static void main(String[] args) throws IOException {
        /*int[][] w = {{2,1,2},{4,-4,4},{2,-1,2}};
        int b= -5;
        int output=0;*/
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //String[] input = new String[3];
        String[] input = new String[5];
        for (int i = 0; i < 5; i++) {
            input[i]= reader.readLine();
        }
        System.out.printf("This number is %d%n",get2layers(input));
        /*for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (input[i].charAt(j)=='X' || input[i].charAt(j)=='x'){
                    output += w[i][j];
                }
            }
        }
        output+=b;
        System.out.printf("This number is %s%n",output>0?"0":"1");*/
    }
}
