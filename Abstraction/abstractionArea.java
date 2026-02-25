abstract class Shape {
    abstract double CalculateArea();
    abstract void DisplayArea();
}

class Circle extends Shape{
    double radius;
    Circle(double radius){
        this.radius = radius;
    }
    @Override
    double CalculateArea() {
        return Math.PI * radius * radius;
    }
    @Override
    void DisplayArea() {
        System.out.println("Area of Circle: " + CalculateArea());
    }
}

class Rectangle extends Shape{
    double length, breadth;
    Rectangle(double length, double breadth){
        this.length = length;
        this.breadth = breadth;
    }
    @Override
    double CalculateArea() {
        return length * breadth;
    }
    @Override
    void DisplayArea() {
        System.out.println("Area of Rectangle: " + CalculateArea());
    }
}

public class abstractionArea {
    public static void main(String[] args) {
        Shape circle = new Circle(7);
        circle.DisplayArea();

        Shape rectangle = new Rectangle(5, 10);
        rectangle.DisplayArea();
    }
}