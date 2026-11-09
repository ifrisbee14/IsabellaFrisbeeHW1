import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Sets up players, runs turns, and manages the final round. 
public final class FarkleGame {
    private static final int DEFAULT_TARGET = 10000;
    private static final String[] DEFAULT_NAMES = {
        "Maple", "Cedar", "Willow", "Aspen", "Birch", "Oak",
        "Pine", "Elm", "Hazel", "Laurel", "Rowan", "Juniper"
    };
    private final Scanner input;
    private final Random random;
    private final List<Player> players = new ArrayList<>();
    private int targetScore = DEFAULT_TARGET;
    private Player winner;
    private boolean quit;

    // Creates a game using keyboard input and random dice. 
    public FarkleGame() {
        this(new Scanner(System.in), new Random());
    }

    // Creates a game with supplied input and dice for testing.
    public FarkleGame(Scanner input, Random random) {
        this.input = input;
        this.random = random;
    }

    // Returns a read-only view of the game's players.
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    // Returns the first player, retaining the HW2 test accessor.
    public Player getPlayer() {
        return players.get(0);
    }

    // Returns the selected target.
    public int getTargetScore() {
        return targetScore;
    }

    // Returns the winner after a completed game.
    public Player getWinner() {
        return winner;
    }

    // Prints the banner and instructions. 
    private void printBanner() {
        System.out.println("********************************************************");
        System.out.println("*          Zag Farkle by Isabella Frisbee              *");
        System.out.println("*                  Copyright: 2026                     *");
        System.out.println("********************************************************");
        System.out.println("Select dice with A-F; repeat a letter to remove a die.");
        System.out.println("R rerolls, K banks, and Q quits the entire game.");
        System.out.println("A full house (three of one face and two of another) scores 1500.");
        System.out.println("A Farkle loses this turn's points, but keeps earlier banked points.");
    }

    // Reads a line or marks the game as quit when input ends.
    private String readLine() {
        if (!input.hasNextLine()) {
            quit = true;
            return "";
        }
        return input.nextLine().trim();
    }

    // Reads a positive integer, repeating invalid input.
    private int readPositiveInt(String prompt, int defaultValue) {
        while (!quit) {
            System.out.print(prompt + " [" + defaultValue + "]: ");
            String answer = readLine();
            if (answer.isEmpty()) {
                return defaultValue;
            }
            try {
                int value = Integer.parseInt(answer);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                // The same message covers non-numeric and out-of-range input.
            }
            System.out.println("Please enter a positive whole number.");
        }
        return defaultValue;
    }

    //  Checks whether a name is already taken, ignoring case.
    private boolean nameTaken(String name) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // Finds an unused named default for a multiplayer game.
    private String getDefaultName(int playerIndex) {
        int candidateIndex = playerIndex;
        String candidate;
        do {
            candidate = DEFAULT_NAMES[candidateIndex % DEFAULT_NAMES.length];
            int group = candidateIndex / DEFAULT_NAMES.length;
            if (group > 0) {
                candidate += " " + (group + 1);
            }
            candidateIndex++;
        } while (nameTaken(candidate));
        return candidate;
    }

    // Asks for the target, number of players, and all names. 
    private void setUp() {
        targetScore = readPositiveInt("How many points would you like to play to?",
                DEFAULT_TARGET);
        if (quit) {
            return;
        }
        int numberOfPlayers = readPositiveInt("How many players are going to be playing?", 1);
        for (int index = 0; index < numberOfPlayers && !quit; index++) {
            String defaultName = numberOfPlayers == 1 ? "Unknown Player" : getDefaultName(index);
            boolean accepted = false;
            while (!accepted && !quit) {
                System.out.print("What is player #" + (index + 1) + "'s name? ["
                        + defaultName + "]: ");
                String name = readLine();
                if (quit) {
                    return;
                }
                if (name.isEmpty()) {
                    name = defaultName;
                }
                if (nameTaken(name)) {
                    System.out.println("That name is already taken. Please choose another.");
                } else {
                    players.add(new Player(name));
                    accepted = true;
                }
            }
        }
    }

    // Prints every player's banked game score. 
    private void printScores() {
        System.out.println("\nCurrent total points:");
        for (Player player : players) {
            System.out.println("    " + player.getName() + "    " + player.getScore());
        }
    }

    // Plays the turns and gives every opponent exactly one final turn. 
    private void playTurns() {
        int playerIndex = 0;
        int turnNumber = 1;
        int finalTurnsLeft = -1;
        Player leader = null;
        while (!quit) {
            Player player = players.get(playerIndex);
            System.out.println("\nTurn " + turnNumber + ": " + player.getName());
            if (finalTurnsLeft >= 0 && leader != null) {
                System.out.println("FINAL TURN - beat " + leader.getName() + "'s "
                        + leader.getScore() + " points!");
            }
            FarkleTurn turn = new FarkleTurn(player, input, random);
            quit = !turn.play();
            printScores();
            if (quit) {
                return;
            }
            if (finalTurnsLeft >= 0 && leader != null) {
                if (player.getScore() > leader.getScore()) {
                    leader = player;
                }
                finalTurnsLeft--;
            } else if (player.getScore() >= targetScore) {
                leader = player;
                finalTurnsLeft = players.size() - 1;
                System.out.println(player.getName() + " reached the target of "
                        + targetScore + "!");
                if (finalTurnsLeft > 0) {
                    System.out.println("Final round: each other player gets one last turn.");
                    System.out.println("You must beat the leader; a tie keeps "
                            + "the current leader ahead.");
                }
            }
            if (finalTurnsLeft == 0) {
                winner = leader;
                return;
            }
            playerIndex = (playerIndex + 1) % players.size();
            turnNumber++;
        }
    }

    // Runs a complete game until the final round finishes or anyone quits. 
    public void play() {
        players.clear();
        winner = null;
        quit = false;
        printBanner();
        setUp();
        if (!quit) {
            playTurns();
        }
        System.out.println("\n********************************************************");
        if (winner == null) {
            System.out.println("Game quit. No winner was declared.");
        } else {
            System.out.println("WINNER: " + winner.getName() + " with "
                    + winner.getScore() + " points!");
        }
        System.out.println("********************************************************");
        printScores();
    }
}
