package levels;

public class InputFunc implements NetworkFunction {
    @Override
    public double process(double inParam) {
        return inParam;
    }

    @Override
    public double derivative(double inParam) {
        return 0;
    }
}
