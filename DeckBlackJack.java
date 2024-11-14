import java.util.*;
public class DeckBlackJack {

    private ArrayList<cardBlackJack> deckOfCardsNoShuffle = new ArrayList<cardBlackJack>();
    private Stack<cardBlackJack> deckOfCardsShuffle = new Stack<cardBlackJack>();
    private Random random = new Random();
    public DeckBlackJack() {
        char[] chars = {'♥', '♣', '♦', '♠'};
        for(int i = 0; i < chars.length; i ++) {
            for(int j = 1; j < 14; j ++) {
                    deckOfCardsNoShuffle.add(new cardBlackJack(j, chars[i]));
            }
        }
    }

    public void makeAndShuffleCards(boolean jockers) {
        deckOfCardsShuffle.clear();
        ArrayList<cardBlackJack> tempDeck = new ArrayList<>(deckOfCardsNoShuffle); // Make a copy of the original deck
        while(!tempDeck.isEmpty()) {
            int index = random.nextInt(tempDeck.size());
            deckOfCardsShuffle.push(tempDeck.remove(index));
        }
    }
    public void printDeck() {
        while(!deckOfCardsShuffle.isEmpty()) {
            cardBlackJack c = deckOfCardsShuffle.pop();
            System.out.println(c.getCardKind() + " " + c.getCardVal());
        }
    }
   public cardBlackJack drawCard() {
       cardBlackJack topCard = deckOfCardsShuffle.pop();
        return topCard;
    }
}
