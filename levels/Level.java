package levels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

abstract class Level implements Serializable {
    //public static int calc = 0;
    public boolean debug = false;
    protected transient NeuronNet net;
    private LevelType levelType;
    private int index;
    protected transient ArrayList<Neuron> neurons;
    private ArrayList<NeuronLink> links;//список входящих в нейроны данного уровня связей
    //решила так сделать, так как их можно создать на стадии создания нейронов уровня,
    // предыдущий уровень уже будет создан


    public ArrayList<NeuronLink> getLinks() { return links; }
    public LevelType getLevelType() { return levelType; }
    public int getIndex(){ return index; };

//создаем уровень типа levelType с количеством нейронов neuronsCnt
    public Level(NeuronNet net, int index, LevelType levelType, int neuronsCnt) {
        this.index = index;
        this.net = net;
        this.levelType = levelType;
        neurons = new ArrayList<>();
        for (int i = 0; i < neuronsCnt; i++) {
            Neuron n = createNeuron(i);
            neurons.add(n);
        }
        //добавляем нейрон смещения
        if (!(this instanceof OutputLevel))
        neurons.add(new BiasNeuron(this,neuronsCnt));
    }

    public void createLinks(boolean fillRnd){
        Random rnd = null;
        if (fillRnd) rnd = new Random();
        //без bias-а
        NeuronLink nl = null;
        links = new ArrayList<>();
        ArrayList<Neuron> prevLevelNeurons = this.getPrevLevel().neurons;
        for (int i = 0; i < neurons.size(); i++) {
            if (!(this instanceof OutputLevel) && i== neurons.size()-1) continue;
            for (Neuron start : prevLevelNeurons) {
                nl = new NeuronLink(start,neurons.get(i),fillRnd?rnd.nextGaussian():0);
                links.add(nl);
            }
        }
    }
    public Level getPrevLevel(){
        if (index==0) throw new IllegalArgumentException("Это первый уровень сети!");
        return this.net.getLevel(this.index-1);
    }
    public Level getNextLevel(){
        if (this instanceof OutputLevel) throw new IllegalArgumentException("Это последний уровень сети!");
        return this.net.getLevel(this.index+1);
    }
    abstract Neuron createNeuron(int index);

    public void setInput(){
        //счиатаем входное значение для всех нейронов кроме Bias
        for (int i = 0; i < this.neurons.size()-1; i++) {
            neurons.get(i).setIn();
        }
    }

    public void setOutput(){
        for (int i = 0; i < this.neurons.size(); i++) {
            neurons.get(i).setOut();
        }
    }

    public void setSigma(){
        if (debug)
            System.out.println("level"+index);
            //System.out.println(getWeightMatrix());

        for (int i = 0; i < this.neurons.size(); i++) {
            neurons.get(i).setSigma();
        }
        if (debug) {
            System.out.println(getStringDerivative());
            System.out.println(getStringSigma());
            System.out.println(getStringOut());
        }
    }

    public void setDelta(){
        for (NeuronLink link : links) {
            link.setDelta();
        }
    }

    public void resetDelta(){
        for (NeuronLink link : links) {
            link.resetDelta();
        }
    }

    public int setNewWeight(){
        //calc++;
        if (debug)
            System.out.println(getDeltaWeightMatrix());
        int cntRecalc = 0; //количество ненулевых дельт
        for (NeuronLink link : links) {
            cntRecalc += link.setNewWeight();
        }
        return cntRecalc;
    }

    public double[] getOutput(){
        double[] result = new double[this.neurons.size()];
        for (int i = 0; i < neurons.size(); i++) {
            result[i] = neurons.get(i).getOut();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("---"+this.index+"---");
        sb.append(System.lineSeparator());
        for (Neuron neuron : neurons) {
            sb.append(neuron.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String getWeightMatrix() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("L%d<->L%d(matrix of weights)\n",this.index,this.getNextLevel().index));
        //выведем все сигмы следующего слоя
        sb.append(this.getNextLevel().getStringSigma());
        sb.append(System.lineSeparator());
        for (int i = 0; i < neurons.size(); i++) {
            sb.append("|");
            for (NeuronLink oLink : ((HiddenNeuron) neurons.get(i)).getOutLinkList()) {
                sb.append(String.format("%8.5f\t",oLink.getWeight()));
                //summa+=oLink.getEndNeuron().getSigma()*oLink.getWeight();
            }
            sb.append("|").append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String getDeltaWeightMatrix() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("L%d<->L%d(matrix of delta weights)\n",this.getPrevLevel().index, this.index));
        for (int i = 0; i < neurons.size()-1; i++) {
            sb.append("|");
            for (NeuronLink oLink : ((OutputNeuron) neurons.get(i)).getInLinkList()) {
                sb.append(String.format("%8.5f\t",oLink.getDelta()));
            }
            sb.append("|").append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String getStringDerivative(){
        StringBuffer sb = new StringBuffer();
        sb.append("d");
        for (Neuron neuron : neurons) {
            sb.append(String.format("%8.5f\t",neuron.getDerivative()));
        }
        //sb.append(System.lineSeparator());
        return sb.toString();
    }

    public String getStringSigma(){
        StringBuffer sb = new StringBuffer();
        sb.append("s");
        for (Neuron neuron : neurons) {
            sb.append(String.format("%8.5f\t",neuron.getSigma()));
        }
        //sb.append(System.lineSeparator());
        return sb.toString();
    }

    public String getStringOut(){
        StringBuffer sb = new StringBuffer();
        sb.append("o");
        for (Neuron neuron : neurons) {
            sb.append(String.format("%8.5f\t",neuron.getOut()));
        }
        //sb.append(System.lineSeparator());
        return sb.toString();
    }
}

