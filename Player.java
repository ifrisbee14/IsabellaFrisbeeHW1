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

    // Returns all points banked in this game.
    public long getScore() {
        return score;
    }

    // Banks a completed turn's points.
    public void addPoints(long points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        score += points;
    }

    // Resets the game score. A Farkle does not call this method. 
    public void resetScore() {
        score = 0;
    }
}
