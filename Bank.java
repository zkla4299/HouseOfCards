import java.util.*;
class Bank {
    private int balance;
    private final int blueChipValue;
    private final int redChipValue;
    private final int blackChipValue;

    public Bank(int initialBalance, int blueChipValue, int redChipValue, int blackChipValue) {
        this.balance = initialBalance;
        this.blueChipValue = blueChipValue;
        this.redChipValue = redChipValue;
        this.blackChipValue = blackChipValue;
    }

    public void addToBalance(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }

    public void subtractFromBalance(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalStateException("Insufficient balance: " + balance + " < " + amount);
        }
        balance -= amount;
    }

    public int getBalance() { return balance; }
}