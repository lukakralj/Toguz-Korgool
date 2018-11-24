import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class that contains tests for Board.java.
 *
 * @author Luka Kralj
 * @version 12 November 2018
 */
public class BoardTest {

    /**
     * Test if all fields in board were initialised correctly. This method assumes that the default starting
     * position is used.
     */
    @Test
    public void testIfAllFieldsInitialisedCorrectly() {
        Board board = new Board();
        assertEquals("White kazan initialised as: " + board.getWhitePlayer().getKazan() + " - incorrect.", 0, board.getWhitePlayer().getKazan());
        assertEquals("Black kazan initialised as: " + board.getBlackPlayer().getKazan() + " - incorrect.", 0, board.getBlackPlayer().getKazan() );
        assertEquals("White tuz initialised as: " + board.getWhitePlayer().getTuz() + " - incorrect.", -1, board.getWhitePlayer().getTuz());
        assertEquals("Black tuz initialised as: " + board.getBlackPlayer().getTuz() + " - incorrect.", -1, board.getBlackPlayer().getTuz());

        for (int i : board.getWhitePlayer().getHoles()) {
            assertEquals("White holes initialised incorrectly.", 9, i);
        }

        for (int i : board.getBlackPlayer().getHoles()) {
            assertEquals("Black holes initialised incorrectly.", 9, i);
        }

        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());
    }

    /**
     * This method tests if some of the moves were successful.
     *
     * TODO: add more edge case calls???
     */
    @Test
    public void testMovesThatShouldReturnSUCCESSFUL() {
        Board board = new Board();
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(6, board.getWhitePlayer(), board.getBlackPlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getBlackPlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(8, board.getBlackPlayer(), board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getBlackPlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(8, board.getBlackPlayer(), board.getWhitePlayer()));

        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());
    }

    /**
     * This method tests if making a move by selecting an empty hole would return a value that
     * means the move was rejected (was unsuccessful).
     */
    @Test
    public void testMovesThatShouldReturnUNSUCCESSFUL() {
        Board board = new Board();
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.MOVE_UNSUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.MOVE_UNSUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(7, board.getBlackPlayer(), board.getWhitePlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getBlackPlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(7, board.getBlackPlayer(), board.getWhitePlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getBlackPlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.MOVE_UNSUCCESSFUL, board.makeMove(7, board.getBlackPlayer(), board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.MOVE_UNSUCCESSFUL, board.makeMove(7, board.getBlackPlayer(), board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.SUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));
        assertTrue("ifMovePossible returned wrong value", board.testCheckIfMovePossible(board.getWhitePlayer()));
        assertEquals("Make move returned wrong value.", BoardStatus.MOVE_UNSUCCESSFUL, board.makeMove(0, board.getWhitePlayer(), board.getBlackPlayer()));

        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());
    }

    /**
     * Test if the board is in correct state after the black player has claimed their tuz.
     */
    @Test
    public void testIfStateOfBoardIsCorrectWhenBlackPlayerClaimsTuz() {
        Board board = setupBoardBlackTuz();
        // Board should be:
        //Black:  12 12 2 12  1 2 10  1 11
        //White:  12 12 1 13 12 0  0 11 11
        //kazanB: 17
        //kazanW: 10
        // tuzB: 5
        // tuzW: -1
        assertEquals("TuzW is incorrect (in black tuz test)", -1, board.getWhitePlayer().getTuz());
        assertEquals("TuzB is incorrect (in black tuz test)", 5, board.getBlackPlayer().getTuz());
        assertEquals("KazanW is incorrect (in black tuz test)", 10, board.getWhitePlayer().getKazan());
        assertEquals("KazanB is incorrect (in black tuz test)", 17, board.getBlackPlayer().getKazan());

        int[] blackHolesShouldBe = new int[]{11,1,10,2,1,12,2,12,12};
        int[] whiteHolesShouldBe = new int[]{12,12,1,13,12,0,0,11,11};
        for (int i = 0; i < 9; i++) {
            assertEquals("Black holes incorrect (in black tuz test).", blackHolesShouldBe[i], board.getBlackPlayer().getHoles()[i]);
            assertEquals("White holes incorrect (in black tuz test).", whiteHolesShouldBe[i], board.getWhitePlayer().getHoles()[i]);
        }
        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());
    }

    /**
     * This method test if the board behaves correctly when the black player tries to change
     * their tuz (by finishing the move with 3 korgools on white side).
     */
    @Test
    public void testIfChangingTuzIsRejected() {
        Board board = setupBoardAttemptSecondBlackTuz();
        // Board should be:
        //Black:  16 17 7 17  1 0 1  3 0
        //White:   3 17 0 18 15 0 1 12 0
        //kazanB: 22
        //kazanW: 12
        // tuzB: 5
        // tuzW: -1

        assertEquals("TuzW is incorrect (in second black tuz test)", -1, board.getWhitePlayer().getTuz());
        assertEquals("TuzB is incorrect (in second black tuz test)", 5, board.getBlackPlayer().getTuz());
        assertEquals("KazanW is incorrect (in second black tuz test)", 12, board.getWhitePlayer().getKazan());
        assertEquals("KazanB is incorrect (in second black tuz test)", 22, board.getBlackPlayer().getKazan());

        int[] blackHolesShouldBe = new int[]{0,3,1,0,1,17,7,17,16};
        int[] whiteHolesShouldBe = new int[]{3,17,0,18,15,0,1,12,0};
        for (int i = 0; i < 9; i++) {
            assertEquals("Black holes incorrect (in second black tuz test).", blackHolesShouldBe[i], board.getBlackPlayer().getHoles()[i]);
            assertEquals("White holes incorrect (in second black tuz test).", whiteHolesShouldBe[i], board.getWhitePlayer().getHoles()[i]);
        }
        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());
    }

    /**
     * Test if endMove behaves correctly during the game (when game is not finished yet).
     */
    @Test
    public void testEndMove() {
        Board board = setupBoardAttemptSecondBlackTuz();
        // The board is now:
        //Black:  16 17 7 17  1 0 1  3 0
        //White:   3 17 0 18 15 0 1 12 0
        //kazanB: 22
        //kazanW: 12
        // tuzB: 5
        // tuzW: -1

        assertEquals("endMove didn't return correct result.", BoardStatus.SUCCESSFUL, board.testEndMove(0, board.getWhitePlayer(), board.getBlackPlayer(), board.getWhitePlayer()));
        assertEquals("tuz was changed", -1, board.getWhitePlayer().getTuz());

        assertEquals("endMove didn't return correct result.", BoardStatus.SUCCESSFUL, board.testEndMove(1, board.getWhitePlayer(), board.getBlackPlayer(), board.getBlackPlayer()));
        assertEquals("tuz was not changed", 1, board.getWhitePlayer().getTuz());
        assertEquals("New tuz was not emptied.", 0, board.getBlackPlayer().getHoles()[1]);
        assertEquals("Score was not updated.", 15, board.getWhitePlayer().getKazan());
        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());
        // The board is now:
        //Black:  16 17 7 17  1 0 1  0 0
        //White:   3 17 0 18 15 0 1 12 0
        //kazanB: 22
        //kazanW: 15
        // tuzB: 5
        // tuzW: 1

        board.makeMove(7, board.getWhitePlayer(), board.getBlackPlayer());
        // The board is now:
        //Black:  17 18 8 18  2 1 2 0 1
        //White:   4 17 0 18 15 0 1 1 1
        //kazanB: 22
        //kazanW: 16
        // tuzB: 5
        // tuzW: 1
        assertEquals("endMove didn't return correct result.", BoardStatus.SUCCESSFUL, board.testEndMove(0, board.getWhitePlayer(), board.getBlackPlayer(), board.getWhitePlayer()));
        assertEquals("tuz was changed", 1, board.getWhitePlayer().getTuz());
        assertEquals("Tuz was not emptied.", 0, board.getBlackPlayer().getHoles()[1]);
        assertEquals("Score was not updated.", 16, board.getWhitePlayer().getKazan());
        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());
    }

    /**
     * Tests if an almost finished board is in correct state.
     */
    @Test
    public void testStateOfAlmostFinishedBoard() {
        Board board = setupAlmostFinishedGameBoard();
        // Board is now:
        //Black:  7 4 3 2 4 3 4 0 0
        //White:  1 0 0 4 2 0 2 2 0
        //kazanB: 74
        //kazanW: 50
        //tuzB: 5
        //tuzW: 1

        assertEquals("TuzW is incorrect", 0, board.getWhitePlayer().getTuz());
        assertEquals("TuzB is incorrect", 5, board.getBlackPlayer().getTuz());
        assertEquals("KazanW is incorrect", 50, board.getWhitePlayer().getKazan());
        assertEquals("KazanB is incorrect", 74, board.getBlackPlayer().getKazan());

        int[] blackHolesShouldBe = new int[]{0,0,4,3,4,2,3,4,7};
        int[] whiteHolesShouldBe = new int[]{1,0,0,4,2,0,2,2,0};
        for (int i = 0; i < 9; i++) {
            assertEquals("Black holes incorrect", blackHolesShouldBe[i], board.getBlackPlayer().getHoles()[i]);
            assertEquals("White holes incorrect", whiteHolesShouldBe[i], board.getWhitePlayer().getHoles()[i]);
        }
        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());

    }

    /**
     * Test if the board behaves correctly when the black player wins.
     */
    @Test
    public void testBlackPlayerWinning() {
        Board board = setupAlmostFinishedGameBoard();
        // Board is now:
        //Black:  7 4 3 2 4 3 4 0 0
        //White:  1 0 0 4 2 0 2 2 0
        //kazanB: 74
        //kazanW: 50
        //tuzB: 5
        //tuzW: 1

        assertEquals("TuzW is incorrect", 0, board.getWhitePlayer().getTuz());
        assertEquals("TuzB is incorrect", 5, board.getBlackPlayer().getTuz());
        assertEquals("KazanW is incorrect", 50, board.getWhitePlayer().getKazan());
        assertEquals("KazanB is incorrect", 74, board.getBlackPlayer().getKazan());

        int[] blackHolesShouldBe = new int[]{0,0,4,3,4,2,3,4,7};
        int[] whiteHolesShouldBe = new int[]{1,0,0,4,2,0,2,2,0};
        for (int i = 0; i < 9; i++) {
            assertEquals("Black holes incorrect", blackHolesShouldBe[i], board.getBlackPlayer().getHoles()[i]);
            assertEquals("White holes incorrect", whiteHolesShouldBe[i], board.getWhitePlayer().getHoles()[i]);
        }
        testNumberOfKorgools(board);
        assertEquals("checkResult didn't return the correct value.", BoardStatus.SUCCESSFUL, board.testCheckResult());


        board.makeMove(3, board.getWhitePlayer(), board.getBlackPlayer());
        //Black:  7 4 3 2 4 3 4 0 0
        //White:  1 0 0 1 3 0 3 2 0
        //kazanB: 75
        //kazanW: 50

        board.makeMove(8, board.getBlackPlayer(), board.getWhitePlayer());
        //Black:  1 4 3 2 4 3 4 0 0
        //White:  2 1 1 2 4 0 3 2 0
        //kazanB: 76
        //kazanW: 50

        board.makeMove(4, board.getWhitePlayer(), board.getBlackPlayer());
        //Black:  1 4 3 2 4 3 4 0 0
        //White:  2 1 1 2 1 0 4 3 0
        //kazanB: 77
        //kazanW: 50

        board.makeMove(7, board.getBlackPlayer(), board.getWhitePlayer());
        //Black:  2 1 3 2 4 3 4 0 0
        //White:  3 0 1 2 1 0 4 3 0
        //kazanB: 79
        //kazanW: 50

        board.makeMove(7, board.getWhitePlayer(), board.getBlackPlayer());
        //Black:  2 1 3 2 4 3 4 0 0
        //White:  3 0 1 2 1 0 4 1 1
        //kazanB: 79
        //kazanW: 51

        assertEquals("black player should have won", BoardStatus.B_WON, board.makeMove(8, board.getBlackPlayer(), board.getWhitePlayer()));
        //Black:  1 1 3 2 4 3 4 0 0
        //White:  0 0 1 2 1 0 4 1 1
        //kazanB: 83
        //kazanW: 51

        // ND: Game end here.
        assertEquals("black player should have won", BoardStatus.B_WON, board.testEndMove(0, board.getWhitePlayer(), board.getBlackPlayer(),board.getBlackPlayer()));
        assertEquals("checkResult didn't return the correct value", BoardStatus.B_WON, board.testCheckResult());

        assertEquals("TuzW is incorrect", 0, board.getWhitePlayer().getTuz());
        assertEquals("TuzB is incorrect", 5, board.getBlackPlayer().getTuz());
        assertEquals("KazanW is incorrect", 51, board.getWhitePlayer().getKazan());
        assertEquals("KazanB is incorrect", 83, board.getBlackPlayer().getKazan());

        int[] blackHolesShouldBe2 = new int[]{0,0,4,3,4,2,3,1,1};
        int[] whiteHolesShouldBe2 = new int[]{0,0,1,2,1,0,4,1,1};
        for (int i = 0; i < 9; i++) {
            assertEquals("Black holes incorrect", blackHolesShouldBe2[i], board.getBlackPlayer().getHoles()[i]);
            assertEquals("White holes incorrect", whiteHolesShouldBe2[i], board.getWhitePlayer().getHoles()[i]);
        }
        testNumberOfKorgools(board);

    }

    // =====================
    //    Helper methods
    // =====================

    /**
     * This method tests if number of all korgools on board is correct (162 in total).
     *
     * @param board Board to check.
     */
    private void testNumberOfKorgools(Board board) {
        int all = 0;
        all += board.getBlackPlayer().getKazan();
        all += board.getWhitePlayer().getKazan();
        for (int i : board.getWhitePlayer().getHoles()) {
            all += i;
        }
        for (int i : board.getBlackPlayer().getHoles()) {
            all += i;
        }
        assertEquals("Number of all korgools on board is " + all + " (but should be 162).", 162, all);
    }

    /**
     * Creates a new board and calls some moves on it to simulate a situation that could happen
     * during the game. It return the board as it is right after the black player has claimed a tuz.
     *
     * @return A board as it could appear during the game.
     */
    private Board setupBoardBlackTuz() {
        Board toReturn = new Board();
        //Black:  9 9 9 9 9 9 9 9 9
        //White:  9 9 9 9 9 9 9 9 9
        //kazanB: 0
        //kazanW: 0

        toReturn.makeMove(2, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  9 9 9  9  9  9  9  0 10
        //White:  9 9 1 10 10 10 10 10 10
        //kazanB: 0
        //kazanW: 10

        toReturn.makeMove(3, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  10 10 10 10 10  1  9  0 10
        //White:  10 10  0 10 10 10 10 10 10
        //kazanB: 2
        //kazanW: 10

        toReturn.makeMove(5, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  10 10 10 11 11  2 10  1 11
        //White:  10 10  0 10 10  1 11 11 11
        //kazanB: 2
        //kazanW: 10

        toReturn.makeMove(6, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  11 11 1 11 11 2 10  1 11
        //White:  11 11 1 11 11 2  0 11 11
        //kazanB: 14
        //kazanW: 10

        toReturn.makeMove(2, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  11 11 1 11 11 2 10  1 11
        //White:  11 11 0 12 11 2  0 11 11
        //kazanB: 14
        //kazanW: 10

        assertTrue("ifMovePossible returned wrong value", toReturn.testCheckIfMovePossible(toReturn.getBlackPlayer()));
        toReturn.makeMove(4, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  12 12 2 12  1 2 10  1 11
        //White:  12 12 1 13 12 0  0 11 11
        //kazanB: 17
        //kazanW: 10
        // tuzB: 5
        return toReturn;
    }

    /**
     * Creates a new board and calls some moves on it to simulate a situation that could happen
     * during the game. It returns the board as it is right after the black player tries to
     * change their tuz by finishing their move with 3 korgools on white side.
     *
     * @return A board as it could appear during the game.
     */
    private Board setupBoardAttemptSecondBlackTuz() {
        Board toReturn = setupBoardBlackTuz();
        //Black:  12 12 2 12  1 2 10  1 11
        //White:  12 12 1 13 12 0  0 11 11
        //kazanB: 17
        //kazanW: 10
        // tuzB: 5

        assertTrue("ifMovePossible returned wrong value", toReturn.testCheckIfMovePossible(toReturn.getWhitePlayer()));
        toReturn.makeMove(0, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  12 12 2 12  1 2 11  2 12
        //White:   1 13 2 14 13 0  1 12 12
        //kazanB: 18
        //kazanW: 10
        // tuzB: 5

        toReturn.makeMove(1, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  12 12 2 12  1 2 12  1 12
        //White:   1 13 2 14 13 0  1 12 12
        //kazanB: 18
        //kazanW: 10
        // tuzB: 5

        toReturn.makeMove(8, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  13 13 3 13  2 3 13  2 13
        //White:   2 14 2 14 13 0  1 12  1
        //kazanB: 18
        //kazanW: 10
        // tuzB: 5

        assertTrue("ifMovePossible returned wrong value", toReturn.testCheckIfMovePossible(toReturn.getBlackPlayer()));
        toReturn.makeMove(0, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  14 14 4 14  3 4 14  3 1
        //White:   3 15 3 15 13 0  1 12 1
        //kazanB: 18
        //kazanW: 10
        // tuzB: 5

        toReturn.makeMove(0, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  14 14 4 14  3 4 14  3 1
        //White:   1 16 4 15 13 0  1 12 1
        //kazanB: 18
        //kazanW: 10
        // tuzB: 5

        toReturn.makeMove(2, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  15 15 5 15  4 5 1  3 1
        //White:   2 17 5 16 14 0 0 12 1
        //kazanB: 21
        //kazanW: 10
        // tuzB: 5

        toReturn.makeMove(8, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  15 15 5 15  4 5 1  3 0
        //White:   2 17 5 16 14 0 0 12 0
        //kazanB: 21
        //kazanW: 12
        // tuzB: 5

        toReturn.makeMove(3, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  15 16 6 16  5 1 1  3 0
        //White:   2 17 5 16 14 0 0 12 0
        //kazanB: 21
        //kazanW: 12
        // tuzB: 5

        toReturn.makeMove(2, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  15 16 6 16  5 1 1  3 0
        //White:   2 17 1 17 15 0 1 12 0
        //kazanB: 22
        //kazanW: 12
        // tuzB: 5

        toReturn.makeMove(3, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  15 16 6 16  6 0 1  3 0
        //White:   2 17 1 17 15 0 1 12 0
        //kazanB: 22
        //kazanW: 12
        // tuzB: 5

        toReturn.makeMove(2, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  15 16 6 16  6 0 1  3 0
        //White:   2 17 0 18 15 0 1 12 0
        //kazanB: 22
        //kazanW: 12
        // tuzB: 5

        assertTrue("ifMovePossible returned wrong value", toReturn.testCheckIfMovePossible(toReturn.getBlackPlayer()));
        toReturn.makeMove(4, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  16 17 7 17  1 0 1  3 0
        //White:   3 17 0 18 15 0 1 12 0
        //kazanB: 22
        //kazanW: 12
        // tuzB: 5

        // NB: At this point black tuz should still be 5 and should not change to 0!!!

        return toReturn;
    }

    /**
     * Creates a new board and calls some moves on it to simulate a situation that could happen
     * during the game - towards the end of the game.
     *
     * @return A board as it could appear during the game.
     */
    private Board setupAlmostFinishedGameBoard() {
        Board toReturn = setupBoardAttemptSecondBlackTuz();
        // Board is now:
        //Black:  16 17 7 17  1 0 1  3 0
        //White:   3 17 0 18 15 0 1 12 0
        //kazanB: 22
        //kazanW: 12
        //tuzB: 5
        //tuzW: -1

        toReturn.makeMove(3, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  17 18 8 18 2 1 2 4 1
        //White:  4 18 1 1 16 0 2 13 1
        //kazanB: 23
        //kazanW: 12
        //tuzB: 5
        //tuzW: -1

        toReturn.makeMove(4, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  17 18 8 19 1 1 2 4 1
        //White:  4 18 1 1 16 0 2 13 1
        //kazanB: 23
        //kazanW: 12
        //tuzB: 5
        //tuzW: -1

        toReturn.makeMove(4, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  18 19 9 20 2 2 3 5 2
        //White:  5 19 1 1 1 0 3 14 2
        //kazanB: 24
        //kazanW: 12
        //tuzB: 5
        //tuzW: -1

        toReturn.makeMove(4, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  18 19 9 21 1 2 3 5 2
        //White:  5 19 1 1 1 0 3 14 2
        //kazanB: 24
        //kazanW: 12
        //tuzB: 5
        //tuzW: -1

        toReturn.makeMove(8, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  18 19 9 21 1 2 3 5 0
        //White:  5 19 1 1 1 0 3 14 1
        //kazanB: 24
        //kazanW: 15
        //tuzB: 5
        //tuzW: 0

        // NB: They both have tuz now!

        toReturn.makeMove(5, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  19 21 11 2 2 3 4 6 0
        //White:  6 20 2 2 2 0 4 15 2
        //kazanB: 25
        //kazanW: 16
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(6, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  19 21 11 2 2 3 4 6 0
        //White:  6 20 2 2 2 0 1 16 3
        //kazanB: 25
        //kazanW: 17
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(7, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  21 2 12 3 3 4 5 7 0
        //White:  0 21 3 3 3 0 2 17 4
        //kazanB: 34
        //kazanW: 18
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(2, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  21 2 12 3 3 4 5 7 0
        //White:  0 21 1 4 4 0 2 17 4
        //kazanB: 34
        //kazanW: 18
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(1, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  21 3 13 4 4 5 6 1 0
        //White:  0 21 1 4 4 0 2 17 4
        //kazanB: 34
        //kazanW: 18
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(7, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  22 4 14 5 5 6 7 2 0
        //White:  1 22 2 5 5 0 2 1 5
        //kazanB: 35
        //kazanW: 19
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(5, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  23 5 15 1 5 6 7 2 0
        //White:  0 22 2 5 5 0 2 1 5
        //kazanB: 37
        //kazanW: 19
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(1, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  24 6 16 2 6 7 8 3 0
        //White:  1 2 4 7 7 0 3 2 6
        //kazanB: 38
        //kazanW: 20
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(7, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  25 1 16 2 6 7 8 3 0
        //White:  2 3 5 0 7 0 3 2 6
        //kazanB: 46
        //kazanW: 20
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(8, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  25 1 16 2 7 8 9 4 0
        //White:  2 3 5 0 7 0 3 2 1
        //kazanB: 46
        //kazanW: 21
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(8, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  2 2 17 3 8 9 10 5 0
        //White:  4 5 7 2 9 0 4 3 2
        //kazanB: 48
        //kazanW: 22
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(4, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  2 2 17 3 8 0 11 6 0
        //White:  4 5 7 2 1 0 5 4 3
        //kazanB: 49
        //kazanW: 33
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(4, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  3 3 18 4 1 0 11 6 0
        //White:  5 6 0 2 1 0 5 4 3
        //kazanB: 57
        //kazanW: 33
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(6, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  3 3 18 4 1 0 11 7 0
        //White:  5 6 0 2 1 0 1 5 4
        //kazanB: 57
        //kazanW: 34
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(7, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  4 1 18 4 1 0 11 7 0
        //White:  0 6 0 2 1 0 1 5 4
        //kazanB: 63
        //kazanW: 34
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(7, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  4 1 18 4 1 0 0 8 0
        //White:  0 6 0 2 1 0 1 1 5
        //kazanB: 63
        //kazanW: 47
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(6, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  5 2 1 5 2 1 1 9 0
        //White:  1 7 1 3 2 0 2 2 6
        //kazanB: 64
        //kazanW: 48
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(8, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  5 2 1 5 3 2 2 10 0
        //White:  1 7 1 3 2 0 2 2 1
        //kazanB: 64
        //kazanW: 49
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(5, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  6 3 2 1 3 2 2 10 0
        //White:  0 7 1 3 2 0 2 2 1
        //kazanB: 66
        //kazanW: 49
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(8, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  6 3 2 1 3 2 2 10 0
        //White:  0 7 1 3 2 0 2 2 0
        //kazanB: 66
        //kazanW: 50
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(1, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  7 4 3 2 4 3 3 1 0
        //White:  1 0 1 3 2 0 2 2 0
        //kazanB: 74
        //kazanW: 50
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(2, toReturn.getWhitePlayer(), toReturn.getBlackPlayer());
        //Black:  7 4 3 2 4 3 3 1 0
        //White:  1 0 0 4 2 0 2 2 0
        //kazanB: 74
        //kazanW: 50
        //tuzB: 5
        //tuzW: 0

        toReturn.makeMove(1, toReturn.getBlackPlayer(), toReturn.getWhitePlayer());
        //Black:  7 4 3 2 4 3 4 0 0
        //White:  1 0 0 4 2 0 2 2 0
        //kazanB: 74
        //kazanW: 50
        //tuzB: 5
        //tuzW: 0

        return toReturn;
    }
}
