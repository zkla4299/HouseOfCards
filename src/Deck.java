import java.util.*;
public class Deck {

    private ArrayList<card> deckOfCardsNoShuffle = new ArrayList<card>();
    private Stack<card> deckOfCardsShuffle = new Stack<card>();
    private Random random = new Random();
    public Deck() {
        char[] chars = {'H', 'C', 'D', 'S'};
        for(int i = 0; i < chars.length; i ++) {
            for(int j = 1; j < 14; j ++) {
                    deckOfCardsNoShuffle.add(new card(j, chars[i]));
            }
        }
    }

    public void makeAndShuffleCards(boolean jockers) {
        char[] chars = {'H', 'C', 'D', 'S'};
        for(int i = 0; i < chars.length; i ++) {
            for(int j = 1; j < 14; j ++) {
                deckOfCardsNoShuffle.add(new card(j, chars[i]));
            }
        }
        while (!deckOfCardsNoShuffle.isEmpty()) {
            int index = random.nextInt(deckOfCardsNoShuffle.size());
            deckOfCardsShuffle.push(deckOfCardsNoShuffle.remove(index));
        }
    }
    public void printDeck() {
        while(!deckOfCardsShuffle.isEmpty()) {
            card c = deckOfCardsShuffle.pop();
            System.out.println(c.getCardKind() + " " + c.getCardVal());
        }
    }
}
