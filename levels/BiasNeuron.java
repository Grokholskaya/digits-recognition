package levels;

public class BiasNeuron extends InputNeuron {
    public BiasNeuron(Level level, int index) {
        super(level, index);
        in = 1;
    }

    @Override
    public void setIn() {
        in = 1;
    }

    @Override
    public String toString() {
        return "b"+super.toString();
    }
}
