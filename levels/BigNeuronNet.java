package levels;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class BigNeuronNet extends NeuronNet {
    private transient int digitsInBatch;//размер пакета, через который будем пересчитывать дельту
    private transient static Mnist m = null;
    private transient int totalSamples;
    public BigNeuronNet(int[] nCnt, int digitsInBatch, Mnist m) {
        super(nCnt);
        this.digitsInBatch = digitsInBatch;
        this.totalSamples = totalSamples;
        this.m = m;
        this.path = "D:\\work\\test\\bigdata.txt";
    }

    public void initData(int[] nCnt,int digitsInBatch, int totalSamples){
        this.nCnt = nCnt;
        this.digitsInBatch = digitsInBatch;
        this.totalSamples = totalSamples;
        isReady = false;
        createLevels(nCnt,true);
    }

    public BigNeuronNet(Mnist m) {
        super();
        this.path = "D:\\work\\test\\bigdata.txt";
        this.m = m;
        //this.m = new Mnist(folderPath, false);
    }

    @Override
    public void learn(int iStep){
        int step =0;
        int result=0;
        int digitsCnt = 3;
        learningInputLength = digitsInBatch*10;
        BatchMNISTIterator batchIterator = m.batchMNISTIterator(digitsCnt,totalSamples);

        while (step<=iStep){//количество прогонов по всей выборке
            if (step>0){
                batchIterator.reset();
            }
            while (batchIterator.hasNext()){
                if (batchIterator.getBatchNom() > 0 || step > 0) {
                    result = 0;
                    this.getOutputLevel().resetTotalError();
                    for (Level level : levels) {
                        level.resetDelta();
                    }
                }
                LinkedList<Mnist.trainingSet> trainingSet =  batchIterator.next();
                for (Mnist.trainingSet set : trainingSet) {
                    setTestInput(set.getInput());
                    //1. Идем слева направа, считаем ошибку
                    for (Level level : this.levels) {
                        level.setInput();
                        level.setOutput();
                        //для выходного уровня устанавливаем вектор идеальных значений для расчета ошибки
                        if (level.getLevelType()== LevelType.OUTPUT)
                            ((OutputLevel) level).setTarget(set.getTarget());
                    }

                    this.getOutputLevel().setTotalError();
                    Level level;
                    //2.идем обратно считаем дельту
                    for (int j = levels.size()-1; j > 0; j--) {
                        level = levels.get(j);
                        level.setSigma();
                        level.setDelta();
                        //result += level.setNewWeight();
                    }
                }
                for (int i = 1; i < levels.size(); i++) {
                    result += levels.get(i).setNewWeight();
                }
                if (step ==0 && batchIterator.getBatchNom()==1)
                    System.out.println("start total error="+this.getOutputLevel().getTotalError());
            }
            step++;
        }
        isReady = true;
        System.out.println("end total error="+this.getOutputLevel().getTotalError());
        //System.out.println("result="+result+"; step="+ step);
        //System.out.println(printNeurons());
    }

    public int recognize(String inputFileName){
        try {
            Mnist.trainingSet t = m.processFile(inputFileName);
            double[] input = t.getInput();
            if (isDebug)
            System.out.println("The number must be: "+t.getTargetInd());
            return super.recognize(input);
        } catch (IOException e) {
            System.out.println("File not found:"+inputFileName);
        }
        return -1;
    }

    public void recognizeAll(){
        int total = m.getFileListCount(-1);
        int hit=0;
        Iterator<Mnist.trainingSet> iterator = m.iterator();
        while (iterator.hasNext()){
            Mnist.trainingSet t = iterator.next();
            if (recognize(t.getInput())==t.getTargetInd()) hit++;
            else {
                if (this.isDebug)
                //добавляем список нераспознанных файлов
                m.addFileToBadList(t.getFileName());
            };
        }
        System.out.printf("The network prediction accuracy: %d/%d, %d%%\n",hit,total, (int)(hit*1d/total*100));
        if (this.isDebug)
        try {
            m.writeBadFileList("D:\\work\\test\\badfiles.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveToFile() throws IOException {
        super.saveToFile();
        if (m.isSaveToFile) m.saveToFile();
    }
}
