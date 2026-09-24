import java.util.ArrayList;
import java.util.List;

// Scoring dice selected from a hand. 
public final class Meld {
    private final List<Die> dice = new ArrayList<>();

    // Adds one die to the meld. 
    public void add(Die die) {
        dice.add(die);
    }

    // Removes one die from the meld. 
    public void remove(Die die) {
        dice.remove(die);
    }

    // Calculates the score using the Zag Farkle combo rules. 
    public int getScore() {
        int[] counts = new int[7];
        for (Die die : dice) {
            counts[die.getValue()]++;
        }

        if (dice.size() == 6) {
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
            if (pairs == 3) {
                return 750;
            }
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
}
