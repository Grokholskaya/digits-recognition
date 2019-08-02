package levels;

public class Sigmoid /*implements NetworkFunction*/ {
    public static double process(double inParam) {
        //Sigmoid(x)=S(x)=1/(1+e−x)
        return 1/(1+Math.exp(-inParam));
    }

    public static double derivative(double inParam) {
        //для сигмоида ее производная (1 - f(x)) * f(x)
        double fx = process(inParam);
        return fx*(1-fx);
    }
}
