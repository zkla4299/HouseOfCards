import java.util.*;

public class PokerGame {
    private final PokerTable table;
    private final Scanner scanner;
    private boolean gameRunning;
    private Player humanPlayer;
    private Player computerPlayer;
    private final int BID_AMOUNT_1 = 10;
    private final int BID_AMOUNT_2 = 25;
    private final int BID_AMOUNT_3 = 50;
    private int roundsPlayed;
    private final int ROUND_LIMIT = 5;

    public PokerGame() {
        this.table = new PokerTable();
        this.scanner = new Scanner(System.in);
        this.gameRunning = true;
        this.roundsPlayed = 0;
    }

    public void initializePlayers(double balance) {
        //how to play
        System.out.println("You will be given a hand with two cards at first, and you have the option to call, raise, and fold. This is called the pre-flop betting round. \n" +
                "\n" +
                "Then you will have another chance to call, raise, and fold once you see three more cards on the table. This is called the Flop round. \n" +
                "\n" +
                "Then another card gets added to the flop round which changes it to the turn betting round. You will have another chance to call, raise, and fold. \n" +
                "\n" +
                "Then, you will see all 5 cards on the table which is called the River Betting Round. You will have one more option to call, raise and fold. \n" +
                "\n" +
                "Last but not least you will have a Showdown which will show your hand, and your rank, and the computers hand, and computer's rank. \n");

        humanPlayer = new Player(1, (int)balance);
        computerPlayer = new Player(2, (int)balance);

        table.addPlayer(humanPlayer);
        table.addPlayer(computerPlayer);

        humanPlayer.setDealer(true);
        computerPlayer.setDealer(false);
    }

    public void playRound() {
        table.nextRound();
        table.dealPlayerCards();

        System.out.println("\nYour hand: " + humanPlayer.getHand());
        System.out.println("Your balance: $" + humanPlayer.getChips());
        System.out.println("Computer's balance: $" + computerPlayer.getChips());

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
            System.out.println("Computer wins $" + table.getPot() + "!");
            return false;
        }

        if (!computerPlayer.getHand().isEmpty()) {
            handleComputerTurn(roundName);
        }

        if (computerPlayer.getHand().isEmpty()) {
            humanPlayer.addChips(table.getPot());
            System.out.println("You win $" + table.getPot() + "!");
            return false;
        }

        return true;
    }

    private int getBetAmount() {
        while (true) {
            System.out.println("Choose your bet:");
            System.out.println("a.) $" + BID_AMOUNT_1);
            System.out.println("b.) $" + BID_AMOUNT_2);
            System.out.println("c.) $" + BID_AMOUNT_3);

            String input = scanner.nextLine().toLowerCase();
            int betAmount = switch (input) {
                case "a" -> BID_AMOUNT_1;
                case "b" -> BID_AMOUNT_2;
                case "c" -> BID_AMOUNT_3;
                default -> -1;
            };

            if (betAmount != -1 && humanPlayer.getChips() >= betAmount) {
                return betAmount;
            }
            System.out.println("Invalid choice or insufficient funds. Please try again.");
        }
    }

    private void handleHumanTurn() {
        System.out.println("\nYour turn");
        System.out.println("Your hand: " + humanPlayer.getHand());
        System.out.println("Your balance: $" + humanPlayer.getChips());
        System.out.println("Current bet: $" + table.getCurrentBet());
        System.out.println("Pot: $" + table.getPot());

        System.out.println("Choose action:");
        System.out.println("1. Call");
        System.out.println("2. Raise");
        System.out.println("3. Fold");

        String choice = scanner.nextLine();
        try {
            handlePlayerAction(humanPlayer, Integer.parseInt(choice), true);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter 1, 2, or 3.");
            handleHumanTurn();
        }
    }

    private void handleComputerTurn(String roundName) {
        System.out.println("\nComputer's turn");

        int choice;
        if (roundName.equals("Pre-flop")) {
            choice = preFlopDecision(computerPlayer.getHand());
        } else {
            List<Card> availableCards = new ArrayList<>(computerPlayer.getHand());
            availableCards.addAll(table.getCommunityCards());

            if (availableCards.size() < 5) {
                choice = new Random().nextInt(2) + 1;
            } else {
                try {
                    List<Card> allCards = new ArrayList<>(computerPlayer.getHand());
                    allCards.addAll(table.getCommunityCards());
                    HandRank handRank = HandEvaluator.evaluateHand(allCards);

                    if (handRank.getRank() >= HandRank.THREE_OF_A_KIND) {
                        choice = new Random().nextInt(2) + 1;
                    } else if (handRank.getRank() >= HandRank.PAIR) {
                        choice = new Random().nextBoolean() ? 1 : 3;
                    } else {
                        choice = new Random().nextInt(3) + 1;
                    }
                } catch (Exception e) {
                    choice = 1;
                }
            }
        }

        handlePlayerAction(computerPlayer, choice, false);
    }

    private void handlePlayerAction(Player player, int choice, boolean isHuman) {
        String playerType = isHuman ? "You" : "Computer";

        try {
            switch (choice) {
                case 1 -> { // Call
                    int betAmount;
                    if (isHuman) {
                        betAmount = getBetAmount();
                    } else {
                        // Computer randomly chooses between the three bet amounts
                        int[] betOptions = {BID_AMOUNT_1, BID_AMOUNT_2, BID_AMOUNT_3};
                        betAmount = betOptions[new Random().nextInt(betOptions.length)];
                    }
                    table.placeBet(player, betAmount);
                    System.out.println(playerType + " call $" + betAmount);
                }
                case 2 -> { // Raise
                    int raiseAmount;
                    if (isHuman) {
                        raiseAmount = getBetAmount();
                    } else {
                        // Computer will probs go to choose higher amounts when raising
                        int[] raiseOptions = {BID_AMOUNT_2, BID_AMOUNT_3};
                        raiseAmount = raiseOptions[new Random().nextInt(raiseOptions.length)];
                    }
                    table.placeBet(player, raiseAmount);
                    System.out.println(playerType + " raise to $" + raiseAmount);
                }
                case 3 -> { // Fold
                    player.fold();
                    System.out.println(playerType + " fold");
                }
                default -> {
                    if (isHuman) {
                        System.out.println("Invalid choice. Please try again.");
                        handleHumanTurn();
                    }
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
            if (isHuman) {
                handleHumanTurn();
            }
        }
    }

    private int preFlopDecision(List<Card> holeCards) {
        if (holeCards.size() != 2) {
            return 1;
        }

        List<Card> sortedHoleCards = new ArrayList<>(holeCards);
        sortedHoleCards.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        int highCard = sortedHoleCards.get(0).getValue();
        int lowCard = sortedHoleCards.get(1).getValue();
        boolean isPair = highCard == lowCard;
        boolean isSuited = sortedHoleCards.get(0).getSuit().equals(sortedHoleCards.get(1).getSuit());

        if (isPair && highCard >= 10) {
            return new Random().nextInt(2) + 1;
        } else if (highCard >= 13 && lowCard >= 10) {
            return new Random().nextInt(2) + 1;
        } else if (isPair) {
            return 1;
        } else if (highCard >= 12 && lowCard >= 9 && isSuited) {
            return 1;
        } else if (highCard >= 14 && lowCard >= 10) {
            return 1;
        } else if (new Random().nextDouble() < 0.3) {
            return new Random().nextInt(2) + 1;
        }

        return 3;
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

    public boolean play(GameBackend GB) {
        try {
            double initialBalance = GB.getBalance();
            System.out.println("Starting balance: $" + initialBalance);
            initializePlayers(initialBalance);
            double targetBalance = initialBalance + 50;

            while (gameRunning && roundsPlayed < ROUND_LIMIT &&
                    humanPlayer.getChips() > 0 && humanPlayer.getChips() < targetBalance) {

                playRound();
                roundsPlayed++;

                System.out.println("\nCurrent balance: $" + humanPlayer.getChips());
                System.out.println("Rounds played: " + roundsPlayed + "/" + ROUND_LIMIT);

                if (humanPlayer.getChips() >= targetBalance) {
                    System.out.println("Congratulations! You've won enough to proceed!");
                    GB.setBalance(humanPlayer.getChips());
                    return true;
                }

                if (roundsPlayed < ROUND_LIMIT && humanPlayer.getChips() > 0) {
                    System.out.println("\nContinue playing? (y/n)");
                    String answer = scanner.nextLine();
                    if (answer.toLowerCase().startsWith("n")) {
                        break;
                    }
                }
            }

            GB.setBalance(humanPlayer.getChips());

            if (humanPlayer.getChips() <= 0) {
                System.out.println("You've lost all your money!");
            } else if (roundsPlayed >= ROUND_LIMIT) {
                System.out.println("Maximum rounds reached.");
            }

            return humanPlayer.getChips() >= targetBalance;

        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        PokerGame game = new PokerGame();
        //game.play();
    }
}
