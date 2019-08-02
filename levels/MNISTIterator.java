package levels;

import java.io.IOException;
import java.util.Iterator;

public class MNISTIterator implements Iterator<Mnist.trainingSet> {
    private Mnist mnist;
    private int listCnt;
    private int currentIndex;
    private int target;

    public MNISTIterator(Mnist mnist, int target) {
        this.mnist = mnist;
        this.target = target;
        this.listCnt = mnist.getFileListCount(target);
        currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < listCnt;
    }

    @Override
    public Mnist.trainingSet next()  {
        try {
            return this.mnist.processFile(target, currentIndex++);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void reset(){
        currentIndex=0;
    }
}
