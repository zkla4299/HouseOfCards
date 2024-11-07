import java.util.Random;
import java.util.Scanner;

public class Slots {
	private Scanner in;
	private Random random;
	private Player player;
	private Machine machine;

	public Slots() {
		in = new Scanner(System.in);
		random = new Random();
		player = new Player(500);

	}

	public void startGame() {
		intro();
		chooseMachine();
		setBetDenomination();
		setSpinCount();
		playSpins();
		showResult();
	}

	private void intro() {
		// beginning welcome screen
		System.out.println("Upon entering the slots room, you see a myriad of different slot machines");
		System.out.println("Two in particular catch your eye, \"Rails to Riches\" and \"Luck of the Irish\"");
	}

	private void chooseMachine() {
		System.out.println("Which would you like to choose? ");

		String machineChoice = in.nextLine().toLowerCase();

		while (!machineChoice.equals("rails to riches") && !machineChoice.equals("luck of the irish")) {
			System.out.println("Incorrect Entry, please enter Rails to Riches or Luck of the Irish");
			machineChoice = in.nextLine().toLowerCase();
		}

		if (machineChoice.equals("rails to riches")) {
			machine = new Machine("Rails to Riches", 25, 25);
		} else if (machineChoice.equals("luck of the irish")) {
			machine = new Machine("Luck of the Irish", 10, 50);
		}

		System.out.println("You chose " + machine.getName());
	}

	private void setBetDenomination() {

		System.out
				.println("You sit down at the " + machine.getName() + " machine and a prompt shows up on the screen: ");
		System.out.println("Choose Denomination! ($1-10)");
		System.out.println("What would you like to enter?");

		int denom = in.nextInt();

		while (denom > 10 || denom < 0) {
			System.out.println("Incorrect Entry, please enter a number 1-10");
			denom = in.nextInt();
		}

		player.setBetDenom(denom);

	}

	private void setSpinCount() {
		System.out.println("How many times would you like to spin?");

		int spins = in.nextInt();

		while (spins > 10 || spins < 0) {
			System.out.println("Incorrect Entry, please enter a number 1-10");
			spins = in.nextInt();
		}

		player.setSpinCount(spins);

	}

	private void playSpins() {
		boolean win = false;

		for (int i = 0; i < player.getSpinCount(); i++) {
			if (random.nextInt(100) < machine.getJackpotChance()) {
				double winnings = machine.getJackpotMultiplier() * player.getBetDenom();
				player.adjustBalance(winnings);
				System.out.println("Congratulations! You hit the jackpot and won $" + winnings + "!");
				win = true;
				break;
			} else {
				System.out.println("No jackpot this time. Try again!");
			}
		}

		if (!win) {
			player.adjustBalance(-player.getBetDenom() * player.getSpinCount());
		}
	}

	private void showResult() {
		if (player.getBalance() > 500) {
			System.out.println("You won the slots room!");
		} else {
			System.out.println("You lost the slots room, better luck next time!");
		}
	}

	public static void main(String[] args) {
		Slots slotsGame = new Slots();
		slotsGame.startGame();
	}

}
