// Stores the player's name and their current turn's points. 
public final class Player {
    private String name;
    private int score;

    // Creates a player with zero points.
    public Player(String name) {
        setName(name);
    }

    // Sets the name, using the default for a blank name.
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            this.name = "Unknown Player";
        } else {
            this.name = name.trim();
        }
    }

    // Returns the player's name.
    public String getName() {
        return name;
    }

    // Returns the points accumulated this turn.
    public int getScore() {
        return score;
    }

    // Adds a completed meld's score.
    public void addPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        score += points;
    }

    // Clears the score when starting a turn or rolling a Farkle. 
    public void resetScore() {
        score = 0;
    }
}
