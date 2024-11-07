import java.util.*;

class Card {
    private final String suit;
    private final String rank;
    private final int value;  // Numeric value for comparison

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = convertRankToValue(rank);
    }

    private int convertRankToValue(String rank) {
        return switch (rank.toUpperCase()) {
            case "2" -> 2;
            case "3" -> 3;
            case "4" -> 4;
            case "5" -> 5;
            case "6" -> 6;
            case "7" -> 7;
            case "8" -> 8;
            case "9" -> 9;
            case "10" -> 10;

            // get represented by numbers
            case "JACK" -> 11;
            case "QUEEN" -> 12;
            case "KING" -> 13;
            case "ACE" -> 14;

            // anything higher than 14
            default -> throw new IllegalArgumentException("Invalid card rank: " + rank);
        };
    }

    public String getSuit() { return suit; }
    public String getRank() { return rank; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}