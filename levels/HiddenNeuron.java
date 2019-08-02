package levels;

import java.util.ArrayList;
import java.util.Iterator;

public class HiddenNeuron extends OutputNeuron {
    //исходящие от нейрона связи
    private ArrayList<NeuronLink> outLinkList = null;// new ArrayList<>();

    public ArrayList<NeuronLink> getOutLinkList() {
        if (outLinkList ==null) setOutLinkList();
        return outLinkList;
    }

    protected void setOutLinkList() {
        this.outLinkList = new ArrayList<>();
        //исходящие связи принадлежат следующему уровню
        Iterator<NeuronLink> linkIterator = this.getLevel().getNextLevel().getLinks().iterator();
        NeuronLink link = null;
        while (linkIterator.hasNext()){
            link = linkIterator.next();
            if (link.getStartNeuron()==this) outLinkList.add(link);
        }
    }

    public HiddenNeuron(Level level, int index) {
        super(level, index);
    }

    @Override
    public void setSigma() {
        //получаем ошибку как взвешенную сумму всех ошибок от нейронов следущего слоя
        double summa = 0;
        for (NeuronLink oLink : getOutLinkList()) {
            summa+=oLink.getEndNeuron().getSigma()*oLink.getWeight();
        }
        setDerivative();
        sigma = summa*getDerivative();
    }

    @Override
    public String toString() {
        ArrayList<NeuronLink> links =  this.getOutLinkList();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < links.size(); i++) {
            sb.append(links.get(i).toString());
            if (i<links.size()-1) sb.append(", ");
        }
        return sb.toString();
    }
}
