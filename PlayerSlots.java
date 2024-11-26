//class representing the player in the slots game, managing their balance, bet denomination, and spin count
public class PlayerSlots {
	private double balance; //current balance
	private int betDenom; //denomination of bet
	private int spinCount; //number of spins
	
	//constructor to initialize with balance
	public PlayerSlots(double initialBalance) {
		this.balance = initialBalance;
	}


	//getter for balance
	public double getBalance() {
		return balance;
	}

	//getter for bet denom
	public int getBetDenom() {
		return betDenom;
	}

	//setter for bet denomination
	public void setBetDenom(int betDenom) {
		this.betDenom = betDenom;
	}


	//getter for spin count
	public int getSpinCount() {
		return spinCount;
	}

	//setter for spin count
	public void setSpinCount(int spinCount) {
		this.spinCount = spinCount;
	}
	
	//adjust balance
	public void adjustBalance(double amount) {
		balance += amount;
	}

}
