public class User implements UserInterface{
    String firstName = "";
    String lastName = "";
    String email = "";

    double balance = 0;

    public void User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public double deposit(double amount){
        balance += amount;
        return balance;
    }

    public void withdraw(int amount){
        balance -= amount;
    }



}