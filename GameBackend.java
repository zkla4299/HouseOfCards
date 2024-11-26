import java.util.Scanner;

public class GameBackend {

    //global vars

    private Scanner scan = new Scanner(System.in); //The scanner to get user input

    private String state = "Title"; //The game state
    private int invalidInputs = 0; //How many times has the player entered invalid inputs
    private boolean doGettingInput = true; //Whether or not the game will wait to get new input before contiuing or not
    private boolean gettingInput = false; //Is the game currently waiting for player input

    private double balance = 250; //Current player balance
    
    //Boolean Switches

    private boolean switch1 = false; //Started a new game
    private boolean switch2 = false; //Entered the hub
    private boolean switch3 = false; //Won game room 1
    private boolean switch4 = false; //Won game room 2
    private boolean switch5 = false; //Won game room 3
    private boolean switch6 = false; //Won game room 4
    private boolean switch7 = false; //In game room 1
    private boolean switch8 = false; //In game room 2
    private boolean switch9 = false; //In game room 3
    private boolean switch10 = false; //In game room 4

    //Functions


    /**
     * Gets the current game state based on the state of the various game switches and then sets the variable "state" to the correct state string.
     */
    private void getGameState(){
        if (gettingInput){
            state = "gettingInput";
        }
        else{
            if (switch1){
                if (switch2){
                    if ((switch3) && (switch4) && (switch5) && (switch6)){
                        state = "GameWin";
                    }
                    else if ((switch7) && (!switch3)){
                        System.out.println("Welcome to blackjack, the goal is to get as close to but not over 21 in hand value.");
                        System.out.println("After your turns if at any point the dealer has a hand value that is greater than or equal to yours you lose.");
                        System.out.println("Otherwise you win double your bet, to pass the room you have to get 50 dollars more than your initial balance.");
                        System.out.println("Enter (\"a\") (\"b\") or (\"c\") to set your bet then enter hit or hold to either gain a card or pass your turn to the dealer.");
                        System.out.println("");
                        switch3 = Oxvegas.blackJack(this);
                        switch7 = false;
                        if (switch3){
                            System.out.println("");
                        }else{
                            System.out.println("");
                            checkFailure();
                        }
                    }
                    else if (switch8){
                        PokerGame PG = new PokerGame();
                        /*switch4 = */PG.play();
                        switch8 = false;
                        if (switch4){

                        }else{
                            
                        }
                    }
                    else if (switch9){
                        Slots SG = new Slots(this);
                        switch5 = SG.startGame();
                        switch9 = false;
                        if (switch5){
                            System.out.println("");
                        }else{
                            System.out.println("");
                        }
                    }
                    else if (switch10){
                        RouletteGame RG = new RouletteGame();
                        switch6 = RG.play();
                        switch10 = false;
                        if (switch6){

                        }else{

                        }
                    }
                    else{
                        state = "HubRoom";
                    }
                }
                else{
                    state = "NewGame";
                }
            }
            else{
                state = "Title";
            }
        }
    }

    /**
     * This method takes a string and sets switches and other backend data based on the input string
     * @param input The string being passed to the parser (should be a player's input)
     */
    public void parseInput(String input){
        switch (input){
            case ("new game"):
            case ("n g"):
            case ("n"):
                if (!switch1){
                    switch1 = true;
                    gettingInput = false;
                }
            break;

            case ("game end"):
            case ("g e"):
            case ("exit game"):
            case ("e g"):
            case ("e"):
                System.out.println(" ______________________");
                System.out.println("|                      |");
                System.out.println("|Thank you for playing!|");
                System.out.println("|______________________|");
            break;

            case("title"):
                setAllSwitches(false);
                gettingInput = false;
            break;

            case ("help"):
            case ("help 1"):
                System.out.println("HELP PAGE ONE:\ntitle - syntax: title - The title command\ntakes the player back to the title of the game.\n\ngame end - syntax: game end (aliases: g e, exit game, e g, e) -\nThe game end command, quits the game and program.\n\nhelp - syntax: help (command) (page) - The help command,\ncan be used to look up info on commands\nand get detailed descriptions on them.\n\ngo - syntax: go R(location) - The go command,\nwill take the player to the location entered");
            break;
            case ("help 2"):
                System.out.println("HELP HERE (2)!!");//TODO
                System.out.println("");
            break;
            case ("help 3"):
                System.out.println("HELP HERE (3)!!");//TODO
                System.out.println("");
            break;

            case ("go blackjack"):
                switch7 = true;
                gettingInput = false;
            break;

            case ("go poker"):
                switch8 = true;
                gettingInput = false;
            break;

            case ("go slots"):
                switch9 = true;
                gettingInput = false;
            break;

            case ("go roulette"):
                switch10 = true;
                gettingInput = false;
            break;

            //DEVCOMMAND SECTION -->

            case ("devcommand --201thehousealwayswins-- switch all true"):
                setAllSwitches(true);
                System.out.println("All switches set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch all false"):
                setAllSwitches(false);
                System.out.println("All switches set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 1 true"):
                switch1 = true;
                System.out.println("switch 1 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 1 false"):
                switch1 = false;
                System.out.println("switch 1 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 2 true"):
                switch2 = true;
                System.out.println("switch 2 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 2 true output false"):
                switch2 = true;
            break;
            case ("devcommand --201thehousealwayswins-- switch 2 false"):
                switch2 = false;
                System.out.println("switch 2 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 3 true"):
                switch3 = true;
                System.out.println("switch 3 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 3 false"):
                switch3 = false;
                System.out.println("switch 3 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 4 true"):
                switch4 = true;
                System.out.println("switch 4 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 4 false"):
                switch4 = false;
                System.out.println("switch 4 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 5 true"):
                switch5 = true;
                System.out.println("switch 5 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 5 false"):
                switch5 = false;
                System.out.println("switch 5 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 6 true"):
                switch6 = true;
                System.out.println("switch 6 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 6 false"):
                switch6 = false;
                System.out.println("switch 6 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 7 true"):
                switch7 = true;
                System.out.println("switch 7 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 7 false"):
                switch7 = false;
                System.out.println("switch 7 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 8 true"):
                switch8 = true;
                System.out.println("switch 8 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 8 false"):
                switch8 = false;
                System.out.println("switch 8 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 9 true"):
                switch9 = true;
                System.out.println("switch 9 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 9 false"):
                switch9 = false;
                System.out.println("switch 9 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 10 true"):
                switch10 = true;
                System.out.println("switch 10 set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- switch 10 false"):
                switch10 = false;
                System.out.println("switch 10 set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- gettinginput true"):
                gettingInput = true;
                System.out.println("gettingInput set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- gettinginput false"):
                gettingInput = false;
                System.out.println("gettingInput set to false.\n");
            break;
            case ("devcommand --201thehousealwayswins-- dogettinginput true"):
                doGettingInput = true;
                System.out.println("doGettingInput set to true.\n");
            break;
            case ("devcommand --201thehousealwayswins-- dogettinginput false"):
                doGettingInput = false;
                System.out.println("doGettingInput set to false.\n");
            break;

            //Default command parse (aka invalid commands)

            default:
                if (invalidInputs < 3){
                    System.out.println(" ________________");
                    System.out.println("|                |");
                    System.out.println("| Invalid Input! |");
                    System.out.println("|________________|");
                    System.out.println("");
                    invalidInputs++;
                }
                else{
                    System.out.println(" ________________");
                    System.out.println("|                |");
                    System.out.println("| Invalid Input! |");
                    System.out.println("|________________|");
                    System.out.println("");
                    System.out.println(" _______________________________________________________________________");
                    System.out.println("|                                                                       |");
                    System.out.println("| The \"help\" command has a list of valid commands for use in each room. |");
                    System.out.println("|_______________________________________________________________________|");
                    System.out.println("");
                    invalidInputs = 0;
                }
            break;
        }
    }

    /**
     * This method creates the ascii art for the game and then gets the player's input and returns it
     * @return The player's input
     */
    public String drawASCIIGUI(){
        String playerinput = "";
        this.getGameState();
        switch (state){
            case ("Title"):
                System.out.println(" --------------------------------------------------------------------------------------------------");
                System.out.println("|    \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/   |");
                System.out.println("|  --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- |");
                System.out.println("|    / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\   |");
                System.out.println("|**************************************************************************************************|");
                System.out.println("|  _________  ____     ____ ___            ___  ________  __________         __          ________  |");
                System.out.println("| /   ___   \\ \\   \\   /   / \\  \\          /  / |        ||   _____  |       /  \\        |        | |");
                System.out.println("| |  |   |  |  \\   \\ /   /   \\  \\        /  /  |   _____||  |     |_|      / /\\ \\       |   _____| |");
                System.out.println("| |  |   |  |   \\   V   /     \\  \\      /  /   |  |____  |  | ______      /  --  \\      |  |_____  |");
                System.out.println("| |  |   |  |    >     <       \\  \\    /  /    |   ____| |  ||____  |    /   __   \\     |____    | |");
                System.out.println("| |  |   |  |   /   ^   \\       \\  \\  /  /     |  |_____ |  |     | |   /   /  \\   \\     ____|   | |");
                System.out.println("| |   ---   |  /   / \\   \\       \\  \\/  /      |        ||  |_____| |  /   /    \\   \\   |        | |");
                System.out.println("| \\_________/ /___/   \\___\\       \\____/       |________||__________| /___/      \\___\\  |________| |");
                System.out.println("|==================================================================================================|");
                System.out.println("|-------------------------------------A Gambling Text Adventure------------------------------------|");
                System.out.println("|==================================================================================================|");
                System.out.println("|**************************************************************************************************|");
                System.out.println("|    \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/     \\_/   |");
                System.out.println("|  --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- --|_|-- |");
                System.out.println("|    / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\     / \\   |");
                System.out.println(" --------------------------------------------------------------------------------------------------");
                System.out.println("Make a selection to begin:");
                System.out.println("1.: (N)ew Game");
                System.out.println("2.: (E)xit Game");
                System.out.println("");
                playerinput = scan.nextLine();
                System.out.println("");
                gettingInput = doGettingInput;
                return playerinput;

            case("NewGame"):
                System.out.println(" __________________________________________________________");
                System.out.println("|                                                          |");
                System.out.println("|  You and your friends have decided to go to a casino,    |");
                System.out.println("|  and to not leave until you have won at all of the games.|");
                System.out.println("|  Thus begins your journey to conquer the casino...       |");
                System.out.println("|  Welcome to OXVEGAS CASINO!                              |");
                System.out.println("|  Choose a game to get started with:                      |");
                System.out.println("|  - Blackjack                                             |");
                System.out.println("|  - Poker                                                 |");
                System.out.println("|  - Slots                                                 |");
                System.out.println("|  - Roulette                                              |");
                System.out.println("|  Type \"go (game)\" to get started.                        |");
                System.out.println("|  ex: go blackjack                                        |");
                System.out.println("|                                                          |");
                System.out.println("|----------------------------------------------------------|");
                System.out.println("| ******************************************************** |");
                System.out.println("| Basic Tutorial -- Commands                               |");
                System.out.println("| ******************************************************** |");
                System.out.println("|==========================================================|");
                System.out.println("| Command syntax may seem complicated but is fairly simple:|");
                System.out.println("| syntax: Command R(argument) (argument)                   |");
                System.out.println("|                                                          |");
                System.out.println("| Command - the command that is having its syntax displayed|");
                System.out.println("|                                                          |");
                System.out.println("| (argument) - an argument or thing that can be passed     |");
                System.out.println("| to the command that is being used                        |");
                System.out.println("|                                                          |");
                System.out.println("| R(argument) - a required form of argument, the command   |");
                System.out.println("| will not run without this argument being filled          |");
                System.out.println("|==========================================================|");
                System.out.println("| Some basic commands are as follows:                      |");
                System.out.println("| help - syntax: help (command) (page) - The help command, |");
                System.out.println("| can be used to look up info on commands                  |");
                System.out.println("| and get detailed descriptions on them.                   |");
                System.out.println("|                                                          |");
                System.out.println("| go - syntax: go R(location) - The go command,            |");
                System.out.println("| will take the player to the location entered             |");
                System.out.println("|==========================================================|");
                System.out.println("|__________________________________________________________|");
                System.out.println("");
                System.out.println("");
                return "devcommand --201thehousealwayswins-- switch 2 true output false";

            case("HubRoom"):
                playerinput = scan.nextLine();
                System.out.println("");
                gettingInput = doGettingInput;
                return playerinput;

            case("GameWin"):
                System.out.println("YOU WIN!");
                return "e";

            case("gettingInput"):
                playerinput = scan.nextLine();
                System.out.println("");
                return playerinput;

            default:
                System.out.println("STATE NOT FOUND ERROR -- GAME TERMINATING");
                return "e";
        }
    }

    /**
     * Sets all of the game state switches to a true or false value
     * @param value True or false
     */
    private void setAllSwitches(boolean value){
        switch1 = value;
        switch2 = value;
        switch3 = value;
        switch4 = value;
        switch5 = value;
        switch6 = value;
        switch7 = value;
        switch8 = value;
        switch9 = value;
        switch10 = value;
    }

    private void checkFailure(){
        if (balance < 0){
            System.out.println("");
            gettingInput = false;
            setAllSwitches(false);
            balance = 250;
        }
    }

    /**
     * Gets the variable balance that represents the player's current money in the game.
     * @return The player's balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the varaible balance that represents the player's current money in the game.
     * @param balance The new balance to set  the balance to.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static void main(String[] args) throws Exception {
        GameBackend app = new GameBackend();
        boolean game = true;
        String pI = "";
        while (game){
            pI = app.drawASCIIGUI();
            pI = pI.toLowerCase();
            pI = pI.strip();
            
            app.parseInput(pI);


            if ((!(pI.equals(""))) || (pI != null)){
                switch (pI) {
                    case ("game end"):
                    case ("g e"):
                    case ("exit game"):
                    case ("e g"):
                    case ("e"):
                        game = false;
                    break;
                    
                    default:
                        game = true;
                    break;
                }
            }
        }
    }
}
