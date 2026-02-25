public class encapsulationStudent {

    String name;
    int roll;
    float marks;

    public void setName(String name) {
        this.name = name;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void setMarks(float marks) {
        this.marks = marks;
    }

    public void display() {
        System.out.println(name + "  "+roll +"  "+ marks);
    }

    public static void main(String[] args) {

        encapsulationStudent s1 = new encapsulationStudent();
        s1.setMarks(12);
        s1.setName("Suyog");
        s1.setRoll(1);
        s1.display();

    }
}
