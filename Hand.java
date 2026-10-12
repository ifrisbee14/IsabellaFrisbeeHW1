import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** One to six sorted dice with fixed letter positions within a roll. */
public final class Hand {
    private static final int HAND_SIZE = 6;
    private final List<Die> dice = new ArrayList<>();
    private final boolean[] inMeld = new boolean[HAND_SIZE];
    private final Meld meld = new Meld();

    /** Rolls and sorts six dice.
     * @param random random number generator
     */
    public Hand(Random random) {
        this(random, HAND_SIZE);
    }

    /** Rolls the requested number of dice.
     * @param random random number generator
     * @param numberOfDice number of dice from 1 through 6
     */
    public Hand(Random random, int numberOfDice) {
        checkSize(numberOfDice);
        for (int index = 0; index < numberOfDice; index++) {
            dice.add(Die.roll(random));
        }
        sortDice();
    }

    /** Creates a sorted hand from fixed face values for testing.
     * @param faces one to six face values
     */
    public Hand(int... faces) {
        checkSize(faces.length);
        for (int face : faces) {
            dice.add(new Die(face));
        }
        sortDice();
    }

    /** Checks that a roll has a permitted number of dice.
     * @param numberOfDice number of dice to check
     */
    private static void checkSize(int numberOfDice) {
        if (numberOfDice < 1 || numberOfDice > HAND_SIZE) {
            throw new IllegalArgumentException("A hand needs one through six dice");
        }
    }

    /** Sorts the dice from lowest to highest face value. */
    private void sortDice() {
        Collections.sort(dice, (first, second) ->
                Integer.compare(first.getValue(), second.getValue()));
    }

    /** Returns the number of dice in this roll.
     * @return the hand size
     */
    public int size() {
        return dice.size();
    }

    /** Toggles a die between the unused hand and the meld.
     * @param index zero-based menu position
     */
    public void toggle(int index) {
        Die die = dice.get(index);
        if (inMeld[index]) {
            meld.remove(die);
        } else {
            meld.add(die);
        }
        inMeld[index] = !inMeld[index];
    }

    /** Returns the selected meld score.
     * @return the current meld score
     */
    public int getMeldScore() {
        return meld.getScore();
    }

    /** Reports whether the whole rolled hand cannot score.
     * @return true when the roll is a Farkle
     */
    public boolean isFarkle() {
        Meld wholeHand = new Meld();
        for (Die die : dice) {
            wholeHand.add(die);
        }
        return wholeHand.isFarkle();
    }

    /** Reports whether every die in this roll scores in the current meld.
     * @return true for a hot hand
     */
    public boolean isHotHand() {
        return meld.getScoringDiceCount() == dice.size();
    }

    /** Returns the next roll size, including selected dice that do not score.
     * @return remaining dice, or six new dice for a hot hand
     */
    public int getRerollCount() {
        int remaining = dice.size() - meld.getScoringDiceCount();
        return remaining == 0 ? HAND_SIZE : remaining;
    }

    /** Prints the current roll. */
    public void printRoll() {
        System.out.print("Hand: ");
        for (Die die : dice) {
            System.out.print(die.getValue() + " ");
        }
        System.out.println();
    }

    /** Prints the hand and meld table with A through F menu positions. */
    public void printTable() {
        System.out.println();
        System.out.println("**************** Current hand and meld ****************");
        System.out.println(" Die   Hand |   Meld");
        System.out.println("------------+---------------");
        for (int index = 0; index < HAND_SIZE; index++) {
            char option = (char) ('A' + index);
            String handValue = " ";
            String meldValue = " ";
            if (index < dice.size()) {
                if (inMeld[index]) {
                    meldValue = "" + dice.get(index).getValue();
                } else {
                    handValue = "" + dice.get(index).getValue();
                }
            }
            System.out.println(" (" + option + ")    " + handValue + "   |     " + meldValue);
        }
        System.out.println("------------+---------------");
        System.out.println("                Meld Score: " + getMeldScore());
        System.out.println();
    }
}
