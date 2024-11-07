import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Roulette {
    private static final int startMoney = 100;
    private static final int chipValue = 5;
    private static final int winAmount = 200;
    private static final int maxRounds = 5;

    private int money;
    private int roundsPlayed;

    public Roulette() {
        this.money = startMoney;
        this.roundsPlayed = 0;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (roundsPlayed < maxRounds && money > 0) {
            System.out.println("\n--- Round " + (roundsPlayed + 1) + " ---");
            System.out.println("You have $" + money + " left.");
            System.out.println("Enter your bet amount (only multiples of $5):");

            int betAmount = scanner.nextInt();
            if (betAmount % chipValue != 0 || betAmount > money) {
                System.out.println("Invalid bet amount. Please enter a multiple of $5.");
                continue;
            }

            money -= betAmount;
            int numberOfBets = betAmount / chipValue;
            Set<Integer> chosenNumbers = new HashSet<>();
            List<Integer> betTypes = new ArrayList<>();

            System.out.println("\nYou have " + numberOfBets + " bet(s) to place. Choose your bet types:");

            for (int i = 0; i < numberOfBets; i++) {
                System.out.println("Choose your bet type:");
                System.out.println("1. Individual number (1-36)");
                System.out.println("2. 1-18");
                System.out.println("3. 19-36");
                System.out.println("4. Even numbers");
                System.out.println("5. Odd numbers");
                System.out.println("6. Red numbers");
                System.out.println("7. Black numbers");

                int betType = scanner.nextInt();
                if (betType == 1) {
                    System.out.println("Enter the individual number you want to bet on (1-36):");
                    int number = scanner.nextInt();
                    if (number >= 1 && number <= 36) {
                        chosenNumbers.add(number);
                    } else {
                        System.out.println("Invalid number. Please enter a number between 1 and 36.");
                        i--; 
                        continue;
                    }
                } else if (betType >= 2 && betType <= 7) {
                    betTypes.add(betType);
                } else {
                    System.out.println("Invalid bet type.");
                    i--; 
                }
            }

            int winningNumber = new Random().nextInt(36) + 1;
            System.out.println("The winning number is: " + winningNumber);

            boolean won = false;
            int winnings = 0;

            if (chosenNumbers.contains(winningNumber)) {
                winnings += chipValue * 35; 
                won = true;
            }
            for (int betType : betTypes) {
                if (betMatches(winningNumber, betType)) {
                    winnings += chipValue * 2;
                    won = true;
                }
            }

            if (won) {
                money += winnings;
                System.out.println("Congratulations! You won $" + winnings + ".");
            } else {
                System.out.println("Sorry, you lost this round.");
            }

            roundsPlayed++;

            if (roundsPlayed < maxRounds) {
                System.out.println("Do you want to play again? (yes or no)");
                String playAgain = scanner.next();
                if (!playAgain.equalsIgnoreCase("yes")) {
                    break;
                }
            }
        }

        if (money >= winAmount) {
            System.out.println("\nCongratulations! You won the game with $" + money + ".");
        } else {
            System.out.println("\nYou lost the game. You have $" + money + " left.");
        }

        scanner.close();
    }

    private boolean betMatches(int winningNumber, int betType) {
        switch (betType) {
            case 2: return winningNumber >= 1 && winningNumber <= 18; // 1-18
            case 3: return winningNumber >= 19 && winningNumber <= 36; // 19-36
            case 4: return winningNumber % 2 == 0; // Even numbers
            case 5: return winningNumber % 2 != 0; // Odd numbers
            case 6: return isRedNumber(winningNumber); // Red numbers
            case 7: return isBlackNumber(winningNumber); // Black numbers
            default: return false;
        }
    }

    private boolean isRedNumber(int number) {
        int[] redNumbers = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        for (int red : redNumbers) {
            if (number == red) return true;
        }
        return false;
    }

    private boolean isBlackNumber(int number) {
        int[] blackNumbers = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        for (int black : blackNumbers) {
            if (number == black) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Roulette game = new Roulette();
        game.play();
    }
}
