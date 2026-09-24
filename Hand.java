import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Six sorted dice with fixed A through F positions. 
public final class Hand {
    private static final int HAND_SIZE = 6;

    private final List<Die> dice = new ArrayList<>();
    private final boolean[] inMeld = new boolean[HAND_SIZE];
    private final Meld meld = new Meld();

    // Rolls and sorts six dice. 
    public Hand(Random random) {
        for (int index = 0; index < HAND_SIZE; index++) {
            dice.add(Die.roll(random));
        }
        sortDice();
    }

    // Creates a sorted hand from exactly six fixed face values, for testing. 
    public Hand(int... faces) {
        if (faces.length != HAND_SIZE) {
            throw new IllegalArgumentException("A hand needs exactly six dice");
        }
        for (int face : faces) {
            dice.add(new Die(face));
        }
        sortDice();
    }

    // Sorts the dice from lowest to highest face value. 
    private void sortDice() {
        Collections.sort(dice, (first, second) ->
                Integer.compare(first.getValue(), second.getValue()));
    }

    // Toggles the die at the given zero-based menu position. 
    public void toggle(int index) {
        Die die = dice.get(index);
        if (inMeld[index]) {
            meld.remove(die);
        } else {
            meld.add(die);
        }
        inMeld[index] = !inMeld[index];
    }

    // Returns the current meld score. 
    public int getMeldScore() {
        return meld.getScore();
    }

    // Returns true when no subset of the rolled hand can score. 
    public boolean isFarkle() {
        int[] counts = new int[7];
        for (Die die : dice) {
            counts[die.getValue()]++;
        }
        if (counts[1] > 0 || counts[5] > 0) {
            return false;
        }
        int pairs = 0;
        for (int face = 1; face <= 6; face++) {
            if (counts[face] >= 3) {
                return false;
            }
            if (counts[face] == 2) {
                pairs++;
            }
        }
        return pairs != 3;
    }

    // Prints the original roll. 
    public void printRoll() {
        System.out.print("Hand: ");
        for (Die die : dice) {
            System.out.print(die.getValue() + " ");
        }
        System.out.println();
    }

    // Prints the hand and meld table with the A through F menu letters. 
    public void printTable() {
        System.out.println();
        System.out.println("*************************** Current hand and meld *******************");
        System.out.println(" Die   Hand |   Meld");
        System.out.println("------------+---------------");
        for (int index = 0; index < dice.size(); index++) {
            char option = (char) ('A' + index);
            String handValue = inMeld[index] ? " " : "" + dice.get(index).getValue();
            String meldValue = inMeld[index] ? "" + dice.get(index).getValue() : " ";
            System.out.println(" (" + option + ")    " + handValue + "   |     " + meldValue);
        }
        System.out.println("------------+---------------");
        System.out.println("                Meld Score: " + getMeldScore());
        System.out.println();
    }
}