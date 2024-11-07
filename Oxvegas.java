import java.util.ArrayList;
public class Oxvegas {
    public static void printHand(ArrayList<card> hand) {
        System.out.print('[');
        for(int i = 0; i < hand.size() -1; i ++) {
            System.out.print(hand.get(i).getCardKind());
            System.out.print(hand.get(i).getCardVal());
            System.out.print(',');
        }
        System.out.print(hand.get(hand.size() -1).getCardKind());
        System.out.print(hand.get(hand.size() - 1).getCardVal());
        System.out.println(']');
    }
    public static boolean blackJack() {
        Deck d1 = new Deck();
        d1.makeAndShuffleCards(false);
        ArrayList<card> playersHand = new ArrayList<card>();
        ArrayList<card> dealersHand = new ArrayList<card>();
        // user bets
        // draws hand
        playersHand.add(d1.drawCard());
        playersHand.add(d1.drawCard());
        // user hits, bets more, or busts
        // if user won enough move forward
        // players turn:
        System.out.println("your hand is ");
        printHand(playersHand);
        return false;
    }
    public static void main(String[] args) {
        blackJack();
    }
}
