import java.util.Random;

// One six-sided die. 
public final class Die {
    private final int value;

    // Creates a die showing the given face. 
    public Die(int value) {
        if (value < 1 || value > 6) {
            throw new IllegalArgumentException("A die must show 1 through 6");
        }
        this.value = value;
    }

    // Rolls a new die. 
    public static Die roll(Random random) {
        return new Die(random.nextInt(6) + 1);
    }

    // Returns the face value. 
    public int getValue() {
        return value;
    }
}
