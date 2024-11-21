public class RoulettePlayer {
    // Private member variables
    private double balance;
    private int betDenom;
    private int spinCount;

    // Constructor to initialize the Player
    public RoulettePlayer(double balance, int betDenom, int spinCount) {
        this.balance = balance;
        this.betDenom = betDenom;
        this.spinCount = spinCount;
    }

    // Getter for balance
    public double getBalance() {
        return balance;
    }

    // Setter for betDenom
    public void setBetDenom(int denom) {
        this.betDenom = denom;
    }

    // Getter for betDenom
    public int getBetDenom() {
        return betDenom;
    }

    // Setter for spinCount
    public void setSpinCount(int count) {
        this.spinCount = count;
    }

    // Getter for spinCount
    public int getSpinCount() {
        return spinCount;
    }

    // Method to adjust balance
    public void adjustBalance(double amount) {
        this.balance += amount;
    }
}
