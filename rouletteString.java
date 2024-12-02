import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

// Main class for Roulette game
public class rouletteString {
    // Constants for chip value, winning amount, and maximum rounds
    private static final int chipValue = 5;
    private static final int winAmount = 200;
    private static final int maxRounds = 5;

    // Instance variables: the player and number of rounds played
    private RoulettePlayer player;
    private int roundsPlayed;

    // Constructor initializes the player with a starting balance and spin count
    public rouletteString() {
        this.player = new RoulettePlayer(100, 0, maxRounds); 
        this.roundsPlayed = 0; 
    }

    // Main gameplay loop
    public boolean play() {
        Scanner scanner = new Scanner(System.in); 

        // Continue while there are rounds left and the player has money
        while (roundsPlayed < player.getSpinCount() && player.getBalance() > 0) {
            System.out.println("\n--- Round " + (roundsPlayed + 1) + " ---");
            System.out.println("You have $" + player.getBalance() + " left.");
            System.out.println("Enter your bet amount (only multiples of $5):");

            // Validate the bet amount
            String betAmount = scanner.nextLine(); 
            if (Integer.parseInt(betAmount) % chipValue != 0 || Integer.parseInt(betAmount) > player.getBalance() || Integer.parseInt(betAmount) < 0) {
                System.out.println("Invalid bet amount. Please enter a multiple of $5.");
                continue; // Prompt the player again
            }

            // Deduct the bet amount from the player's balance
            player.adjustBalance(-Integer.parseInt(betAmount));
            int numberOfBets = Integer.parseInt(betAmount) / chipValue; 
            player.setBetDenom(Integer.parseInt(betAmount)); 

            // Initialize bet types and individual numbers
            Set<Integer> chosenNumbers = new HashSet<>();
            List<Integer> betTypes = new ArrayList<>();

            System.out.println("\nYou have " + numberOfBets + " bet(s) to place. Choose your bet types:");

            // Loop through the number of bets to collect player choices
            for (int i = 0; i < numberOfBets; i++) {
                System.out.println("Choose your bet type:");
                System.out.println("1. Individual number (1-36)");
                System.out.println("2. 1-18");
                System.out.println("3. 19-36");
                System.out.println("4. Even numbers");
                System.out.println("5. Odd numbers");
                System.out.println("6. Red numbers");
                System.out.println("7. Black numbers");

                String betType = scanner.nextLine(); // Read bet type
                if (Integer.parseInt(betType) == 1) {
                    System.out.println("Enter the individual number you want to bet on (1-36):");
                    String number = scanner.nextLine();
                    if (Integer.parseInt(number) >= 1 && Integer.parseInt(number) <= 36) {
                        chosenNumbers.add(Integer.parseInt(number));
                    } else {
                        System.out.println("Invalid number. Please enter a number between 1 and 36.");
                        i--; 
                        continue;
                    }
                } else if (Integer.parseInt(betType) >= 2 && Integer.parseInt(betType) <= 7) {
                    betTypes.add(Integer.parseInt(betType));
                } else {
                    System.out.println("Invalid bet type.");
                    i--; 
                }
            }

            // Generate a random winning number between 1 and 36
            int winningNumber = new Random().nextInt(36) + 1;
            System.out.println("The winning number is: " + winningNumber);

            boolean won = false;
            int winnings = 0;

            // Check if the player won based on chosen individual numbers
            if (chosenNumbers.contains(winningNumber)) {
                winnings += chipValue * 35; 
                won = true;
            }

            // Check if the player won based on group bets
            for (int betType : betTypes) {
                if (betMatches(winningNumber, betType)) {
                    winnings += chipValue * 2; 
                    won = true;
                }
            }

            // Update the player's balance and show the result
            if (won) {
                player.adjustBalance(winnings);
                System.out.println("Congratulations! You won $" + winnings + ".");
            } else {
                System.out.println("Sorry, you lost this round.");
            }

            roundsPlayed++; 
        }

        // Determine if the player won or lost the game
        if (player.getBalance() >= winAmount) {
            System.out.println("\nCongratulations! You won the game with $" + player.getBalance() + ".");
            scanner.close();
            return true;
        } else {
            System.out.println("\nYou lost the game. You have $" + player.getBalance() + " left.");
            scanner.close();
            return false;
        }
    }

    // Helper method to check if a bet type matches the winning number
    private boolean betMatches(int winningNumber, int betType) {
        switch (betType) {
            case 2: return winningNumber >= 1 && winningNumber <= 18;
            case 3: return winningNumber >= 19 && winningNumber <= 36;
            case 4: return winningNumber % 2 == 0; // Even
            case 5: return winningNumber % 2 != 0; // Odd
            case 6: return isRedNumber(winningNumber); // Red
            case 7: return isBlackNumber(winningNumber); // Black
            default: return false;
        }
    }

    // Helper method to determine if a number is red
    private boolean isRedNumber(int number) {
        int[] redNumbers = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        for (int red : redNumbers) {
            if (number == red) return true;
        }
        return false;
    }

    // Helper method to determine if a number is black
    private boolean isBlackNumber(int number) {
        int[] blackNumbers = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        for (int black : blackNumbers) {
            if (number == black) return true;
        }
        return false;
    }

    // Main method to start the game
    public static void main(String[] args) {
        rouletteString game = new rouletteString(); 
        System.out.println("Welcome to Roulette!\nIn this game, you will have 5 rounds to try and win x amount of money."
        		+ "\nFor each round, input how much you want to bet and look over the menu to choose what you want to bet on."
        		+ "\nGood Luck!");
        game.play(); 
        
    }
}
