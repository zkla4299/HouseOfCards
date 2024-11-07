import java.util.*;
public class Deck {

    private ArrayList<card> deckOfCardsNoShuffle = new ArrayList<card>();
    private Stack<card> deckOfCardsShuffle = new Stack<card>();
    private Random random = new Random();
    public Deck() {
        char[] chars = {'♥', '♣', '♦', '♠'};
        for(int i = 0; i < chars.length; i ++) {
            for(int j = 1; j < 14; j ++) {
                    deckOfCardsNoShuffle.add(new card(j, chars[i]));
            }
        }
    }

    public void makeAndShuffleCards(boolean jockers) {
        deckOfCardsShuffle.clear();
        ArrayList<card> tempDeck = new ArrayList<>(deckOfCardsNoShuffle); // Make a copy of the original deck
        while(!tempDeck.isEmpty()) {
            int index = random.nextInt(tempDeck.size());
            deckOfCardsShuffle.push(tempDeck.remove(index));
        }
    }
    public void printDeck() {
        while(!deckOfCardsShuffle.isEmpty()) {
            card c = deckOfCardsShuffle.pop();
            System.out.println(c.getCardKind() + " " + c.getCardVal());
        }
    }
   public card drawCard() {
        card topCard = deckOfCardsShuffle.pop();
        return topCard;
    }
}
