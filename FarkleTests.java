// Ten tests for the HW2 Zag Farkle classes. 
public final class FarkleTests {
    private static int testsPassed;

    private FarkleTests() {
    }

    // Runs all ten tests.
    public static void main(String[] args) {
        testStraight();
        testThreePairs();
        testSingleOne();
        testSingleFive();
        testTripleOnes();
        testPlayerName();
        testDefaultName();
        testFarkle();
        testRerollCount();
        testHotHand();
        System.out.println("Passed " + testsPassed + " tests.");
    }

    // Tests that a straight scores 1000 points. 
    private static void testStraight() {
        Meld meld = new Meld(1, 2, 3, 4, 5, 6);
        check(meld.getScore() == 1000, "Straight");
    }

    // Tests that three different pairs score 750 points. 
    private static void testThreePairs() {
        Meld meld = new Meld(2, 2, 4, 4, 6, 6);
        check(meld.getScore() == 750, "Three pairs");
    }

    // Tests that a single one scores 100 points. 
    private static void testSingleOne() {
        Meld meld = new Meld(1);
        check(meld.getScore() == 100, "Single one");
    }

    // Tests that a single five scores 50 points. 
    private static void testSingleFive() {
        Meld meld = new Meld(5);
        check(meld.getScore() == 50, "Single five");
    }

    // Tests that triple ones score 1000 without also scoring as singles. 
    private static void testTripleOnes() {
        Meld meld = new Meld(1, 1, 1);
        check(meld.getScore() == 1000, "Triple ones");
    }

    // Tests that the name setter stores the requested name. 
    private static void testPlayerName() {
        Player player = new Player("");
        player.setName("Isabella");
        check(player.getName().equals("Isabella"), "Player name");
    }

    // Tests that a blank name defaults to Unknown Player. 
    private static void testDefaultName() {
        Player player = new Player("");
        check(player.getName().equals("Unknown Player"), "Default name");
    }

    // Tests a known Farkle through the Meld class. 
    private static void testFarkle() {
        Meld meld = new Meld(2, 2, 3, 4, 4, 6);
        check(meld.isFarkle(), "Farkle detection");
    }

    // Tests that selected dice without a scoring combo still reroll. 
    private static void testRerollCount() {
        Hand hand = new Hand(1, 2, 3, 4, 4, 6);
        hand.toggle(0);
        hand.toggle(1);
        check(hand.getRerollCount() == 5, "Reroll count");
    }

    // Tests that using every die in a straight gives six new dice. 
    private static void testHotHand() {
        Hand hand = new Hand(1, 2, 3, 4, 5, 6);
        for (int index = 0; index < hand.size(); index++) {
            hand.toggle(index);
        }
        check(hand.isHotHand() && hand.getRerollCount() == 6, "Hot hand");
    }

    // Reports a passed test or stops on a failure.
    private static void check(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError("FAILED: " + name);
        }
        testsPassed++;
        System.out.println("PASSED: " + name);
    }
}
