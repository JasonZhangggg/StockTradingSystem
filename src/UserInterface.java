interface UserInterface {
    String firstName = "";
    String lastName = "";
    String email = "";

    double balance = 0;

    public void User(String firstName, String lastName, String email);

    public double deposit(double amount);

    public void withdraw(int amount);



}