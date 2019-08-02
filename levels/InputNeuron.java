package levels;

import java.util.ArrayList;

public class InputNeuron extends HiddenNeuron{
    @Override
    public ArrayList<NeuronLink> getInLinkList() {
        throw new IllegalArgumentException("Input level cannot have input links!");
    }

    public InputNeuron(Level level, int index) {
        super(level, index);
    }

    @Override
    public void setIn() { }

    public void setIn(double value){
        in = value;//передаем значение из обучающей выборки
    }
}
