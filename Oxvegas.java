import java.util.ArrayList;
import java.util.Scanner;

public class Oxvegas {


    //print card simply prints the card objects throughout the program
    public static void printCard(cardBlackJack c) {
        System.out.print(c.getCardKind());
        if(c.getCardVal() == 11) {
            System.out.print("J");
        } else if(c.getCardVal() == 12) {
            System.out.print("Q");
        } else if(c.getCardVal() == 13) {
            System.out.print("K");
        } else if(c.getCardVal() == 1) {
            System.out.print("A");
        } else {
            System.out.print(c.getCardVal());
        }
    }

    // Prints the hand of the player
    public static void printHand(ArrayList<cardBlackJack> hand) {
        System.out.print('[');
        for(int i = 0; i < hand.size() -1; i ++) {
            printCard(hand.get(i));
            System.out.print(',');
        }
        printCard(hand.get(hand.size()-1));
        System.out.println(']');
    }

    // prints the hands of the other players including the dealer
    public static void printTableHands(ArrayList<cardBlackJack> hand) {
        System.out.print('[');
        for(int i = 0; i < hand.size() -2; i ++) {
            printCard(hand.get(i));
            System.out.print(',');
        }
        printCard(hand.get(hand.size()-1));

        System.out.print(", Face Down Card");

        System.out.println(']');
    }

    // calculates the hand total of a given player
    public static int handTotal(ArrayList<cardBlackJack> hand) {
        int sum = 0;
        for(int i = 0; i < hand.size(); i ++) {
            int val = hand.get(i).getCardVal();
            if(val > 10) {
                sum += 10;
            } else {
                sum += val;
            }
        }
        return sum;
    }

    // Asks user what action they want to take and turns that input into something we can use later
    public static String playerAction() {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("Hit - Draw a Card");
        System.out.println("Hold - Keep Current Hand");
        Scanner playerAction = new Scanner(System.in);
        String inputValue = playerAction.next();
        return inputValue.toLowerCase();

    }

    // takes in everything above and executes the players turn
    public static boolean playerTurn(ArrayList<cardBlackJack> hand, DeckBlackJack d1, double returnValueMultiplyer) {

        // print the hand of the player
        System.out.println("your hand is: ");
        printHand(hand);
        System.out.println("your hand total is: " + handTotal(hand));
        Boolean hold = false;
        // player wins automatically if it's equal to 21
        while(handTotal(hand) < 22 && hold == false) {
            if(handTotal(hand) == 21) {
                System.out.println("You got a hand equaling 21, you win "+ returnValueMultiplyer +"x your original bet!");
                return true;
            }
            // get players chosen action
            String action = playerAction();

            // runs if player chooses to hold
            if(action.equals("hold")) {
                hold = true;
            } else if (action.equals("hit")) { // runs if the player chooses to hit(draw a card)
                hand.add(d1.drawCard());
                System.out.println("your hand is: ");
                printHand(hand);
                System.out.println("your hand total is: " + handTotal(hand));
                if(handTotal(hand) > 21) { // if the hand of the player is over 21 they automatically lose the round and their bet
                    System.out.println("your hand busted. You lose the round");
                    return false;
                }
            } else {
                //do nothing
            }
        }
        return true;
    }

    // runs in the back end taking in some basic logic to run a dealers turn. Also handles winning or losing. Directly impacting the user's ability to win
    public static boolean dealersTurn(ArrayList<cardBlackJack> hand, DeckBlackJack d1, double returnValueMultiplyer) {
        Boolean hold = false;
        while(handTotal(hand) < 22 && hold == false) {

            if(handTotal(hand) < 17) {
                hand.add(d1.drawCard());
                if(handTotal(hand) > 22) {
                    System.out.println("Dealer's hand busted. All remaining players win the round! bet is returned with a multiple of " + returnValueMultiplyer + "x");
                    System.out.println("their hand was: ");
                    printHand(hand);
                    System.out.println("their hand total is: " + handTotal(hand));
                    return false;
                }
            } else {
                System.out.println("Dealer's hand was: ");
                printHand(hand);
                System.out.println("Dealer's hand total is: " + handTotal(hand));
                return true;
            }
        }
        return true;
    }

    // This ties everything above it, runs for each round of BlackJack and updates balances
    public static double blackJackRound(double bankAmount) {
        DeckBlackJack d1 = new DeckBlackJack();
        d1.makeAndShuffleCards(false);
        ArrayList<cardBlackJack> playersHand = new ArrayList<cardBlackJack>();
        ArrayList<cardBlackJack> dealersHand = new ArrayList<cardBlackJack>();

        double multiplierValue = 2;

        int bidAmountOption1 = 10;
        int bidAmountOption2 = 25;
        int bidAmountOption3 = 50;
        int bidAmount = 0;
        // user bets
        boolean validBid = false;
        while(validBid == false) {
            Scanner bet = new Scanner(System.in);
            System.out.println("Do you want to bet a.) " + bidAmountOption1 + " b.) " + bidAmountOption2 + " c.) " + bidAmountOption3);
            char inputValue = bet.next().charAt(0);
            inputValue = Character.toLowerCase(inputValue);
            if (inputValue == 'a') {
                bidAmount = bidAmountOption1;
                validBid = true;
            } else if (inputValue == 'b') {
                bidAmount = bidAmountOption2;
                validBid = true;
            } else if (inputValue == 'c') {
                bidAmount = bidAmountOption3;
                validBid = true;
            } else {
                // do nothing
            }
        }
        bankAmount -= bidAmount;

        // draws hand
        playersHand.add(d1.drawCard());
        dealersHand.add(d1.drawCard());
        playersHand.add(d1.drawCard());
        dealersHand.add(d1.drawCard());

        // others hands that you can see
        System.out.println("Dealers Hand: ");
        printTableHands(dealersHand);
        System.out.println();



        // user hits, bets more, or busts
        // if user won enough move forward
        // players turn:
        boolean playerResult = playerTurn(playersHand, d1, multiplierValue);
        if(handTotal(playersHand) == 21) {

            System.out.println("Dealer got 21! All remaining players lose");
            System.out.println("their hand was: ");
            printHand(dealersHand);
            System.out.println("their hand total is: " + handTotal(playersHand));
            return bankAmount + (multiplierValue*bidAmount);
        }

        System.out.println();

        boolean dealerResult = false;

        if(playerResult == false) {
            System.out.println("Dealer wins all bets are lost");
        } else {
            System.out.println();
            dealerResult = dealersTurn(dealersHand, d1, multiplierValue);
        }

        int playerHandTotal = handTotal(playersHand);
        int dealershandTotal = handTotal(dealersHand);

        if(dealerResult == false) {
            if(playerResult == true && playerHandTotal < 21) {
                System.out.println("you win! You beat the dealer");
                return bankAmount + (multiplierValue*bidAmount);
            }

        }
        if(dealerResult == true && playerResult == true) {
            if(dealershandTotal >=playerHandTotal) {
                System.out.println("you lose, dealer wins!");
                System.out.println("their hand was: ");
                printHand(dealersHand);
            } else {
                System.out.println("you win, you get " + multiplierValue + "x your bet");
                return bankAmount + (multiplierValue*bidAmount);
            }
        }

        if(dealershandTotal == playerHandTotal && playerHandTotal != 21) {
            System.out.println("you lost! the dealer tied with you. you lose your bet");
        }

        return bankAmount;

    }

    // The blackJack function runs a number of blackJack rounds equal to the amount specified. Returns true if you win
    // returns false if you lose either by losing all your money, or by not winning the minimum amount required to move on
    // after a certain number of rounds.
    public static boolean blackJack() {
        int roundsPlayed = 0;
        int roundLimit = 5;
        double winAmount = 100;
        double currentAmount = 50;
        while(roundsPlayed < roundLimit && currentAmount < winAmount && currentAmount > 0) {
            currentAmount = blackJackRound(currentAmount);
            System.out.println();
            System.out.println("Remaining Balance: " + currentAmount);
            roundsPlayed +=1;

            if(currentAmount >= winAmount) {
                System.out.println("Congrats you won the BlackJack Room");
                return true;
            }

            if(currentAmount <= 0) {
                System.out.println("You have no more remaining funds, you lost");
                return false;
            }
        }
        return false;
    }

    // main just runs the BlackJack function.
    public static void main(String[] args) {
        blackJack();
    }
}
