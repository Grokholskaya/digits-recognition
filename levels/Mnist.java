package levels;

import java.io.*;
import java.util.*;

public class Mnist  implements Iterable<Mnist.trainingSet>, Serializable {
    static final long SerialVersionUID = 1L;
    private String filePath = "D:\\work\\test\\mnist.txt";
    public  String DIRPATH;// = "D:\\work\\test\\data";
    private transient static LinkedList<String> badFileList = null;
    private transient static String srcFileName;
    private String[] filesList = null;
    //private transient static int testCnt = 0;
    public transient boolean isSaveToFile = false;
    private LinkedList<String>[] groupDataList= null;

    public String getFolderPath() {
        return DIRPATH;
    }

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
    };

   /* public String[] getFilesList() {
        return filesList;
    }*/
    public int getFileListCount(int index){
        if (filesList == null) return 0;
        if (index==-1) return filesList.length;
        else return groupDataList[index].size();
        //return testCnt==0?filesList.length:testCnt;
    }
    private double[] input;
    private int target;

    public Mnist(String DIRPATH, boolean loadFileList) {
        this.DIRPATH = DIRPATH;
        if (loadFileList) setFilesList();
    }

    public trainingSet processFile(int target, int index) throws IOException {
        if (target==-1)
            setSrcFileName(filesList[index]);
        else
            setSrcFileName(groupDataList[target].get(index));
        return new trainingSet(target==-1?-1:0,this.input,this.target,index);
    }

    public trainingSet processFile(String srcFileName) throws IOException {
        setSrcFileName(srcFileName);
        //return new trainingSet(this.input, this.target, -1);
        return new trainingSet(-1,this.input,this.target,-1);
    }

    private void setSrcFileName(String srcFileName) throws IOException {
        this.srcFileName = srcFileName;
        processFile();
    }
    public void processFile() throws IOException {
        List<Integer> listInput = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(DIRPATH+"\\"+srcFileName))))
        {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String s : line.split("\t")) {
                    listInput.add(Integer.parseInt(s));
                }
                //System.out.println(line);
            }
            if (listInput.size()>784)
                target = listInput.remove(listInput.size()-1);
            else
                target = -1;
            input = listInput.stream().mapToDouble(i -> i/255d).toArray();
        }
    }

    public void setFilesList() {
        try {
            loadFromFile();
        } catch (IOException | ClassNotFoundException e) { /*e.printStackTrace();*/ System.out.println("Need to save MNIST data in file..."); }
        if (filesList == null) {
            isSaveToFile = true;
            File folder = new File(DIRPATH);
            filesList = folder.list();
            this.groupDataList = new LinkedList[10];
            for (int i = 0; i < groupDataList.length; i++) {
                groupDataList[i] = new LinkedList<>();
            }
            Iterator<Mnist.trainingSet> iterator = iterator();
            while (iterator.hasNext()){
                Mnist.trainingSet t = iterator.next();
                groupDataList[t.getTargetInd()].add(t.getFileName());
            }
        }
    }

    @Override
    public Iterator<Mnist.trainingSet> iterator() {
        return new MNISTIterator(this, -1);
    }

    //SamplesCapacity - суммарный объем тестовой выборки
    public BatchMNISTIterator batchMNISTIterator(int digitsCnt, int totalSamples){
        return new BatchMNISTIterator(this,idealOutput.length,digitsCnt,totalSamples);
    }

    //создаем массив итераторов для каждой цифры
    public Iterator<Mnist.trainingSet>[] iterators(int length){
        Iterator<Mnist.trainingSet>[] result = new Iterator[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new MNISTIterator(this, i);
        }
        return result;
    }

    /*public class trainingSet{
        public int[] getInput() {
            return input;
        }

        public int[] getTarget() {
            return idealOutput[target];
        }

        public int getTargetInd(){
            return target;
        }

        public String getFileName(){
            return srcFileName;
        }
    }*/

    public class trainingSet{
        private transient double[] tInput;
        private transient int tTarget;
        private transient int tIndex;
        private transient int base;//=-1 ищем по всему fileList,0 ищем по groupFileList[target]

        public trainingSet(int base, double[] tInput, int tTarget, int tIndex) {
            this.base = base;
            this.tInput = Arrays.copyOf(tInput, tInput.length);
            this.tTarget = tTarget;
            this.tIndex = tIndex;
        }

        public double[] getInput() {
            return tInput;
        }

        public int[] getTarget() {
            return idealOutput[tTarget];
        }

        public int getTargetInd(){ return tTarget; }

        public String getFileName(){
            if (tIndex==-1) return srcFileName;
            if (base==-1) return filesList[tIndex];
            else return groupDataList[tTarget].get(tIndex);
        }
    }

    public static void saveGroupDataList() throws IOException {
        //Mnist m = new Mnist("D:\\work\\test\\data", true);
        Mnist m = new Mnist("D:\\work\\test\\test_samples", true);
        m.groupDataList = new LinkedList[10];
        for (int i = 0; i < m.groupDataList.length; i++) {
            m.groupDataList[i] = new LinkedList<>();
        }
        Iterator<Mnist.trainingSet> iterator = m.iterator();
        while (iterator.hasNext()){
            Mnist.trainingSet t = iterator.next();
            m.groupDataList[t.getTargetInd()].add(t.getFileName());
            //System.out.print(t.getFileName());
            //System.out.println(t.getTargetInd());
        }
        m.saveToFile();
    }

    public void addFileToBadList(String fileName){
        if (badFileList ==null)
            badFileList = new LinkedList<>();
        badFileList.add(fileName);
    }
    public void writeBadFileList(String fileName) throws IOException {
        if (badFileList == null || badFileList.size()==0) return;
        File file = new File(fileName);
        // Создание файла
        file.createNewFile();
        FileWriter fr = new FileWriter(file);
        for (String s : badFileList) {
            fr.write(s);
            fr.write(System.lineSeparator());
        }
        fr.flush();
        fr.close();
    }

    public void saveToFile() throws IOException {
        File file = new File(filePath);
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

    public int loadFromFile() throws IOException, ClassNotFoundException {
        int result=0;
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Mnist m = (Mnist) objectInputStream.readObject();
        if (m.DIRPATH.equals(this.DIRPATH)){
            this.filesList =  m.filesList;
            this.groupDataList = m.groupDataList;
            result = 1;//данные загружены
        }
        objectInputStream.close();
        fileInputStream.close();
        return result;
        //this.data = weight.data;
    }
}
