package levels;

public class HiddenLevel extends Level {
    public HiddenLevel(NeuronNet net, int index, int neuronsCnt) {
        super(net,index, LevelType.HIDDEN, neuronsCnt);
    }

    @Override
    Neuron createNeuron(int index) {
        Neuron n = new HiddenNeuron(this, index);
        return n;
    }

}
