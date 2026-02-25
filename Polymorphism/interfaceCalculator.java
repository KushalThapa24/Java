interface Calculator{
    int sum(int a, int b);
    int sub(int a, int b);
    double mul(int a, int b);
    double div(int a, int b);
}
class Scientific implements Calculator{
    @Override
    public int sum(int a, int b) {
        return a+b;
    }
    @Override
    public int sub(int a, int b) {
        return a-b;
    }
    @Override
    public double mul(int a, int b) {
        return a*b;
    }
    @Override
    public double div(int a, int b) {
        return a/b;
    }
    double power(int a, int b){
        return Math.pow(a,b);
    }
}
class Basic implements Calculator{
    @Override
    public int sum(int a, int b) {
        return a+b;
    }
    @Override
    public int sub(int a, int b) {
        return a-b;
    }
    @Override
    public double mul(int a, int b) {
        return a*b;
    }
    @Override
    public double div(int a, int b) {
        return a/b;
    }
}
public class interfaceCalculator {
    public static void main(String[] args) {
        Calculator calc1 = new Scientific();
        Calculator calc2 = new Basic();
        System.out.println(calc2.mul(5, 2));
    }
}