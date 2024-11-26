import java.util.*;

/**
 * A single-player poker game implementation where one human player plays against a computer opponent.
 */
public class PokerGame {
    private final PokerTable table;
    private final Bank bank;
    private final Scanner scanner;
    private boolean gameRunning;
    private Player humanPlayer;
    private Player computerPlayer;

    public PokerGame() {
        this.table = new PokerTable();
        // Using the same bank values as original implementation
        this.bank = new Bank(10000, 10, 25, 100);
        this.scanner = new Scanner(System.in);
        this.gameRunning = true;
    }

    public void initializePlayers() {
        System.out.println("Enter your starting chips amount:");
        int initialChips = scanner.nextInt();

        // Create human and computer players with proper player numbers
        humanPlayer = new Player(1, initialChips);
        computerPlayer = new Player(2, initialChips);

        // Initial dealer setting
        humanPlayer.setDealer(true);
        computerPlayer.setDealer(false);

        // Add players to table in proper order
        table.addPlayer(humanPlayer);
        table.addPlayer(computerPlayer);
    }

    public void playRound() {
        // Reset table for new round
        table.nextRound();

        // Deal hole cards using table's dealing mechanism
        table.dealPlayerCards();

        // Display initial game state
        System.out.println("\nYour hand: " + humanPlayer.getHand());
        System.out.println("Your chips: " + humanPlayer.getChips());
        System.out.println("Computer's chips: " + computerPlayer.getChips());

        // Conduct betting rounds
        if (!bettingRound("Pre-flop")) return;

        // Deal and conduct flop
        for (int i = 0; i < 3; i++) {
            table.dealCard();
        }
        System.out.println("\nFlop: " + table.getCommunityCards());
        if (!bettingRound("Flop")) return;

        // Deal and conduct turn
        table.dealCard();
        System.out.println("\nTurn: " + table.getCommunityCards());
        if (!bettingRound("Turn")) return;

        // Deal and conduct river
        table.dealCard();
        System.out.println("\nRiver: " + table.getCommunityCards());
        if (!bettingRound("River")) return;

        showdown();
    }

    private boolean bettingRound(String roundName) {
        System.out.println("\n=== " + roundName + " Betting Round ===");

        // Human player's turn if still in hand
        if (!humanPlayer.getHand().isEmpty()) {
            handleHumanTurn();
        }

        // Check if human folded
        if (humanPlayer.getHand().isEmpty()) {
            // Use table's pot distribution mechanism
            computerPlayer.addChips(table.getPot());
            System.out.println("Computer wins " + table.getPot() + " chips!");
            return false;
        }

        // Computer's turn if still in hand
        if (!computerPlayer.getHand().isEmpty()) {
            handleComputerTurn();
        }

        // Check if computer folded
        if (computerPlayer.getHand().isEmpty()) {
            // Use table's pot distribution mechanism
            humanPlayer.addChips(table.getPot());
            System.out.println("You win " + table.getPot() + " chips!");
            return false;
        }

        return true;
    }

    private void handleHumanTurn() {
        System.out.println("\nYour turn");
        System.out.println("Your hand: " + humanPlayer.getHand());
        System.out.println("Your chips: " + humanPlayer.getChips());
        System.out.println("Current bet: " + table.getCurrentBet());
        System.out.println("Pot: " + table.getPot());

        System.out.println("Choose action:");
        System.out.println("1. Call");
        System.out.println("2. Raise");
        System.out.println("3. Fold");

        int choice = scanner.nextInt();
        handlePlayerAction(humanPlayer, choice, true);
    }

    private void handleComputerTurn() {
        System.out.println("\nComputer's turn");

        // Use the table's hand evaluation through Player's checkHand method
        HandRank handRank = computerPlayer.checkHand(table.getCommunityCards());
        int choice;

        // AI decision making based on hand rank
        if (handRank.getRank() >= HandRank.THREE_OF_A_KIND) {
            choice = new Random().nextInt(2) + 1; // 50% call, 50% raise
        } else if (handRank.getRank() >= HandRank.PAIR) {
            choice = new Random().nextBoolean() ? 1 : 3; // 50% call, 50% fold
        } else {
            choice = new Random().nextInt(3) + 1; // Equal chance of all actions
        }

        handlePlayerAction(computerPlayer, choice, false);
    }

    private void handlePlayerAction(Player player, int choice, boolean isHuman) {
        String playerType = isHuman ? "You" : "Computer";

        try {
            switch (choice) {
                case 1 -> { // Call
                    int callAmount = table.getCurrentBet();
                    table.placeBet(player, callAmount);
                    System.out.println(playerType + " call " + callAmount);
                }
                case 2 -> { // Raise
                    int raiseAmount;
                    if (isHuman) {
                        System.out.println("Enter raise amount:");
                        raiseAmount = scanner.nextInt();
                        if (raiseAmount <= table.getCurrentBet()) {
                            System.out.println("Raise must be greater than current bet");
                            handlePlayerAction(player, choice, isHuman);
                            return;
                        }
                    } else {
                        // Computer's raise is double the current bet
                        raiseAmount = table.getCurrentBet() * 2;
                    }
                    table.placeBet(player, raiseAmount);
                    System.out.println(playerType + " raise to " + raiseAmount);
                }
                case 3 -> { // Fold
                    player.fold();
                    System.out.println(playerType + " fold");
                }
                default -> {
                    if (isHuman) {
                        System.out.println("Invalid choice. Please try again.");
                        handlePlayerAction(player, scanner.nextInt(), isHuman);
                    }
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
            if (isHuman) {
                handlePlayerAction(player, scanner.nextInt(), isHuman);
            }
        }
    }

    private void showdown() {
        System.out.println("\n=== Showdown ===");

        // Show hands and rankings using Player's checkHand method
        if (!humanPlayer.getHand().isEmpty()) {
            HandRank humanRank = humanPlayer.checkHand(table.getCommunityCards());
            System.out.println("Your hand: " + humanPlayer.getHand());
            System.out.println("Your rank: " + humanRank.getName());
        }

        if (!computerPlayer.getHand().isEmpty()) {
            HandRank computerRank = computerPlayer.checkHand(table.getCommunityCards());
            System.out.println("Computer's hand: " + computerPlayer.getHand());
            System.out.println("Computer's rank: " + computerRank.getName());
        }

        // Use table's pot distribution mechanism
        table.distributePot();
        System.out.println("Pot has been distributed: " + table.getPot());
    }

    public void play() {
        try {
            System.out.println("Welcome to Single-Player Poker!");
            initializePlayers();

            while (gameRunning && humanPlayer.getChips() > 0 && computerPlayer.getChips() > 0) {
                playRound();

                System.out.println("\nPlay another round? (y/n)");
                String answer = scanner.next();
                if (answer.toLowerCase().startsWith("n")) {
                    gameRunning = false;
                }
            }

            // Game over message
            System.out.println("\nGame Over!");
            System.out.println("Final chips - You: " + humanPlayer.getChips() +
                    ", Computer: " + computerPlayer.getChips());

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public static void main(String[] args) {
        PokerGame game = new PokerGame();
        game.play();
    }
}