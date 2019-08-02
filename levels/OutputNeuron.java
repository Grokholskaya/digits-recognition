package levels;

import java.util.ArrayList;
import java.util.Iterator;

public class OutputNeuron extends Neuron {
    private transient double target;//идеальное выходное значение
    private transient double error;
    //список входящих связей
    protected transient ArrayList<NeuronLink> inLinkList = null;

    public OutputNeuron(Level level, int index) {
        super(level, index);
    }

    public ArrayList<NeuronLink> getInLinkList() {
        if (inLinkList == null) setInLinkList();
        return inLinkList;
    }

    public double getError() {
        return error;
    }

    protected void setInLinkList() {
        this.inLinkList = new ArrayList<>();
        //входящие связи принадлежат этому же уровню что и сам нейрон
        Iterator<NeuronLink> linkIterator = this.getLevel().getLinks().iterator();
        NeuronLink link = null;
        while (linkIterator.hasNext()){
            link = linkIterator.next();
            if (link.getEndNeuron()==this) inLinkList.add(link);
        }
    }

    public void setTarget(double target) {
        this.target = target;
    }

    //исходящих связей нет

    @Override
    public void setIn() {
        double summa=0;
        /*if (this.getLevel().getIndex()==3 && this.getIndex()==0*//* && this instanceof OutputNeuron*//*){
            System.out.printf("L%d\n",this.level.getIndex());
            for (NeuronLink link : getInLinkList()) {
                System.out.printf("%.3f*%.3f+", link.getStartNeuron().getOut(),link.getWeight());
            }
        }*/
        for (NeuronLink link : getInLinkList()) {
            summa +=link.getStartNeuron().getOut()*link.getWeight();
        }
        in = summa;
        /*if (this.getLevel().getIndex()==3 && this.getIndex()==0 *//*&& this instanceof OutputNeuron*//*){
            System.out.printf("=%.3f;%.3f\n",summa, Sigmoid.process(summa));
        }*/
    }

    @Override
    public void setSigma() {
        //для выходного нейрона величина ошибки вычисляется как разность
        // между идеальным и реальным значением*производную выходной функции
        //это значение передаем не предыдущий слой для суммирования
        setDerivative();
        sigma =  (getOut()-target)*getDerivative();
    }

    public void setError(){
        error = 0.5*Math.pow(getOut()-target,2);
    }

    @Override
    public String toString() {
        return String.format("(%d):s=%.5f(out=%.5f)",this.getIndex(),this.sigma, this.getOut());
    }
}
