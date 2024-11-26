import java.util.*;

class HandEvaluator {
    public static HandRank evaluateHand(List<Card> cards) {
        if (cards.size() < 2) { // Changed to allow 2 hole cards + community cards
            throw new IllegalArgumentException("Need at least 2 cards to evaluate a hand");
        }

        // If we have more than 5 cards (hole cards + community), find best 5-card combination
        if (cards.size() > 5) {
            return findBestHand(cards);
        }

        // For exactly 5 cards, use original evaluation logic
        return evaluateFiveCards(cards);
    }

    private static HandRank findBestHand(List<Card> cards) {
        List<Card> cardsList = new ArrayList<>(cards);
        HandRank bestHand = null;

        // Generate all possible 5-card combinations and find the best one
        for (int i = 0; i < cardsList.size() - 4; i++) {
            for (int j = i + 1; j < cardsList.size() - 3; j++) {
                for (int k = j + 1; k < cardsList.size() - 2; k++) {
                    for (int l = k + 1; l < cardsList.size() - 1; l++) {
                        for (int m = l + 1; m < cardsList.size(); m++) {
                            List<Card> fiveCards = Arrays.asList(
                                    cardsList.get(i),
                                    cardsList.get(j),
                                    cardsList.get(k),
                                    cardsList.get(l),
                                    cardsList.get(m)
                            );
                            HandRank currentHand = evaluateFiveCards(fiveCards);
                            if (bestHand == null || currentHand.compareTo(bestHand) > 0) {
                                bestHand = currentHand;
                            }
                        }
                    }
                }
            }
        }
        return bestHand;
    }

    private static HandRank evaluateFiveCards(List<Card> cards) {
        if (cards.size() != 5) {
            throw new IllegalArgumentException("Need exactly 5 cards to evaluate");
        }

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

        // Check for Full House
        if ((relevantCards = findFullHouse(cards)) != null) {
            return new HandRank(HandRank.FULL_HOUSE, relevantCards);
        }

        // Check for Flush
        if ((relevantCards = findFlush(cards)) != null) {
            return new HandRank(HandRank.FLUSH, relevantCards);
        }

        // Check for Straight
        if ((relevantCards = findStraight(cards)) != null) {
            return new HandRank(HandRank.STRAIGHT, relevantCards);
        }

        // Check for Three of a Kind
        if ((relevantCards = findThreeOfAKind(cards)) != null) {
            return new HandRank(HandRank.THREE_OF_A_KIND, relevantCards);
        }

        // Check for Two Pair
        if ((relevantCards = findTwoPair(cards)) != null) {
            return new HandRank(HandRank.TWO_PAIR, relevantCards);
        }

        // Check for One Pair
        if ((relevantCards = findOnePair(cards)) != null) {
            return new HandRank(HandRank.PAIR, relevantCards);
        }

        // High Card
        relevantCards = new ArrayList<>(cards);
        relevantCards.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return new HandRank(HandRank.HIGH_CARD, relevantCards);
    }

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
        Map<Integer, List<Card>> valueGroups = groupByValue(cards);

        for (List<Card> group : valueGroups.values()) {
            if (group.size() == 4) {
                List<Card> result = new ArrayList<>(group);
                // Add highest kicker
                for (Card card : cards) {
                    if (card.getValue() != group.get(0).getValue()) {
                        result.add(card);
                        break;
                    }
                }
                return result;
            }
        }
        return null;
    }

    private static List<Card> findFullHouse(List<Card> cards) {
        Map<Integer, List<Card>> valueGroups = groupByValue(cards);
        List<Card> threeOfKind = null;
        List<Card> pair = null;

        for (List<Card> group : valueGroups.values()) {
            if (group.size() == 3 && threeOfKind == null) {
                threeOfKind = group;
            } else if (group.size() >= 2 && pair == null) {
                pair = group.subList(0, 2);
            }
        }

        if (threeOfKind != null && pair != null) {
            List<Card> result = new ArrayList<>(threeOfKind);
            result.addAll(pair);
            return result;
        }
        return null;
    }

    private static List<Card> findFlush(List<Card> cards) {
        Map<String, List<Card>> suits = new HashMap<>();
        for (Card card : cards) {
            suits.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        for (List<Card> suitedCards : suits.values()) {
            if (suitedCards.size() >= 5) {
                suitedCards.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
                return suitedCards.subList(0, 5);
            }
        }
        return null;
    }

    private static List<Card> findStraight(List<Card> cards) {
        List<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        // Remove duplicates by value
        List<Card> uniqueCards = new ArrayList<>();
        int lastValue = -1;
        for (Card card : sortedCards) {
            if (card.getValue() != lastValue) {
                uniqueCards.add(card);
                lastValue = card.getValue();
            }
        }

        // Check for Ace-low straight
        if (uniqueCards.size() >= 5 &&
                uniqueCards.get(0).getValue() == 14 &&
                uniqueCards.get(uniqueCards.size() - 1).getValue() == 2) {
            List<Card> aceLowStraight = new ArrayList<>();
            Card ace = uniqueCards.get(0);
            for (int i = uniqueCards.size() - 4; i < uniqueCards.size(); i++) {
                if (i >= 0) aceLowStraight.add(uniqueCards.get(i));
            }
            if (aceLowStraight.size() == 4) {
                aceLowStraight.add(ace);
                return aceLowStraight;
            }
        }

        // Check for regular straight
        for (int i = 0; i <= uniqueCards.size() - 5; i++) {
            if (uniqueCards.get(i).getValue() == uniqueCards.get(i + 4).getValue() + 4) {
                return uniqueCards.subList(i, i + 5);
            }
        }

        return null;
    }

    private static List<Card> findThreeOfAKind(List<Card> cards) {
        Map<Integer, List<Card>> valueGroups = groupByValue(cards);
        List<Card> kickers = new ArrayList<>(cards);

        for (List<Card> group : valueGroups.values()) {
            if (group.size() == 3) {
                List<Card> result = new ArrayList<>(group);
                kickers.removeAll(group);
                kickers.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
                result.addAll(kickers.subList(0, Math.min(2, kickers.size())));
                return result;
            }
        }
        return null;
    }

    private static List<Card> findTwoPair(List<Card> cards) {
        Map<Integer, List<Card>> valueGroups = groupByValue(cards);
        List<List<Card>> pairs = new ArrayList<>();
        List<Card> kickers = new ArrayList<>(cards);

        for (List<Card> group : valueGroups.values()) {
            if (group.size() >= 2) {
                pairs.add(group.subList(0, 2));
                if (pairs.size() == 2) break;
            }
        }

        if (pairs.size() == 2) {
            List<Card> result = new ArrayList<>();
            for (List<Card> pair : pairs) {
                result.addAll(pair);
                kickers.removeAll(pair);
            }
            kickers.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
            result.add(kickers.get(0));
            return result;
        }
        return null;
    }

    private static List<Card> findOnePair(List<Card> cards) {
        Map<Integer, List<Card>> valueGroups = groupByValue(cards);
        List<Card> kickers = new ArrayList<>(cards);

        for (List<Card> group : valueGroups.values()) {
            if (group.size() >= 2) {
                List<Card> result = new ArrayList<>(group.subList(0, 2));
                kickers.removeAll(group);
                kickers.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
                result.addAll(kickers.subList(0, Math.min(3, kickers.size())));
                return result;
            }
        }
        return null;
    }

    private static Map<Integer, List<Card>> groupByValue(List<Card> cards) {
        Map<Integer, List<Card>> groups = new HashMap<>();
        for (Card card : cards) {
            groups.computeIfAbsent(card.getValue(), k -> new ArrayList<>()).add(card);
        }
        return groups;
    }
}