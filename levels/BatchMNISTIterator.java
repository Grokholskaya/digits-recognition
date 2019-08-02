package levels;

import java.util.Iterator;
import java.util.LinkedList;

//возвращаем батч(список) с образцами,каждая цифра в количестве digitsCnt
public class BatchMNISTIterator implements Iterator<LinkedList<Mnist.trainingSet>> {
    private Mnist mnist;
    private int digitsCnt;
    private int targetsCnt;
    private int totalSamples;
    private Iterator<Mnist.trainingSet>[] iterators;
    private int batchNom;
    private int batchTtl;
    //digitsCnt - количество образцов каждой цифры, результирующий батч будет digitsCnt*targetsCnt(=10) образцов
    //targetsCnt - количество выходных нейронов - цифр
    public BatchMNISTIterator(Mnist mnist, int targetsCnt, int digitsCnt, int totalSamples) {
        this.mnist = mnist;
        this.digitsCnt = digitsCnt;
        this.targetsCnt = targetsCnt;
        this.totalSamples = totalSamples;
        batchTtl = totalSamples/(digitsCnt*targetsCnt);
        iterators = mnist.iterators(targetsCnt);
    }

    public int getBatchNom() {
        return batchNom;
    }

    @Override
    public boolean hasNext() {
        if (batchNom>batchTtl) return false;
        //пока какой-то итератор что-то возвращает, считаем что и итератор батчей тоже что-то вернет
        for (Iterator<Mnist.trainingSet> iterator : iterators) {
            if (iterator.hasNext()) return true;
        }
        return false;
    }

    @Override
    public LinkedList<Mnist.trainingSet> next() {
        LinkedList<Mnist.trainingSet> result=new LinkedList<>();
        for (int i = 0; i < targetsCnt; i++) {
            for (int j = 0; j < digitsCnt; j++) {
                if (iterators[i].hasNext()) result.add(iterators[i].next());
            }
        }
        batchNom++;
        return result;
    }

    public void reset(){
        for (Iterator<Mnist.trainingSet> iterator : iterators) {
            ((MNISTIterator)iterator).reset();
        }
        batchNom = 0;
    }
}
