class vehicle{
    protected String brand;
    vehicle(String brand){
        this.brand = brand;
    }
    void start(){
        System.out.println("Started");
    }
    void stop(){
        System.out.println("Stopped");
    }
    void display(){
        System.out.println("Brand: " + brand);
    }
    }
    class car extends vehicle{
        car(String brand){
            super(brand);
        }
        @Override
        void display() {
            System.out.println("Car Brand: " + brand);
        }
    }
    class motorcycle extends vehicle{
        motorcycle(String brand){
            super(brand);
        }
        @Override
        void display() {
            System.out.println("It is Honda Bike");
        }
    }
    public class inheritanceVehicle{
        public static void main(String[] args) {
            car c1 = new car("Toyota");
            c1.start();
            c1.display();
            c1.stop();

            motorcycle myBike = new motorcycle("Honda");
            myBike.start();
            myBike.display();
            myBike.stop();
        }
    }
