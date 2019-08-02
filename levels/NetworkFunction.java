package levels;
//интерфейс для получения входного и выходного значения нейрона
public interface NetworkFunction {
    //сама функция
    double process(double inParam);
    //производная
    double derivative(double inParam);
}
