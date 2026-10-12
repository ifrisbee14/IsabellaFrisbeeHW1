import java.util.ArrayList;
import java.util.List;

// Scoring dice selected from a hand. 
public final class Meld {
    private final List<Die> dice = new ArrayList<>();

    // Creates an empty meld. 
    public Meld() {
    }

    // Creates a meld from fixed face values, useful for testing.
    public Meld(int... faces) {
        for (int face : faces) {
            add(new Die(face));
        }
    }

    // Adds one die to the meld.
    public void add(Die die) {
        if (die == null || dice.size() == 6) {
            throw new IllegalArgumentException("A meld needs at most six real dice");
        }
        dice.add(die);
    }

    // Removes one die from the meld.
    public void remove(Die die) {
        dice.remove(die);
    }

    // Counts the dice showing each face value.
    private int[] getCounts() {
        int[] counts = new int[7];
        for (Die die : dice) {
            counts[die.getValue()]++;
        }
        return counts;
    }

    // Finds a six-die straight or three different pairs.
    private int getSpecialScore(int[] counts) {
        if (dice.size() != 6) {
            return 0;
        }
        boolean straight = true;
        int pairs = 0;
        for (int face = 1; face <= 6; face++) {
            if (counts[face] != 1) {
                straight = false;
            }
            if (counts[face] == 2) {
                pairs++;
            }
        }
        if (straight) {
            return 1000;
        }
        return pairs == 3 ? 750 : 0;
    }

    // Calculates the score using the HW1 Appendix B rules.
    public int getScore() {
        int[] counts = getCounts();
        int specialScore = getSpecialScore(counts);
        if (specialScore > 0) {
            return specialScore;
        }
        int score = 0;
        for (int face = 1; face <= 6; face++) {
            if (counts[face] >= 3) {
                score += face == 1 ? 1000 : face * 100;
                score += (counts[face] - 3) * face * 100;
            } else if (face == 1) {
                score += counts[face] * 100;
            } else if (face == 5) {
                score += counts[face] * 50;
            }
        }
        return score;
    }

    // Counts selected dice that are actually part of a scoring combo.
    public int getScoringDiceCount() {
        int[] counts = getCounts();
        if (getSpecialScore(counts) > 0) {
            return 6;
        }
        int scoringDice = 0;
        for (int face = 1; face <= 6; face++) {
            if (face == 1 || face == 5 || counts[face] >= 3) {
                scoringDice += counts[face];
            }
        }
        return scoringDice;
    }

    //Reports whether these dice cannot form any scoring combo.
    public boolean isFarkle() {
        return getScore() == 0;
    }
}
