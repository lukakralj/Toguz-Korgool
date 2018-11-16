import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.Set;

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

    public GameManager() {
        gameWindow = new GameWindow(this);
        populateInitialBoard();
        core = new Board();
        //makeMove();
    }

    private void populateInitialBoard() {
        Set<String> setOfButtonsIDs = gameWindow.getButtonMap().keySet();
        for (String buttonId : setOfButtonsIDs) {
            gameWindow.setHoleText(buttonId, "9");
        }
    }

    private void updateWhiteDisplay() {
        int[] holesW = core.getHolesW();
        for (int i = 1; i <= 9; i++) {
            gameWindow.setHoleText("W" + i, holesW[i - 1] + "");
        }
    }

    private void updateBlackDisplay() {
        int[] holesB = core.getHolesB();
        int holeIndex = 0;
        for (int i = 9; i >= 1; i--) {
            gameWindow.setHoleText("B" + i, holesB[holeIndex] + "");
            holeIndex++;
        }
    }

    private void updateWhiteKazan() {
        int kazanW = core.getKazanW();
        gameWindow.setKazanRightText(kazanW + "");
    }

    private void updateBlackKazan() {
        int kazanB = core.getKazanB();
        gameWindow.setKazanLeftText(kazanB + "");
    }

    private void updateWhiteTuz() {
        int tuzW = core.getTuzW();
        if (tuzW != -1) {
            int buttonNumber = 9 - tuzW;  //because ID's start from 1
            gameWindow.makeTuz("B" + buttonNumber);
        }
    }

    private void updateBlackTuz() {
        int tuzB = core.getTuzB();
        if (tuzB != -1) {
            int buttonNumber = tuzB + 1;  //because ID's start from 1
            gameWindow.makeTuz("W" + buttonNumber);
        }
    }

    public void updateDisplayOnSuccess() {
        updateWhiteDisplay();
        updateBlackDisplay();

        updateWhiteKazan();
        updateBlackKazan();

        updateWhiteTuz();
        updateBlackTuz();
    }

    private int machineMakeMove() {
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

    public void makeMove(String buttonId) {
        try {
            if (buttonId.startsWith("W")) {
                System.out.println("test");
                moveCore(buttonId.substring(1), true);
            } else moveCore(buttonId.substring(1), false);
        } catch (Exception e) {
            System.out.println("Enter: 0-8 or 'q' to exit. Cannot pick a hole with 0 korgols ");
            e.printStackTrace();
        }
    }


    private void moveCore(String line, boolean isWhiteTurn) throws Exception {
        int hole;
        if (!line.trim().equals("")) {
            hole = Integer.parseInt(line) - 1;
            if (hole > 8 || hole < 0) {
                throw new Exception();
            }
        } else {
            throw new Exception();
        }
        BoardStatus moveStatus = core.makeMove(hole, isWhiteTurn);
        BoardStatus endStatus = core.testCheckResult();
        if (moveStatus == BoardStatus.SUCCESSFUL) {
            if (isWhiteTurn) makeMove("B" + (machineMakeMove() + 1));
            updateDisplayOnSuccess();

        } else if (moveStatus == BoardStatus.MOVE_UNSUCCESSFUL) {
            throw new Exception();
        } else if (moveStatus == BoardStatus.MOVE_IMPOSSIBLE) {
            System.out.println("The player cannot make a move. Will skip turn");
        }

        core.printBoard();
        if (endStatus == BoardStatus.B_WON) {
            System.out.println("Black player won!");
            return;
        } else if (endStatus == BoardStatus.W_WON) {
            System.out.println("White player won!");
            return;
        } else if (endStatus == BoardStatus.DRAW) {
            System.out.println("It's a tie!");
            return;
        }
    }
}