import java.util.*;
class PokerTable {
    private int pot;
    private final List<Card> communityCards;
    private final List<Player> players;
    private int currentBet;
    private int dealerPosition;
    private final Deck deck;

    public PokerTable() {
        this.pot = 0;
        this.communityCards = new ArrayList<>();
        this.players = new ArrayList<>();
        this.currentBet = 0;
        this.dealerPosition = 0;
        this.deck = new Deck();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void dealCard() {
        if (communityCards.size() >= 5) {
            throw new IllegalStateException("All community cards have been dealt");
        }
        communityCards.add(deck.drawCard());
    }

    public void dealPlayerCards() {
        deck.shuffle();
        for (int i = 0; i < 2; i++) {  // Deal 2 cards to each player
            for (Player player : players) {
                player.receiveCard(deck.drawCard());
            }
        }
    }

    public void placeBet(Player player, int amount) {
        player.placeBet(amount);
        currentBet = amount;
        pot += amount;
    }

    public HandRank evaluateHand(Player player) {
        return player.checkHand(communityCards);
    }

    public void distributePot() {
        // Find the winning hand
        Player winner = null;
        HandRank bestHand = null;

        for (Player player : players) {
            HandRank currentHand = evaluateHand(player);
            if (bestHand == null || currentHand.compareTo(bestHand) > 0) {
                bestHand = currentHand;
                winner = player;
            }
        }

        if (winner != null) {
            winner.addChips(pot);
            pot = 0;
        }
    }

    public void nextRound() {
        communityCards.clear();
        currentBet = 0;
        dealerPosition = (dealerPosition + 1) % players.size();
        players.get(dealerPosition).setDealer(true);
        deck.reset();
    }

    public int getPot() { return pot; }
    public List<Card> getCommunityCards() { return new ArrayList<>(communityCards); }
    public int getCurrentBet() { return currentBet; }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
}
