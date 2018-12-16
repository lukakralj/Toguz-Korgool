import static junit.framework.TestCase.assertTrue;
import static logic.BoardStatus.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import gui.GameWindow;
import gui.Hole;
import logic.AnimationController;
import logic.BoardStatus;
import logic.GameManager;
import logic.Player;
import org.junit.Before;
import org.junit.Test;
import com.athaydes.automaton.Swinger;
import com.athaydes.automaton.Speed;

import java.util.HashMap;


/**
 * Class that contains tests for the Game Manager - tests if changes in GUI reflect changes in logic
 *
 * @author Kayla Phillips Sanchez, Karolina Szafranek
 * @version 26 November 2018
 */

public class GameManagerTest {

    private GameManager gameManager;
    private GameWindow gameWindow;
    private Swinger swinger;

    private int[] movesW = {1,2,4,5,3,5,6,4,5,6,9,8,7,1,8,9,1,9,4,5,3,8,8,6,7,9,9,4,3,4,3,4,5,6,7,8,9,1,4, 8, 9};
    private int[][] expectedKazans ={{0,0}, {10,0}, {14,3}, {15,4}, {16,4}, {17,21}, {18,30}, {18,30}, {19,31}, {19,31}, {21,33}, {40,34}, {41,44}, {41,45}, {43,45}, {45,45}, {46,46}, {49,48}, {49,48}, {49,48}, {50,51}, {52,52}, {53,53}, {55,59}, {65,59}, {69,59},{69,59},{69,59},{70,60}, {70,60}, {71,60}, {71,60},{75,60}, {75,61}, {75,61}, {77,65}, {80,65}, {80,66}, {80,66}, {80,66}, {82,66} };
    private int[][] expectedHolesW = {{2,11,11,11,11,11,10,10,10}, {3,2,13,13,13,12,11,11,11}, {4,0,13,1,14,13,12,12,12}, {5,0,14,2,2,15,14,14,14}, {5,0,1,3,3,16,15,15,15}, {6,0,2,4,2,18,17,0,15}, {0,0,3,5,3,1,18,1,16}, {0,0,3,1,4,2,19,2,16}, {1,0,4,2,2,4,21,4,17},{1,0,4,2,2,1,22,5,18}, {3,0,6,4,4,3,23,6,1}, {4,0,6,4,4,3,23,1,2}, {6,0,0,5,5,4,2,3,4}, {1,0,1,6,6,5,2,3,4}, {1,0,1,6,6,5,2,1,5}, {1,0,1,6,6,5,2,1,1}, {0,0,1,6,6,5,2,1,1}, {2,0,3,8,8,7,3,2,1}, {2,0,3,1,9,8,4,3,2}, {2,0,3,1,1,9,5,4,3}, {3,0,0,2,2,9,5,4,3}, {4,0,1,2,2,9,5,1,4}, {5,0,2,3,3,9,5,0,5}, {0,0,2,3,3,1,6,1,6}, {0,0,2,3,3,1,1,2,7}, {0,0,2,3,3,1,1,2,1}, {0,0,2,3,3,1,1,2,0},{1,0,2,1,4,2,1,2,0}, {2,0,1,2,4,2,1,2,0}, {2,0,1,1,5,2,1,2,0}, {2,0,0,2,5,2,1,2,0},  {2,0,0,1,6,2,1,2,0},  {2,0,0,1,1,3,2,3,1}, {3,0,0,1,1,1,3,4,1}, {3,0,0,1,1,1,1,5,2}, {0,0,0,1,1,1,1,1,3}, {1,0,0,1,1,1,1,1,1}, {0,0,0,1,1,1,1,1,1}, {0,0,0,0,2,1,1,1,1}, {1,0,0,0,2,1,1,0,2}, {1,0,0,0,2,1,1,0,1}};
    private int[][] expectedHolesB = {{10,10,1,9,9,9,9,9,9}, {11,11,2,1,9,9,0,10,10}, {12,12,0,3,11,11,2,12,1}, {14,1,0,4,12,12,3,14,3}, {14,1,0,5,13,14,5,16,1}, {15,2,0,6,1,14,5,16,1}, {17,1,0,7,2,15,6,17,2}, {17,1,0,8,1,15,6,17,2}, {18,2,0,9,2,16,7,1,2}, {19,1,0,9,2,16,7,1,2}, {21,3,0,1,3,17,8,2,3}, {22,4,0,2,4,1,1,3,4}, {24,1,0,3,5,2,2,5,6}, {24,1,0,4,6,3,3,6,1}, {24,1,0,5,7,1,3,6,0}, {25,0,0,5,7,0,4,7,1}, {25,1,0,6,8,1,5,1,1}, {2,2,0,7,9,2,6,2,1}, {3,1,0,7,9,2,6,3,2}, {3,1,0,7,10,4,8,1,3}, {4,2,0,1,10,4,8,1,3}, {1,2,0,1,10,4,8,0,4}, {2,3,0,2,1,4,8,0,4}, {1,3,0,2,0,5,9,1,5}, {1,3,0,3,1,6,1,3,1}, {1,3,0,1,1,7,2,4,2}, {1,3,0,1,2,8,3,1,3},{0,3,0,1,2,8,3,1,3}, {1,4,0,2,3,1,3,1,3}, {1,4,0,2,3,1,4,0,3}, {1,4,0,3,1,1,4,0,3},  {1,4,0,4,0,1,4,0,3}, {1,4,0,4,1,0,4,0,0}, {2,1,0,4,1,0,4,0,0}, {2,1,0,5,2,1,1,0,0}, {1,1,0,5,2,1,0,1,1}, {2,2,0,1,2,1,0,0,2}, {2,2,0,1,3,0,0,0,2}, {2,2,0,1,3,0,0,1,1}, {1,2,0,1,3,0,0,1,1}, {1,2,0,1,3,0,0,1,0}};

    private int[] whiteWinsSetupW = {1,0,0,2,0,2,1,5,0};
    private int[] whiteWinsSetupB = {0,1,3,0,0,0,3,1,0};
    private int[] whiteWinsSetupKazans = {78, 65};

    private int[] blackWinsSetupW = {3,2,1,0,0,3,0,3,4};
    private int[] blackWinsSetupB = {0,0,0,0,1,2,4,0,0};
    private int[] blackWinsSetupKazans = {60, 79};

    private int[] drawSetupW = {0,0,0,0,0,0,0,0,1};
    private int[] drawSetupB = {1,0,0,0,0,0,0,0,0};
    private int[] drawSetupKazans = {79, 81};

    private int[] moveImpossibleBSetupW = {0,0,0,0,0,0,0,0,0};
    private int[] moveImpossibleBSetupB = {9,9,9,9,9,9,9,9,9};
    private int[] moveImpossibleBSetupKazans = {40, 41};

    private int[] moveImpossibleWSetupW = {9,8,7,6,5,4,3,2,0};
    private int[] moveImpossibleWSetupB = {0,0,0,0,0,0,0,0,0};
    private int[] moveImpossibleWSetupKazans = {59, 59};


    @Before
    public void openWindow() {
        gameManager = new GameManager();
        gameWindow = gameManager.getWindow();
        AnimationController.setRunTime(1);
        swinger = Swinger.getUserWith(gameWindow);
        Swinger.setDEFAULT(Speed.VERY_FAST);
        swinger.pause(200);
    }

    /**
     * Test that GUI and Core are set up correctly at the start of the game
     */
    @Test
    public void testSetUp() {
        Player white =  gameManager.getCore().getWhitePlayer();
        Player black = gameManager.getCore().getBlackPlayer();


        // test holes setup
        int[] expected = {9,9,9,9,9,9,9,9,9};
        compareHoles(expected, getWhiteArray(), white.getHoles());
        compareHoles(expected, getBlackArray(), black.getHoles());

        // test tuzes setup
        HashMap<String, Hole> holes = gameWindow.getButtonMap();
        for (String hole : holes.keySet()) {
            if (holes.get(hole).isTuz()) {
                if (hole.startsWith("W")) {
                    int index = Integer.parseInt(hole.substring(1)) - 1;
                    assertEquals(index, white.getTuz());
                } else {
                    int index = 9 - Integer.parseInt(hole.substring(1));
                    assertEquals(index, black.getTuz());
                }
            }
        }

        // test kazans setup
        compare(0, gameWindow.getKazanLeft().getNumberOfKorgools(), white.getKazan());
        compare(0, gameWindow.getKazanRight().getNumberOfKorgools(), black.getKazan());


    }

    /**
     * Test that predefined move sequence results in correct changes to both the GUI and the Core
     */
    @Test
    public void testSingleGame() {
        gameManager.setRandomSeed();
        swinger.pause(1000);

        for (int i = 0; i < movesW.length; i++) {
            swinger.clickOn("name:W" + movesW[i]).pause(2000);
            compareAll(i, expectedKazans[i][0], expectedKazans[i][1]);
            if (i > 1) {
                compareTuzes();
            }
        }
    }

    /**
     * Test scenario when white player wins
     */
    @Test
    public void testWhiteWins() {
        testEndGame(8, "White player won!", W_WON, whiteWinsSetupW, whiteWinsSetupB, whiteWinsSetupKazans[0], whiteWinsSetupKazans[1]);
    }

    /**
     * Test draw scenario
     */
    @Test
    public void testDraw() {
        testEndGame(9, "It's a tie!", DRAW, drawSetupW, drawSetupB, drawSetupKazans[0], drawSetupKazans[1]);
    }

    @Test
    public void testBlackWins() {
        gameManager.setRandomSeed();
        testEndGame(8, "Black player won!", B_WON, blackWinsSetupW, blackWinsSetupB, blackWinsSetupKazans[0], blackWinsSetupKazans[1]);
    }

    @Test
    public void testBlackWinsAfterMoveImpossible() {
        testEndGame(9, "Black player won!", B_WON, moveImpossibleBSetupW, moveImpossibleBSetupB, moveImpossibleBSetupKazans[0], moveImpossibleBSetupKazans[1]);
    }

    @Test
    public void testWhiteWinsAfterMoveImpossible() {
        testEndGame(8, "White player won!", W_WON, moveImpossibleWSetupW, moveImpossibleWSetupB, moveImpossibleWSetupKazans[0], moveImpossibleWSetupKazans[1]);
    }

    /**
     * Helper function checking state after making the desired move.
     * Uses custom input to set up the board before the move.
     * @param moveW the move white player will make
     * @param message end message that should be displayed
     * @param endStatus end status that board should have
     * @param holesW white player's hole values just before the move
     * @param holesB black player's hole values just before the move
     * @param kazanW white player's kazan value just before the move
     * @param kazanB black player's kazan value just before the move
     */
    private void testEndGame(int moveW, String message, BoardStatus endStatus, int[] holesW, int[] holesB, int kazanW, int kazanB) {
        gameManager.populateInitialBoard(holesW, holesB, 3, 2, kazanW, kazanB);
        swinger.pause(500);
        swinger.clickOn("name:W" + moveW).pause(1000);
        assertEquals(endStatus, gameManager.getCore().testCheckResult());
        assertEquals("<html><div style='text-align: center; color: white; -webkit-text-stroke-width: 1px;'>" + message + "</div></html>", gameWindow.getMessage());
    }

    /**
     * Get hole values on white player's board in GUI in the form of an array
     * @return white player's whole values
     */
    private int[] getWhiteArray() {
        int[] w = new int[gameWindow.getButtonMap().size()/2];
        for (int i=1; i <= gameWindow.getButtonMap().size()/2; i++) {
            Hole hole = gameWindow.getButtonMap().get("W" + i);
            w[i-1] = hole.getNumberOfKorgools();
        }
        return w;
    }

    /**
     * Get hole values on black player's board in GUI in the form of an array
     * @return black player's whole values
     */
    private int[] getBlackArray() {
        int[] b = new int[9];
        for (int i = 0; i < 9; i++) {
            b[i] = gameWindow.getButtonMap().get("B" + (i + 1)).getNumberOfKorgools();
        }
        return b;
    }

    /**
     * Compare hole and kazans values in GUI and Core to each other and to expected values
     * @param moveIndex index of the move in the sequence
     * @param expectedKazanW expected value for white player's kazan after given move
     * @param expectedKazanB expected value for black player's kazan after given move
     */
    private void compareAll(int moveIndex, int expectedKazanW, int expectedKazanB) {
        //holes
        compareHoles(expectedHolesW[moveIndex], getWhiteArray(), gameManager.getCore().getWhitePlayer().getHoles());
        int[] reversed = new int[9];
        for (int i = 0; i < 9; i++) {
            reversed[8 - i] = gameManager.getCore().getBlackPlayer().getHoles()[i];
        }
        compareHoles(expectedHolesB[moveIndex], getBlackArray(), reversed);
        //kazans
        compare(expectedKazanW, gameWindow.getKazanRight().getNumberOfKorgools(), gameManager.getCore().getWhitePlayer().getKazan());
        compare(expectedKazanB, gameWindow.getKazanLeft().getNumberOfKorgools(), gameManager.getCore().getBlackPlayer().getKazan());
    }

    /**
     * Compare hole values in GUI and Core to each other and to expected values
     * @param expected Expected hole value
     * @param gui hole value on the GUI
     * @param core hole value in the Core
     */
    private void compareHoles(int[] expected, int[] gui, int[] core) {
        assertArrayEquals(expected, gui);
        assertArrayEquals(gui, core);
    }

    /**
     * Compare GUI and Core values to each other and to expected value
     * @param expected Expected value
     * @param gui Value on the GUI
     * @param core Value in the Core
     */
    private void compare(int expected, int gui, int core) {
        assertEquals(expected, gui);
        assertEquals(gui, core);
    }

    /**
     * Verify that tuzes stay set to appropriate values
     */
    private void compareTuzes() {
        assertTrue(gameWindow.getButtonMap().get("B3").isTuz());
        assertEquals(6, gameManager.getCore().getWhitePlayer().getTuz());
        assertTrue(gameWindow.getButtonMap().get("W2").isTuz());
        assertEquals(1, gameManager.getCore().getBlackPlayer().getTuz());
    }

}
