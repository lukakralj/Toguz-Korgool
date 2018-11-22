import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import javax.swing.ImageIcon;

/*
 * Main class for the Team Platypus Agile Project
 *
 */
public class GameManager {

    private GameWindow gameWindow;
    private Board core;

    public static void main(String[] args) {
        System.out.println("Main method works as intended");
        GameManager manager = new GameManager();
    }

    /**
     * Construct the game manager
     */
    public GameManager() {
        gameWindow = new GameWindow(this);
        populateInitialBoard();
        core = new Board();
    }

    /**
     * Set the default initial state of the board (all 9's)
     */
    private void populateInitialBoard() {
        Set<String> setOfButtonsIDs = gameWindow.getButtonMap().keySet();
        for (String buttonId : setOfButtonsIDs) {
            gameWindow.setHoleText(buttonId, "9");
        }
    }

    public void setUpBoard(int[] wHoles, int[] bHoles, int wTuz, int bTuz, int wKazan, int bKazan) {
        core.setHolesW(wHoles);  //updates game logic
        core.setHolesB(bHoles);
        core.setTuzW(wTuz - 1);
        core.setTuzB(bTuz - 1);
        core.setKazanW(wKazan);
        core.setKazanB(bKazan);
        updateDisplayOnSuccess();   //updates display

    }

    /**
     * Update the GUI to correctly display the changes that have occurred in the array of white holes.
     */
    private void updateWhiteDisplay() {
        int[] holesW = core.getHolesW();
        for (int i = 1; i <= 9; i++) {
            gameWindow.setHoleText("W" + i, holesW[i - 1] + "");
        }
    }

    /**
     * Update the GUI to correctly display the changes that have occurred in the array of black holes.
     */
    private void updateBlackDisplay() {
        int[] holesB = core.getHolesB();
        int holeIndex = 0;
        for (int i = 9; i >= 1; i--) {
            gameWindow.setHoleText("B" + i, holesB[holeIndex] + "");
            holeIndex++;
        }
    }

    /**
     * Update the GUI to correctly display the changes that have occurred in the white player's kazan.
     */
    private void updateWhiteKazan() {
        int kazanW = core.getKazanW();
        gameWindow.setKazanRightText(kazanW + "");
    }

    /**
     * Update the GUI to correctly display the changes that have occurred in the black player's kazan.
     */
    private void updateBlackKazan() {
        int kazanB = core.getKazanB();
        gameWindow.setKazanLeftText(kazanB + "");
    }

    /**
     * Checks to see if a Tuz has been set for the white player. If yes, it updates the GUI to represent this change.
     */
    private void updateWhiteTuz() {
        int tuzW = core.getTuzW();
        if (tuzW != -1) {
            int buttonNumber = 9 - tuzW;  //because ID's start from 1
            gameWindow.makeTuz("B" + buttonNumber);
            //System.out.print("white tuz was set to "+ tuzW);
        }
    }

    /**
     * Checks to see if a Tuz has been set for the black player. If yes, it updates the GUI to represent this change.
     */
    private void updateBlackTuz() {
        int tuzB = core.getTuzB();
        if (tuzB != -1) {
            int buttonNumber = tuzB + 1;  //because ID's start from 1
            gameWindow.makeTuz("W" + buttonNumber);
            //System.out.print("black tuz was set to "+ tuzB);
        }
    }

    /**
     * Combines all previous update methods into one general function meant to update the GUI after any changes.
     */
    public void updateDisplayOnSuccess() {
        updateWhiteDisplay();
        updateBlackDisplay();

        updateWhiteKazan();
        updateBlackKazan();

        updateWhiteTuz();
        updateBlackTuz();
    }
	
	public JTextArea getKazanLeft() {
        return gameWindow.getKazanLeft();
    }
	
	public JTextArea getKazanRight() {
        return gameWindow.getKazanRight();
    }

    /**
     * Returns a randomly chosen hole number. Makes sure it does not equal the white Tuz, and also makes sure that the chosen
     *  whole has at least 1 korgool in it.
     *  @return the hole chosen by the machine
     */
    private int machineChooseHole() {
        Random random = new Random();
        boolean foundNumber = false;
        int hole = -1;
        while (!foundNumber) {
            hole = random.nextInt(8);  //generate random number 0-7
            if (hole >= core.getTuzW()) {         // if random number >= white Tuz
                hole += 1;                        // we increase by 1 to cover the full range 0-8 (except the tuz)
            }
            int[] blackHoles = core.getHolesB();
            if (blackHoles[hole] != 0) {
                foundNumber = true;
            }
        }
        System.out.println("Randomly chose " + hole);
        return hole;
    }

    /**
     * If a move is impossible, the opponent collects all of their own korgools and puts them into
     * their kazaan. The kazans of the two players are then compared to determine the winner (more korgools wins)
     *  @param isWhiteTurn boolean indicating whether it is the white player's turn
     */
    public void endImpossibleGame(boolean isWhiteTurn) {
        if (isWhiteTurn) {
            core.getAllBlackKorgools();
        }
        else {
            core.getAllWhiteKorgools();
        }
        updateDisplayOnSuccess();
        BoardStatus endStatus = core.testCheckResultOnImpossible();
        checkEndStatus(endStatus);

    }

    /**
     *  Checks whether the game has ended, either because of a win, loss, or tie.
     *  @param endStatus the endStatus of the board at this point
     */
    public void checkEndStatus(BoardStatus endStatus) {

        if (endStatus == BoardStatus.B_WON) {
            gameWindow.setKazanLeftText("Black player won!");

        } else if (endStatus == BoardStatus.W_WON) {
            gameWindow.setKazanRightText("White player won!");

        } else if (endStatus == BoardStatus.DRAW) {
            gameWindow.setKazanLeftText("It's a tie!");
            gameWindow.setKazanRightText("It's a tie!");
        }
    }

    /**
     *  Checks whether the move was succesful, or if a move is impossible.
     *  @param moveStatus the status of the most recent move.
     *  @param isWhiteTurn boolean indicating whether it is the white player's turn.
     *  @param endStatus the endStatus of the board at this point
     */
    public void checkMoveStatus(BoardStatus moveStatus, boolean isWhiteTurn, BoardStatus endStatus) {
        if (moveStatus == BoardStatus.SUCCESSFUL) {  //move went well but the game is not finished, next player should make a move
            updateDisplayOnSuccess();
            if (isWhiteTurn) {
                makeMove("", false);   //calls makeMove with hole chosen by the machine.
            }
            updateDisplayOnSuccess();
            checkEndStatus(endStatus);
        }
        if (moveStatus == BoardStatus.MOVE_IMPOSSIBLE) {
            endImpossibleGame(isWhiteTurn);
        }
    }

    /**
     * Makes a move and updates the board display.
     *  @param line the string representing the buttonID
     *  @param isWhiteTurn boolean indicating whether it is the white player's turn
     */
    public void makeMove(String line, boolean isWhiteTurn) {
        int hole;
        BoardStatus moveStatus;
        if (isWhiteTurn) {
            hole = Integer.parseInt(line) - 1;     //subtract 1 because logic goes from 0-8, while GUI goes from 1-9
            moveStatus = core.makeMove(hole, true);
        }
        else {
            if (core.testCheckIfMovePossible(false)) {
                hole = machineChooseHole();
                moveStatus = core.makeMove(hole, false);
            }
            else {
                endImpossibleGame(isWhiteTurn);
                return;
            }
        }
        BoardStatus endStatus = core.testCheckResult();
        checkMoveStatus(moveStatus, isWhiteTurn, endStatus);
        checkEndStatus(endStatus);

    }


}

