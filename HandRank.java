import java.util.*;
class HandRank implements Comparable<HandRank> {
    private final String name;
    private final int rank;
    private final List<Card> relevantCards;  // Cards that make up the hand

    public static final int HIGH_CARD = 1;
    public static final int PAIR = 2;
    public static final int TWO_PAIR = 3;
    public static final int THREE_OF_A_KIND = 4;
    public static final int STRAIGHT = 5;
    public static final int FLUSH = 6;
    public static final int FULL_HOUSE = 7;
    public static final int FOUR_OF_A_KIND = 8;
    public static final int STRAIGHT_FLUSH = 9;
    public static final int ROYAL_FLUSH = 10;

    public HandRank(int rank, List<Card> relevantCards) {
        this.rank = rank;
        this.relevantCards = new ArrayList<>(relevantCards);
        this.name = convertRankToName(rank);
    }

    private String convertRankToName(int rank) {
        return switch (rank) {
            case 1 -> "High Card";
            case 2 -> "Pair";
            case 3 -> "Two Pair";
            case 4 -> "Three of a Kind";
            case 5 -> "Straight";
            case 6 -> "Flush";
            case 7 -> "Full House";
            case 8 -> "Four of a Kind";
            case 9 -> "Straight Flush";
            case 10 -> "Royal Flush";
            default -> throw new IllegalArgumentException("Invalid rank: " + rank);
        };
    }

    @Override
    public int compareTo(HandRank other) {
        if (this.rank != other.rank) {
            return Integer.compare(this.rank, other.rank);
        }
        // If ranks are equal, compare high cards
        for (int i = 0; i < Math.min(relevantCards.size(), other.relevantCards.size()); i++) {
            int comparison = Integer.compare(
                    relevantCards.get(i).getValue(),
                    other.relevantCards.get(i).getValue()
            );
            if (comparison != 0) return comparison;
        }
        return 0;
    }

    public String getName() { return name; }
    public int getRank() { return rank; }
    public List<Card> getRelevantCards() { return new ArrayList<>(relevantCards); }
}