public class balance {
    public static void main(String[] args) {
        int bal = 5;
        
        try {
            if (bal < 10) {
                throw new Exception();
            }
            System.out.println("Balance is sufficient: " + bal);
        } catch (Exception e) {
            System.out.println("Error: Balance is less than 10 ");
        }
    }
}