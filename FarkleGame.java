import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

// Handles one player's roll, meld selection, and banking. 
public final class FarkleGame {
    private final Scanner input;
    private final Hand hand;

    // Creates a game with a random six-die hand. 
    public FarkleGame() {
        this(new Scanner(System.in), new Random());
    }

    // Creates a game with supplied input and random generator. 
    public FarkleGame(Scanner input, Random random) {
        this.input = input;
        hand = new Hand(random);
    }

    // Plays one round and shows the final score. 
    public void play() {
        System.out.println("Zag Farkle Rolling and Scoring");
        hand.printRoll();

        int totalScore = 0;
        if (hand.isFarkle()) {
            System.out.println("Farkle! Points: 0");
        } else {
            boolean done = false;
            while (!done) {
                hand.printTable();
                System.out.println(" (A-F) Add or remove a die from the meld");
                System.out.println(" (K) BanK Meld & End Round");
                System.out.println(" (Q) Quit game");
                System.out.println();
                System.out.print("Enter letters for your choice(s): ");
                if (!input.hasNext()) {
                    break;
                }
                String choices = input.next().toUpperCase(Locale.ROOT);
                for (int index = 0; index < choices.length() && !done; index++) {
                    char choice = choices.charAt(index);
                    if (choice >= 'A' && choice <= 'F') {
                        hand.toggle(choice - 'A');
                    } else if (choice == 'Q') {
                        done = true;
                    } else if (choice == 'K') {
                        if (hand.getMeldScore() > 0) {
                            totalScore = hand.getMeldScore();
                            done = true;
                        } else {
                            System.out.println("Choose scoring dice before banking.");
                        }
                    } else {
                        System.out.println("Ignoring unknown choice: " + choice);
                    }
                }
            }
            hand.printTable();
        }

        System.out.println();
        System.out.println("Round over. Total score is now: " + totalScore);
    }
}