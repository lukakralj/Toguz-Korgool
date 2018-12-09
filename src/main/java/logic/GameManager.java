package logic;

import gui.GameWindow;
import gui.Hole;
import gui.OvalButton;
import java.util.Random;
import java.util.Set;

/**
 * Main class for the Team Platypus Agile Project
 *
 */
public class GameManager {

    private GameWindow gameWindow;
    private Board core;
    private AnimationController anim;
    private Random random;

    /**
     * Construct the game manager
     */
    public GameManager() {
        random = new Random();
        gameWindow = new GameWindow(this);
        AnimationController.resetController(gameWindow);
        AnimationController.instance().start();
        populateInitialBoard();
        core = new Board();
    }

    /**
     * Set the default initial state of the board (all 9's)
     */
    private void populateInitialBoard() {
        Set<String> setOfButtonsIDs = gameWindow.getButtonMap().keySet();
        for (String buttonId : setOfButtonsIDs) {
            gameWindow.populateWithKorgools(buttonId, 9);
        }
        gameWindow.resetTuzes();
    }

    /**
     * Return the GUI window
     *
     * @return the game window JFrame
     */
    public GameWindow getWindow() {
        return gameWindow;
    }


    /**
     * Set the initial state of the board with given custom parameters
     *
     * @param wHoles White player's holes
     * @param bHoles Black player's holes
     * @param wTuz White player's tuz
     * @param bTuz Black player's tuz
     * @param wKazan White player's kazan
     * @param bKazan Black player's kazan
     */
    public void populateInitialBoard(int[] wHoles, int[] bHoles, int wTuz, int bTuz, int wKazan, int bKazan) {
        //updates game logic
        AnimationController.resetController(gameWindow);
        AnimationController.instance().start();
        populatePlayerBoard(core.getWhitePlayer(), wHoles, wTuz, wKazan);
        populatePlayerBoard(core.getBlackPlayer(), bHoles, bTuz, bKazan);

        gameWindow.populateWithKorgools("left", bKazan);
        gameWindow.populateWithKorgools("right", wKazan);

        gameWindow.resetTuzes();

        for (int i = 0; i < 9; i++) {
            gameWindow.populateWithKorgools("W" + (i + 1), wHoles[i]);
        }

        for (int i = 0; i < 9; i++) {
            gameWindow.populateWithKorgools("B" + (9 - i), bHoles[i]);
        }

        if (wTuz != -1) {
            gameWindow.setTuz("B" + (9 - wTuz)); // set white tuz
        }
        if (bTuz != -1) {
            gameWindow.setTuz("W" + bTuz); // set black tuz
        }
    }


    /**
     * Set the initial state of the player's board with given custom parameters
     *
     * @param player player whose board is to be initialised
     * @param holes player's holes
     * @param tuz player's tuz
     * @param kazan player's kazan
     */
    private void populatePlayerBoard(Player player, int[] holes, int tuz, int kazan) {
        player.setHoles(holes);
        player.setTuz(tuz - 1);
        player.setKazan(kazan);
    }

    /**
     * Makes a move and updates the board display.
     *  @param buttonID the string representing the buttonID
     *  @param isWhiteTurn boolean indicating whether it is the white player's turn
     */
    public void makeMove(String buttonID, boolean isWhiteTurn) {
        int hole;
        BoardStatus moveStatus;
        if (isWhiteTurn) {
            hole = Integer.parseInt(buttonID) - 1;     //subtract 1 because logic goes from 0-8, while GUI goes from 1-9
            moveStatus = core.makeMove(hole, core.getWhitePlayer(), core.getBlackPlayer());
        }
        else {
            if (core.checkIfMovePossible(core.getBlackPlayer())) {
                hole = machineChooseHole();
                moveStatus = core.makeMove(hole, core.getBlackPlayer(), core.getWhitePlayer());
            }
            else {
                endImpossibleGame(false);
                return;
            }
        }
        BoardStatus endStatus = core.checkResult();
        checkMoveStatus(moveStatus, isWhiteTurn, endStatus);
        checkEndStatus(endStatus);
    }

    /**
     * Returns a randomly chosen hole number. Makes sure it does not equal the white Tuz, and also makes sure that the chosen
     *  whole has at least 1 korgool in it.
     *
     *  @return the hole chosen by the machine
     */
    public int machineChooseHole() {
        boolean foundNumber = false;
        int hole = -1;
        while (!foundNumber) {
            hole = random.nextInt(8);  //generate random number 0-7
            if (hole >= core.getWhitePlayer().getTuz()) {         // if random number >= white Tuz
                hole += 1;                        // we increase by 1 to cover the full range 0-8 (except the tuz)
            }
            System.out.println("Bot chose hole of index " + hole);
            if (core.getBlackPlayer().getHoleAt(hole) != 0) {
                foundNumber = true;
                System.out.println("Bot CHOSE FINAL hole of index " + hole);
            }
        }
        return hole;
    }


    /**
     * If a move is impossible, the opponent collects all of their own korgools and puts them into
     * their kazan. The kazans of the two players are then compared to determine the winner (more korgools wins)
     *
     * @param isWhiteTurn boolean indicating whether it is the white player's turn
     */
    private void endImpossibleGame(boolean isWhiteTurn) {
        if (isWhiteTurn) {
            core.getAllKorgools(core.getBlackPlayer());
        }
        else {
            core.getAllKorgools(core.getWhitePlayer());
        }
        BoardStatus endStatus = core.checkResultOnImpossible();
        checkEndStatus(endStatus);
    }

    /**
     *  Checks whether the game has ended, either because of a win, loss, or tie.
     *
     *  @param endStatus the endStatus of the board at this point
     */
    private void checkEndStatus(BoardStatus endStatus) {

        if (endStatus == BoardStatus.B_WON) {
            gameWindow.displayMessage("Black player won!");
            AnimationController.instance().stopAnimator();

        } else if (endStatus == BoardStatus.W_WON) {
            gameWindow.displayMessage("White player won!");
            AnimationController.instance().stopAnimator();

        } else if (endStatus == BoardStatus.DRAW) {
            gameWindow.displayMessage("It's a tie!");
            AnimationController.instance().stopAnimator();
        }
    }

    /**
     *  Checks whether the move was succesful, or if a move is impossible.
     *  @param moveStatus the status of the most recent move.
     *  @param isWhiteTurn boolean indicating whether it is the white player's turn.
     *  @param endStatus the endStatus of the board at this point
     */
    private void checkMoveStatus(BoardStatus moveStatus, boolean isWhiteTurn, BoardStatus endStatus) {
        if (moveStatus == BoardStatus.SUCCESSFUL) {  // move went well but the game is not finished, next player should make a move
            if (isWhiteTurn) {
                makeMove("", false);   // calls makeMove with hole chosen by the machine.
            }
            checkEndStatus(endStatus);
        }
        if (moveStatus == BoardStatus.MOVE_IMPOSSIBLE) {
            endImpossibleGame(isWhiteTurn);
        }
    }

    public Hole getKazanLeft() {
        return gameWindow.getKazanLeft();
    }

    public Hole getKazanRight() {
        return gameWindow.getKazanRight();
    }


    /**
     * Sets random seed to make bot's behaviour predictable.
     * Used for testing
     */
    public void setRandomSeed() {
        random.setSeed(3);
    }

    /**
     * Getter only used for testing
     * @return board of the game
     */
    public Board getCore() {
        return core;
    }
}

