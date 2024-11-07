import java.util.*;
class HandEvaluator {
    public static HandRank evaluateHand(List<Card> cards) {
        if (cards.size() < 5) {
            throw new IllegalArgumentException("Need at least 5 cards to evaluate a hand");
        }

        // Check for each hand type from highest to lowest
        List<Card> relevantCards;

        // Check for Royal Flush
        if ((relevantCards = findRoyalFlush(cards)) != null) {
            return new HandRank(HandRank.ROYAL_FLUSH, relevantCards);
        }

        // Check for Straight Flush
        if ((relevantCards = findStraightFlush(cards)) != null) {
            return new HandRank(HandRank.STRAIGHT_FLUSH, relevantCards);
        }

        // Check for Four of a Kind
        if ((relevantCards = findFourOfAKind(cards)) != null) {
            return new HandRank(HandRank.FOUR_OF_A_KIND, relevantCards);
        }

        // Continue with other hand checks...
        // For brevity, we'll just return High Card if no other hand is found
        relevantCards = new ArrayList<>(cards);
        relevantCards.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return new HandRank(HandRank.HIGH_CARD, relevantCards.subList(0, 5));
    }

    // Helper methods for finding specific hands
    private static List<Card> findRoyalFlush(List<Card> cards) {
        List<Card> straightFlush = findStraightFlush(cards);
        if (straightFlush != null && straightFlush.get(0).getValue() == 14) {
            return straightFlush;
        }
        return null;
    }

    private static List<Card> findStraightFlush(List<Card> cards) {
        // Group cards by suit
        Map<String, List<Card>> suits = new HashMap<>();
        for (Card card : cards) {
            suits.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        // Check each suit for a straight
        for (List<Card> suitedCards : suits.values()) {
            if (suitedCards.size() >= 5) {
                List<Card> straight = findStraight(suitedCards);
                if (straight != null) {
                    return straight;
                }
            }
        }
        return null;
    }

    private static List<Card> findFourOfAKind(List<Card> cards) {
        // Group cards by rank
        Map<Integer, List<Card>> ranks = new HashMap<>();
        for (Card card : cards) {
            ranks.computeIfAbsent(card.getValue(), k -> new ArrayList<>()).add(card);
        }

        // Look for four of a kind
        for (List<Card> rankCards : ranks.values()) {
            if (rankCards.size() == 4) {
                List<Card> result = new ArrayList<>(rankCards);
                // Add highest kicker
                cards.stream()
                        .filter(c -> c.getValue() != rankCards.get(0).getValue())
                        .max(Comparator.comparingInt(Card::getValue))
                        .ifPresent(result::add);
                return result;
            }
        }
        return null;
    }

    private static List<Card> findStraight(List<Card> cards) {
        // Sort cards by value
        List<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        // Look for 5 consecutive cards
        for (int i = 0; i <= sortedCards.size() - 5; i++) {
            if (isStraight(sortedCards.subList(i, i + 5))) {
                return sortedCards.subList(i, i + 5);
            }
        }
        return null;
    }

    private static boolean isStraight(List<Card> cards) {
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getValue() != cards.get(i + 1).getValue() + 1) {
                return false;
            }
        }
        return true;
    }
}