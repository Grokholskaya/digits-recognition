package levels;

public class OutputLevel extends Level {
    private transient double totalError;
    public OutputLevel(NeuronNet net, int index, int neuronsCnt) {
        super(net, index, LevelType.OUTPUT, neuronsCnt);
    }

    @Override
    Neuron createNeuron(int index) {
        Neuron n = new OutputNeuron(this, index);
        return n;
    }

    public void setTarget(int[] target){
        if (this.neurons.size()>target.length)
            throw new IllegalArgumentException("Количество выходных нейронов и переданных целевых значений не совпадает!");
        for (int i = 0; i < this.neurons.size(); i++) {
            ((OutputNeuron)neurons.get(i)).setTarget(target[i]);
        }
    }

    @Override
    public void setInput() {
        //счиатаем входное значение для всех выходных нейронов
        for (int i = 0; i < this.neurons.size(); i++) {
            neurons.get(i).setIn();
        }
    }

    public void setTotalError(){
        double xTotalError=0;
        OutputNeuron o;
        for (int i = 0; i < this.neurons.size(); i++) {
            o = (OutputNeuron)neurons.get(i);
            o.setError();
            xTotalError+=o.getError();
        }
        totalError+=xTotalError;
        //return totalError/NeuronNet.learningInputLength;
    }

    public double getTotalError() {
        return totalError/NeuronNet.learningInputLength;
    }

    public void resetTotalError(){
        totalError = 0;
    }

    @Override
    public String getWeightMatrix() {
        return getPrevLevel().getWeightMatrix();
    }

   /* @Override
    public String getDeltaWeightMatrix() {
        return null;
    }*/
}
