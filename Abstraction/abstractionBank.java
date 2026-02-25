abstract class BankAccount {
    protected double balance;
    BankAccount (double bal){
        this.balance = bal;
    }
    abstract void deposit(double amount);
    abstract void withdraw(double amount);
    abstract void display();
}

class Saving extends BankAccount{

    public Saving() {
        super(0);
    }

    @Override
    void deposit(double amount) {
        balance += amount;
    }
    @Override
    void withdraw(double amount) {
        if(amount <= balance){
            balance -= amount;
        } else {
            System.out.println("Insufficient balance in Saving Account");
        }
    }
    @Override
    void display() {
        System.out.println("Saving Account Balance: " + balance);
    }
}

class Current extends BankAccount{

    public Current() {
        super(0);
    }
    @Override
    void deposit(double amount) {
        balance += amount;
    }
    @Override
    void withdraw(double amount) {
        if(amount <= balance){
            balance -= amount;
        } else {
            System.out.println("Insufficient balance in Current Account");
        }
    }   
    @Override
    void display() {
        System.out.println("Current Account Balance: " + balance);
    }
}

public class abstractionBank {
    public static void main(String[] args) {
        BankAccount savingAccount = new Saving();
        savingAccount.deposit(500);
        savingAccount.withdraw(200);
        savingAccount.display();

        BankAccount currentAccount = new Current();
        currentAccount.deposit(1000);
        currentAccount.withdraw(500);
        currentAccount.display();
    }
}
