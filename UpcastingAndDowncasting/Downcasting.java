public class Downcasting {

    public static void main(String[] args) {

        Animal a1 = new Dog();
        Dog d1 = (Dog) a1;
        d1.bark();
        d1.sound();
        a1.sound();

    }

}

// Upcasting hides child features
// Downcasting reveals child features
// Reference controls visibility, object controls execution.