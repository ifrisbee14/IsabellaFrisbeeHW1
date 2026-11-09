import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

// Plays one turn and keeps its unbanked points separate from the game score. 
public final class FarkleTurn {
    private final Player player;
    private final Scanner input;
    private final Random random;
    private Hand hand;
    private long turnPoints;
    private boolean quit;

    // Creates one turn for the given player.
    public FarkleTurn(Player player, Scanner input, Random random) {
        this.player = player;
        this.input = input;
        this.random = random;
    }

    // Rolls a new hand and checks for a Farkle.
    private boolean roll(int numberOfDice) {
        System.out.println("Rolling " + numberOfDice + " dice...");
        hand = new Hand(random, numberOfDice);
        hand.printRoll();
        if (hand.isFarkle()) {
            turnPoints = 0;
            System.out.println("Farkle! This turn scores 0. Earlier banked points are safe.");
            return true;
        }
        return false;
    }

    // Prints the current hand, turn score, and menu. 
    private void printMenu() {
        hand.printTable();
        System.out.println("Banked game score: " + player.getScore());
        System.out.println("Points from earlier melds this turn: " + turnPoints);
        System.out.println("Current turn total: " + (turnPoints + hand.getMeldScore()));
        if (hand.isHotHand()) {
            System.out.println("\n***** HOT HAND! ****");
            System.out.println("Roll 6 new dice, or bank and end your turn.");
            System.out.println("************************");
        }
        System.out.println(" (A-F) Add or remove a die from the meld");
        System.out.println(" (R) Reroll dice not used in scoring combos");
        System.out.println(" (K) BanK points and end your turn");
        System.out.println(" (Q) Quit the entire game (unbanked turn points are lost)");
        System.out.print("Enter letters for your choice(s): ");
    }

    // Processes letters until a reroll, bank, or quit changes the current hand.
    private boolean processChoices(String choices) {
        for (int index = 0; index < choices.length(); index++) {
            char choice = choices.charAt(index);
            if (Character.isWhitespace(choice)) {
                continue;
            }
            if (choice >= 'A' && choice <= 'F') {
                int position = choice - 'A';
                if (position < hand.size()) {
                    hand.toggle(position);
                } else {
                    System.out.println("There is no die at " + choice + " in this roll.");
                }
            } else if (choice == 'Q') {
                quit = true;
                return true;
            } else if (choice == 'K' || choice == 'R') {
                if (hand.getMeldScore() == 0) {
                    System.out.println("Choose scoring dice before banking or rerolling.");
                } else {
                    turnPoints += hand.getMeldScore();
                    if (choice == 'K') {
                        player.addPoints(turnPoints);
                        System.out.println(player.getName() + " banked " + turnPoints + " points.");
                        return true;
                    }
                    int nextRollSize = hand.getRerollCount();
                    if (hand.isHotHand()) {
                        System.out.println("***** HOT HAND! Rolling 6 new dice! *****");
                    }
                    return roll(nextRollSize);
                }
            } else {
                System.out.println("Ignoring unknown choice: " + choice);
            }
        }
        return false;
    }

    // Plays the turn until banking, a Farkle, quitting, or end of input.
    public boolean play() {
        turnPoints = 0;
        quit = false;
        System.out.println(player.getName() + ", it is your turn!");
        boolean done = roll(6);
        while (!done) {
            printMenu();
            if (!input.hasNextLine()) {
                quit = true;
                break;
            }
            done = processChoices(input.nextLine().toUpperCase(Locale.ROOT));
        }
        hand.printTable();
        return !quit;
    }
}
