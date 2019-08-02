package levels;

import java.util.ArrayList;

public class InputLevel extends Level {
    public InputLevel(NeuronNet net,int neuronsCnt) {
        super(net,0, LevelType.INPUT, neuronsCnt);
    }

    @Override
    Neuron createNeuron(int index) {
        Neuron n = new InputNeuron(this, index);
        return n;
    }

    @Override
    public void setInput(){
        double[] input = net.getInput();
        if (input.length<this.neurons.size()-1)
            throw new IllegalArgumentException("Количество входных нейронов и переданных входных значений не совпадает!");
        for (int i = 0; i < this.neurons.size()-1; i++) {
            ((InputNeuron)neurons.get(i)).setIn(input[i]);
        }
    }

    @Override
    public void resetDelta() {
        return;
    }

    @Override
    public void createLinks(boolean fillRnd) {
        throw new IllegalArgumentException("Функция не может быть использована в данном контексте!");
    }

    @Override
    public String getDeltaWeightMatrix() {
        StringBuffer sb = new StringBuffer();
        Level nextLevel = this.getNextLevel();
        sb.append(String.format("L%d<->L%d(matrix of delta weights)\n",this.getIndex(),this.getNextLevel().getIndex()));
        ArrayList<Neuron> n =  nextLevel.neurons;
        for (int i = 0; i < n.size()-1; i++) {//исключаем bias
            sb.append("|");
            for (NeuronLink oLink : ((HiddenNeuron) n.get(i)).getInLinkList()) {
                sb.append(String.format("%8.5f\t",oLink.getDelta()));
            }
            sb.append("|").append(System.lineSeparator());
        }
        return sb.toString();
    }
}
