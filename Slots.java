import java.util.Random;
import java.util.Scanner;

public class Slots {
	private Scanner in; //scanner for user input
	private Random random; //random object for numbers
	private PlayerSlots player; //player object to manage details
	private Machine machine; //chosen slot machine
	private GameBackend gb; //backend object

	public Slots(GameBackend gb) {
		this.gb = gb;
		in = new Scanner(System.in); //initialize scanner
		random = new Random(); //initialize random
		player = new PlayerSlots(gb.getBalance()); //create player with current backend balance

	}
	
	//entry point to start game
	public boolean startGame() {
		intro();
		chooseMachine();
		setBetDenomination();
		setSpinCount();
		return showResult(playSpins()); //execute spins and display the result
	}
	
	//beginning introductory message
	private void intro() {
		System.out.println("Upon entering the slots room, you see a myriad of different slot machines");
		System.out.println("Two in particular catch your eye, \"Rails to Riches\" and \"Luck of the Irish\"");
	}

	//allow player to choose between machines
	private void chooseMachine() {
		System.out.println("Which would you like to choose? ");

		String machineChoice = in.nextLine().toLowerCase();

		//validate input
		while (!machineChoice.equals("rails to riches") && !machineChoice.equals("luck of the irish")) {
			System.out.println("Incorrect Entry, please enter Rails to Riches or Luck of the Irish");
			machineChoice = in.nextLine().toLowerCase();
		}

		// initialize the chosen machine with its name, jackpot chance, and multiplier
		if (machineChoice.equals("rails to riches")) {
			machine = new Machine("Rails to Riches", 25, 25);
		} else if (machineChoice.equals("luck of the irish")) {
			machine = new Machine("Luck of the Irish", 10, 50);
		}

		System.out.println("You chose " + machine.getName());
	}

	//ask player bet denomination
	private void setBetDenomination() {

		System.out.println("You sit down at the " + machine.getName() + " machine and a prompt shows up on the screen: ");
		System.out.println("Choose Denomination! ($1-10)");
		System.out.println("What would you like to enter?");

		String prompt = "Choose Denomination! ($1-10)\nWhat would you like to enter?";
	    int denom = getValidIntInput(prompt, 1, 10); //helper method
	    player.setBetDenom(denom); //set denomination

	}

	//ask player spin amount
	private void setSpinCount() {
		String prompt = "How many times would you like to spin? (1-10)";
	    int spins = getValidIntInput(prompt, 1, 10); //helper method
	    player.setSpinCount(spins); //set spin count

	}

	//perform spins and return jackpot win or loss
	private boolean playSpins() {
		boolean win = false; //tracks if win or loss

		for (int i = 0; i < player.getSpinCount(); i++) {
			//check if jackpot win based on machine odds
			if (random.nextInt(100) < machine.getJackpotChance()) {
				double winnings = machine.getJackpotMultiplier() * player.getBetDenom(); //calculate winnings
				player.adjustBalance(winnings); //update balance
				System.out.println("Congratulations! You hit the jackpot and won $" + winnings + "!");
				win = true;
				break; //exit if jackpot win
			} else {
				System.out.println("No jackpot this time. Try again!");
			}
		}

		//deduct betting amount if player doesnt win
		if (!win) {
			player.adjustBalance(-player.getBetDenom() * player.getSpinCount());
		}

		return win; //return win
	}

	//display result and update backend player balance
	private boolean showResult(boolean win) {
		if (win) {
			System.out.println("You won the slots room!");
			gb.setBalance(player.getBalance()); //update backend balance
			return true;
		} else {
			System.out.println("You lost the slots room, better luck next time!");
			gb.setBalance(player.getBalance()); //update backend balance
			return false;
		}
	}
	
	//helper method to get valid integer input
	private int getValidIntInput(String prompt, int min, int max) {
	    int input = 0;
	    boolean validInput = false;

	    while (!validInput) {
	        System.out.println(prompt); //display the prompt
	        if (in.hasNextInt()) { //check if the input is an integer
	            input = in.nextInt(); //read the integer
	            if (input >= min && input <= max) { //validate range
	                validInput = true;
	            } else {
	                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
	            }
	        } else {
	            System.out.println("Invalid input. Please enter a valid number.");
	            in.next(); //get rid of invalid
	        }
	    }

	    return input; //return the validated integer
	}

	//testing
	public static void main(String[] args) {
		
	}

}
