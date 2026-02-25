class Animal {
    void display() {
        System.out.println("Animal makes sound");
    }

}

class Dog extends Animal {
    void display() {
        System.out.println("Dog makes sound");
    }

}

class Cat extends Animal {
    void display() {
        System.out.println("Cat makes sound");
    }

}

class Tiger extends Animal {
    void display() {
        System.out.println("Tiger makes sound");
    }

}

public class Poly {

    public static void main(String[] args) {
        Animal[] animal = new Animal[3];
        animal[0] = new Dog();
        animal[1] = new Tiger();
        animal[2] = new Cat();

        for (Animal a : animal) {// for (a in animal)
            a.display();
        }

    }

}
