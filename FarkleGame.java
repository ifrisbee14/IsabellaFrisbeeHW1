import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

// Handles one player's turn, including repeated rolls and accumulated points. 
public final class FarkleGame {
    private final Scanner input;
    private final Random random;
    private final Player player = new Player("");
    private Hand hand;

    // Creates a game using keyboard input and random dice. 
    public FarkleGame() {
        this(new Scanner(System.in), new Random());
    }

    // Creates a game with supplied input and dice generator for testing.
    public FarkleGame(Scanner input, Random random) {
        this.input = input;
        this.random = random;
    }

    // Returns the player so tests can check the final result.
    public Player getPlayer() {
        return player;
    }

    // Prints the game's introduction and instructions. 
    private void printBanner() {
        System.out.println("********************************************************");
        System.out.println("*          Zag Farkle by Isabella Frisbee!              *");
        System.out.println("*                  Copyright: 2026                     *");
        System.out.println("********************************************************");
        System.out.println("Select dice with A-F; repeat a letter to remove a die.");
        System.out.println("R rerolls remaining dice, K banks, and Q quits.");
        System.out.println("A Farkle loses all points earned during this turn.");
    }

    // Shows the rolled dice and checks for a Farkle.
    private boolean checkRoll() {
        hand.printRoll();
        if (hand.isFarkle()) {
            player.resetScore();
            System.out.println("Farkle! Points: 0. Your turn is over.");
            return true;
        }
        return false;
    }

    // Prints the current score, dice, and available choices. 
    private void printMenu() {
        hand.printTable();
        System.out.println("Points from earlier melds: " + player.getScore());
        System.out.println("Current total: " + (player.getScore() + hand.getMeldScore()));
        if (hand.isHotHand()) {
            System.out.println("\n***** HOT HAND! ****");
            System.out.println("Would you like to roll 6 new dice, or bank and end your turn?");
            System.out.println("************************");
        }
        System.out.println(" (A-F) Add or remove a die from the meld");
        System.out.println(" (R) Reroll dice not used in scoring combos");
        System.out.println(" (K) BanK Meld & End Round");
        System.out.println(" (Q) Quit game and show current total");
        System.out.print("Enter letters for your choice(s): ");
    }

    // Applies one line of menu input until a roll or an end-of-turn choice.
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
                player.addPoints(hand.getMeldScore());
                return true;
            } else if (choice == 'K' || choice == 'R') {
                if (hand.getMeldScore() == 0) {
                    System.out.println("Choose scoring dice before banking or rerolling.");
                } else {
                    player.addPoints(hand.getMeldScore());
                    if (choice == 'K') {
                        return true;
                    }
                    int nextRollSize = hand.getRerollCount();
                    if (hand.isHotHand()) {
                        System.out.println("***** HOT HAND! Rolling 6 new dice! *****");
                    }
                    System.out.println("Rolling " + nextRollSize + " dice...");
                    hand = new Hand(random, nextRollSize);
                    return checkRoll();
                }
            } else {
                System.out.println("Ignoring unknown choice: " + choice);
            }
        }
        return false;
    }

    // Plays one turn, ending on banking, quitting, or a Farkle. 
    public void play() {
        printBanner();
        System.out.print("Enter your player's name: ");
        String name = input.hasNextLine() ? input.nextLine() : "";
        player.setName(name);
        player.resetScore();
        System.out.println(player.getName() + ", it is your turn! Rolling dice...");
        hand = new Hand(random);
        boolean done = checkRoll();
        while (!done) {
            printMenu();
            if (!input.hasNextLine()) {
                player.addPoints(hand.getMeldScore());
                break;
            }
            String choices = input.nextLine().toUpperCase(Locale.ROOT);
            done = processChoices(choices);
        }
        hand.printTable();
        System.out.println(player.getName() + ", round over. Total score is now: "
                + player.getScore());
    }
}
