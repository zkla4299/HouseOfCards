import java.util.*;
class Player {
    private final int playerNumber;
    private int chips;
    private final List<Card> hand;
    private boolean isDealer;

    public Player(int playerNumber, int initialChips) {
        this.playerNumber = playerNumber;
        this.chips = initialChips;
        this.hand = new ArrayList<>();
        this.isDealer = false;
    }

    public int displayPlayerNumber() {
        return playerNumber;
    }

    public void placeBet(int amount) {
        if (amount > chips) {
            throw new IllegalStateException("Insufficient chips: " + chips + " < " + amount);
        }
        chips -= amount;
    }

    public void fold() {
        hand.clear();
    }

    public HandRank checkHand(List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(hand);
        allCards.addAll(communityCards);
        return HandEvaluator.evaluateHand(allCards);
    }

    public void receiveCard(Card card) {
        if (hand.size() >= 2) {
            throw new IllegalStateException("Player already has maximum number of cards");
        }
        hand.add(card);
    }

    public List<Card> getHand() { return new ArrayList<>(hand); }
    public int getChips() { return chips; }
    public void addChips(int amount) { chips += amount; }
    public void setDealer(boolean dealer) { isDealer = dealer; }
    public boolean isDealer() { return isDealer; }
}