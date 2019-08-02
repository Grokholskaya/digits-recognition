package levels;

abstract class Neuron implements NeuronMethods {
    protected Level level;
    protected double in;
    protected double out;
    protected double sigma;//величина ошибки dEtotal/dw
    private double derivative;
    private int index;
    //private static Sigmoid func = new Sigmoid();

    public Level getLevel() { return level;}
    public int getIndex(){ return index; };
    public double getIn() { return in; }
    public double getOut(){ return out; };
    public double getSigma() { return sigma; }
    public double getDerivative(){ return derivative; }

    @Override
    public void setOut() {
        out = Sigmoid.process(getIn());
       /* if (this.level.getIndex()==3 && this.getIndex()==0*//* && this instanceof OutputNeuron*//*){
            System.out.printf("L%d:F(%d)=%.3f\n",this.level.getIndex(),  this.getIndex(),out);
        }*/
    }

    public void setDerivative() {
        this.derivative = Sigmoid.derivative(out);
    }

    public void setIndex(int index) { this.index = index;}

    //abstract double setIn();//вынесла в интерфейс, зачем, пока не знаю
    //abstract double setSigma();

    public Neuron(Level level, int index) {
        this.level = level;
        this.index = index;
    }
}
