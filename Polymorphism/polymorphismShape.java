abstract class shape{
    abstract void calculate();
    abstract void display();
}
 class Rectangle extends shape{
    @Override
    void calculate() {
        System.out.println("area= l*b");
    }
    @Override
    void display() {
        System.out.println("shape= Rectangle");
    }
}
 class Circle extends shape{
    @Override
    void calculate() {
        System.out.println("area= 3.14*r*r");
    }
    @Override
    void display() {
        System.out.println("shape= Circle");
    }
}
public class polymorphismShape {
    public static void main(String[] args) {
        shape s1 = new Rectangle();
        s1.calculate();
        s1.display();

        shape s2 = new Circle();
        s2.calculate();
        s2.display();
    }
}