import java.util.Random;
import java.util.Scanner;

// Focused tests for Zag Farkle scoring, turns, and complete games. 
public final class FarkleTests {
    private static int testsPassed;

    private FarkleTests() {
    }

    // Runs scoring tests and scripted game tests.
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
        testFullHouse();
        testFullHouseReroll();
        testGameDefaults();
        testMultipleTurns();
        testFinalRound();
        testFinalRoundTie();
        testQuitGame();
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

    // Tests both full-house examples without also counting the triple. 
    private static void testFullHouse() {
        Meld first = new Meld(2, 2, 4, 4, 4);
        Meld second = new Meld(1, 1, 1, 2, 2);
        check(first.getScore() == 1500 && second.getScore() == 1500
                && first.getScoringDiceCount() == 5, "Full house");
    }

    // Tests full houses with scoring and non-scoring sixth dice. 
    private static void testFullHouseReroll() {
        Hand hand = new Hand(2, 2, 4, 4, 4, 6);
        for (int index = 0; index < hand.size(); index++) {
            hand.toggle(index);
        }
        Meld withFive = new Meld(1, 1, 1, 2, 2, 5);
        check(hand.getMeldScore() == 1500 && hand.getRerollCount() == 1
                && !hand.isHotHand() && withFive.getScore() == 1550
                && withFive.getScoringDiceCount() == 6, "Full house and remaining dice");
    }

    // Tests blank defaults, invalid numbers, and unique named defaults. 
    private static void testGameDefaults() {
        FarkleGame single = runGame("\n\n\nq\n", 6, 1, 2, 2, 3, 4, 6);
        FarkleGame multi = runGame("bad\n-1\n\n0\n2\nCedar\n\nq\n", 6,
                1, 2, 2, 3, 4, 6);
        check(single.getTargetScore() == 10000
                && single.getPlayer().getName().equals("Unknown Player")
                && multi.getPlayers().size() == 2
                && multi.getPlayers().get(0).getName().equals("Cedar")
                && multi.getPlayers().get(1).getName().equals("Willow"), "Setup defaults");
    }

    // Tests banking across turns and losing only the current turn on a Farkle. 
    private static void testMultipleTurns() {
        FarkleGame game = runGame("200\n1\nSam\nAK\nAR\nAK\n", 23,
                1, 2, 2, 3, 4, 6,
                1, 2, 2, 3, 4, 6, 2, 2, 3, 4, 6,
                1, 2, 2, 3, 4, 6);
        check(game.getPlayer().getScore() == 200 && game.getWinner() == game.getPlayer(),
                "Banked points survive a later Farkle");
    }

    // Tests a middle player triggering the final round and another player winning. 
    private static void testFinalRound() {
        FarkleGame game = runGame("200\n3\nAda\nBea\nCy\nAK\nABK\nABCDEFK\nAK\n",
                24, 1, 2, 2, 3, 4, 6,
                1, 1, 2, 3, 4, 6,
                2, 2, 4, 4, 6, 6,
                1, 2, 2, 3, 4, 6);
        check(game.getWinner().getName().equals("Cy")
                && game.getPlayers().get(0).getScore() == 200
                && game.getPlayers().get(1).getScore() == 200
                && game.getPlayers().get(2).getScore() == 750,
                "Each opponent gets exactly one final turn");
    }

    // Tests that matching the leader is insufficient to take the lead. 
    private static void testFinalRoundTie() {
        FarkleGame game = runGame("100\n2\nAda\nBea\nAK\nAK\n", 12,
                1, 2, 2, 3, 4, 6, 1, 2, 2, 3, 4, 6);
        check(game.getWinner().getName().equals("Ada")
                && game.getPlayers().get(1).getScore() == 100, "Final round tie");
    }

    // Tests quitting during the final round without banking or declaring a winner. 
    private static void testQuitGame() {
        FarkleGame game = runGame("100\n2\nAda\nBea\nAK\nAQ\nAK\n", 12,
                1, 2, 2, 3, 4, 6, 1, 2, 2, 3, 4, 6);
        check(game.getWinner() == null && game.getPlayer().getScore() == 100
                && game.getPlayers().get(1).getScore() == 0, "Quit ends the entire game");
    }

    // Runs a game using predetermined input and dice.
    private static FarkleGame runGame(String commands,
            int expectedRolls, int... faces) {
        ScriptedRandom random = new ScriptedRandom(faces);
        Scanner input = new Scanner(commands);
        FarkleGame game = new FarkleGame(input, random);

        game.play();

        if (random.rolls != expectedRolls) {
            throw new AssertionError("Unexpected number of dice rolled");
        }

        return game;
    }

    // Supplies predetermined dice for repeatable tests. 
    private static class ScriptedRandom extends Random {
        private static final long serialVersionUID = 1L;
        private final int[] faces;
        private int rolls;

        // Stores the dice to use.
        private ScriptedRandom(int... faces) {
            this.faces = faces;
        }

        // Gives the next predetermined die result.
        @Override
        public int nextInt(int bound) {
            if (rolls >= faces.length) {
                throw new AssertionError("More dice rolled than expected");
            }

            return faces[rolls++] - 1;
        }
    }

    // Checks whether a test passed.
    private static void check(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError("FAILED: " + name);
        }

        testsPassed++;
        System.out.println("PASSED: " + name);
    }
}
