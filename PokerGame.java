import java.util.*;
// Example usage with complete game loop
public class PokerGame {
    private final PokerTable table;
    private final Bank bank;
    private final Scanner scanner;
    private boolean gameRunning;

    public PokerGame() {
        this.table = new PokerTable();
        this.bank = new Bank(10000, 10, 25, 100);
        this.scanner = new Scanner(System.in);
        this.gameRunning = true;
    }

    public void initializePlayers() {
        System.out.println("Enter number of players (2-8):");
        int numPlayers = scanner.nextInt();

        if (numPlayers < 2 || numPlayers > 8) {
            throw new IllegalArgumentException("Invalid number of players. Must be between 2 and 8.");
        }

        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("Enter initial chips for Player " + i + ":");
            int initialChips = scanner.nextInt();
            Player player = new Player(i, initialChips);
            table.addPlayer(player);
        }
    }

    public void playRound() {
        // Reset for new round
        table.nextRound();

        // Deal hole cards
        table.dealPlayerCards();

        // Pre-flop betting round
        bettingRound("Pre-flop");

        // Flop
        for (int i = 0; i < 3; i++) {
            table.dealCard();
        }
        System.out.println("Flop: " + table.getCommunityCards());
        bettingRound("Flop");

        // Turn
        table.dealCard();
        System.out.println("Turn: " + table.getCommunityCards());
        bettingRound("Turn");

        // River
        table.dealCard();
        System.out.println("River: " + table.getCommunityCards());
        bettingRound("River");

        // Showdown
        showdown();
    }

    private void bettingRound(String roundName) {
        System.out.println("\n=== " + roundName + " Betting Round ===");
        List<Player> players = table.getPlayers();

        for (Player player : players) {
            // Skip players who have folded
            if (player.getHand().isEmpty()) {
                continue;
            }

            System.out.println("\nPlayer " + player.displayPlayerNumber() + "'s turn");
            System.out.println("Your hand: " + player.getHand());
            System.out.println("Your chips: " + player.getChips());
            System.out.println("Current bet: " + table.getCurrentBet());
            System.out.println("Pot: " + table.getPot());

            System.out.println("Choose action:");
            System.out.println("1. Call");
            System.out.println("2. Raise");
            System.out.println("3. Fold");

            int choice = scanner.nextInt();
            handlePlayerAction(player, choice);
        }
    }

    private void handlePlayerAction(Player player, int choice) {
        switch (choice) {
            case 1 -> { // Call
                int callAmount = table.getCurrentBet();
                table.placeBet(player, callAmount);
                System.out.println("Player " + player.displayPlayerNumber() + " calls " + callAmount);
            }
            case 2 -> { // Raise
                System.out.println("Enter raise amount:");
                int raiseAmount = scanner.nextInt();
                if (raiseAmount <= table.getCurrentBet()) {
                    System.out.println("Raise must be greater than current bet");
                    handlePlayerAction(player, choice);
                    return;
                }
                table.placeBet(player, raiseAmount);
                System.out.println("Player " + player.displayPlayerNumber() + " raises to " + raiseAmount);
            }
            case 3 -> { // Fold
                player.fold();
                System.out.println("Player " + player.displayPlayerNumber() + " folds");
            }
            default -> {
                System.out.println("Invalid choice. Please try again.");
                handlePlayerAction(player, scanner.nextInt());
            }
        }
    }

    private void showdown() {
        System.out.println("\n=== Showdown ===");
        List<Player> players = table.getPlayers();

        // Show all hands of players who haven't folded
        for (Player player : players) {
            if (!player.getHand().isEmpty()) {
                HandRank handRank = table.evaluateHand(player);
                System.out.println("Player " + player.displayPlayerNumber() + ":");
                System.out.println("Hand: " + player.getHand());
                System.out.println("Rank: " + handRank.getName());
                System.out.println();
            }
        }

        // Distribute pot
        table.distributePot();
    }

    public void play() {
        try {
            System.out.println("Welcome to Poker!");
            initializePlayers();

            while (gameRunning) {
                playRound();

                System.out.println("\nPlay another round? (y/n)");
                String answer = scanner.next();
                if (answer.toLowerCase().startsWith("n")) {
                    gameRunning = false;
                }
            }

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void main(String[] args) {
        PokerGame game = new PokerGame();
        game.play();
    }
}