
public class Upcasting {

    public static void main(String[] args) {
        // upcasting
        Animal a1 = new Dog();
        Dog d1 = new Dog();// this can acces both parent and child methods unlike upcast and down...
        a1.sound();
        d1.sound();
        d1.bark();
        // a1.bark(); this is not running because of upcasting

    }

}
