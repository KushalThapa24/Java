abstract class Employee{
    double rate;
    Employee(double rate){
        this.rate = rate;
    }
    abstract double salary();
}
class FullTime extends Employee{

    FullTime(double rate) {
        super(rate);
    }
    @Override
    double salary() {
        return rate * 40;
    }
}
class PartTime extends Employee{

    PartTime(double rate) {
        super(rate);
    }
    @Override
    double salary() {
        return rate * 20;
    }
}
public class inheritanceEmployee {
    public static void main(String[] args) {
        Employee e1 = new FullTime(500);
        System.out.println( e1.salary());

        Employee e2 = new PartTime(200);
        System.out.println(e2.salary());
    }
}