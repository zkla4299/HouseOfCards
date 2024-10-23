public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Deck d1 = new Deck();
        d1.makeAndShuffleCards(false);
        d1.printDeck();
    }
}
