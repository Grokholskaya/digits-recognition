package levels;

import java.io.Serializable;

public class NeuronLink implements Serializable {
    private transient Neuron startNeuron;
    private transient Neuron endNeuron;

    private double weight;//вес от нейрона startNeuron к endNeuron

    public double getWeight() {
        return weight;
    }
    private double delta;

    public Neuron getStartNeuron() { return startNeuron; }

    public Neuron getEndNeuron() { return endNeuron; }

    public NeuronLink(Neuron startNeuron, Neuron endNeuron, double weight) {
        //startNeuron.level.index<endNeuron.level.index;
        this.startNeuron = startNeuron;
        this.endNeuron = endNeuron;
        this.weight = weight;
    }

    public void setDelta(){
        delta += endNeuron.getSigma()*startNeuron.getOut();
    }
    public void setWeight(double weight) { this.weight = weight;}

    public double getDelta() {
        return delta;
    }

    public int setNewWeight() {
        delta = (delta/NeuronNet.learningInputLength)*NeuronNet.NJU;
        if (Math.abs(delta) < 1e-6) return 0;
        weight -= delta;
        //delta=0;
        return 1;
    }

    public void resetDelta(){
        delta = 0;
    }

    @Override
    public String toString() {
        return String.format("(%d;%d) %.3f",this.startNeuron.getIndex(),this.endNeuron.getIndex(), this.getWeight());
    }
}
