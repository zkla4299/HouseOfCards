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
        this.bank = new Bank(10000, 10, 25, 100);
        this.scanner = new Scanner(System.in);
        this.gameRunning = true;
    }

    public void initializePlayers() {
        System.out.println("You will be given a hand with two cards at first, and you have the option to call, raise, and fold. This is called the pre-flop betting round. \n" +
                "\n" +
                "Then you will have another chance to call, raise, and fold once you see three more cards on the table. This is called the Flop round. \n" +
                "\n" +
                "Then another card gets added to the flop round which changes it to the turn betting round. You will have another chance to call, raise, and fold. \n" +
                "\n" +
                "Then, you will see all 5 cards on the table which is called the River Betting Round. You will have one more option to call, raise and fold. \n" +
                "\n" +
                "Last but not least you will have a Showdown which will show your hand, and your rank, and the computers hand, and computer’s rank. \n");
        System.out.println("Enter your starting chips amount:");
        int initialChips = scanner.nextInt();

        humanPlayer = new Player(1, initialChips);
        computerPlayer = new Player(2, initialChips);

        humanPlayer.setDealer(true);
        computerPlayer.setDealer(false);

        table.addPlayer(humanPlayer);
        table.addPlayer(computerPlayer);
    }

    public void playRound() {
        table.nextRound();
        table.dealPlayerCards();

        System.out.println("\nYour hand: " + humanPlayer.getHand());
        System.out.println("Your chips: " + humanPlayer.getChips());
        System.out.println("Computer's chips: " + computerPlayer.getChips());

        // Pre-flop
        if (!bettingRound("Pre-flop")) return;

        // Flop
        for (int i = 0; i < 3; i++) {
            table.dealCard();
        }
        System.out.println("\nFlop: " + table.getCommunityCards());
        if (!bettingRound("Flop")) return;

        // Turn
        table.dealCard();
        System.out.println("\nTurn: " + table.getCommunityCards());
        if (!bettingRound("Turn")) return;

        // River
        table.dealCard();
        System.out.println("\nRiver: " + table.getCommunityCards());
        if (!bettingRound("River")) return;

        showdown();
    }

    private boolean bettingRound(String roundName) {
        System.out.println("\n=== " + roundName + " Betting Round ===");

        if (!humanPlayer.getHand().isEmpty()) {
            handleHumanTurn();
        }

        if (humanPlayer.getHand().isEmpty()) {
            computerPlayer.addChips(table.getPot());
            System.out.println("Computer wins " + table.getPot() + " chips!");
            return false;
        }

        if (!computerPlayer.getHand().isEmpty()) {
            handleComputerTurn(roundName);
        }

        if (computerPlayer.getHand().isEmpty()) {
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

    private void handleComputerTurn(String roundName) {
        System.out.println("\nComputer's turn");

        int choice;
        if (roundName.equals("Pre-flop")) {
            choice = preFlopDecision(computerPlayer.getHand());
        } else {
            // Get available cards for post-flop decision
            List<Card> availableCards = new ArrayList<>(computerPlayer.getHand());
            availableCards.addAll(table.getCommunityCards());

            if (availableCards.size() < 5) {
                // Shouldn't happen, but just in case
                choice = new Random().nextInt(2) + 1; // Call or raise
            } else {
                try {
                    // Evaluate current hand strength
                    List<Card> allCards = new ArrayList<>(computerPlayer.getHand());
                    allCards.addAll(table.getCommunityCards());

                    HandRank handRank = HandEvaluator.evaluateHand(allCards);

                    if (handRank.getRank() >= HandRank.THREE_OF_A_KIND) {
                        choice = new Random().nextInt(2) + 1; // 50% call, 50% raise
                    } else if (handRank.getRank() >= HandRank.PAIR) {
                        choice = new Random().nextBoolean() ? 1 : 3; // 50% call, 50% fold
                    } else {
                        choice = new Random().nextInt(3) + 1; // Equal chance of all actions
                    }
                } catch (Exception e) {
                    // If hand evaluation fails, make a conservative choice
                    choice = 1; // Call
                }
            }
        }

        handlePlayerAction(computerPlayer, choice, false);
    }

    private int preFlopDecision(List<Card> holeCards) {
        if (holeCards.size() != 2) {
            return 1; // Default to call if something is wrong
        }

        // Sort hole cards by value
        List<Card> sortedHoleCards = new ArrayList<>(holeCards);
        sortedHoleCards.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        int highCard = sortedHoleCards.get(0).getValue();
        int lowCard = sortedHoleCards.get(1).getValue();
        boolean isPair = highCard == lowCard;
        boolean isSuited = sortedHoleCards.get(0).getSuit().equals(sortedHoleCards.get(1).getSuit());

        // Strong starting hands
        if (isPair && highCard >= 10) {
            return new Random().nextInt(2) + 1; // 50% call, 50% raise
        } else if (highCard >= 13 && lowCard >= 10) {
            return new Random().nextInt(2) + 1; // 50% call, 50% raise
        } else if (isPair) {
            return 1; // Call
        } else if (highCard >= 12 && lowCard >= 9 && isSuited) {
            return 1; // Call
        } else if (highCard >= 14 && lowCard >= 10) {
            return 1; // Call
        } else if (new Random().nextDouble() < 0.3) {
            return new Random().nextInt(2) + 1; // Bluff occasionally
        }

        return 3; // Fold weak hands
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
                System.out.println("Please try again:");
                handlePlayerAction(player, scanner.nextInt(), isHuman);
            }
        }
    }

    private void showdown() {
        System.out.println("\n=== Showdown ===");

        if (!humanPlayer.getHand().isEmpty()) {
            try {
                List<Card> allCards = new ArrayList<>(humanPlayer.getHand());
                allCards.addAll(table.getCommunityCards());
                HandRank humanRank = HandEvaluator.evaluateHand(allCards);
                System.out.println("Your hand: " + humanPlayer.getHand());
                System.out.println("Your rank: " + humanRank.getName());
            } catch (Exception e) {
                System.out.println("Error evaluating your hand");
            }
        }

        if (!computerPlayer.getHand().isEmpty()) {
            try {
                List<Card> allCards = new ArrayList<>(computerPlayer.getHand());
                allCards.addAll(table.getCommunityCards());
                HandRank computerRank = HandEvaluator.evaluateHand(allCards);
                System.out.println("Computer's hand: " + computerPlayer.getHand());
                System.out.println("Computer's rank: " + computerRank.getName());
            } catch (Exception e) {
                System.out.println("Error evaluating computer's hand");
            }
        }

        table.distributePot();
    }

    public boolean play() {
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

            System.out.println("\nGame Over!");
            System.out.println("Final chips - You: " + humanPlayer.getChips() +
                    ", Computer: " + computerPlayer.getChips());

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        return (humanPlayer.getChips() > computerPlayer.getChips());
    }

    public static void main(String[] args) {
        PokerGame game = new PokerGame();
        game.play();
    }
}