
public class PlayerPoker {
	private double balance;
	private int betDenom;
	private int spinCount;
	
	
	public PlayerPoker(double initialBalance) {
		this.balance = initialBalance;
	}


	public double getBalance() {
		return balance;
	}

	public int getBetDenom() {
		return betDenom;
	}

	public void setBetDenom(int betDenom) {
		this.betDenom = betDenom;
	}


	public int getSpinCount() {
		return spinCount;
	}

	public void setSpinCount(int spinCount) {
		this.spinCount = spinCount;
	}
	
	
	public void adjustBalance(double amount) {
		balance += amount;
	}

}
