import java.util.Arrays;

/**
 * Class representing a board of the game
 */
public class Board {
    // White player
    private int[] holesW;
    private int kazanW; // number of kargools in player's kazan
    private int tuzW; // index of player's tuz hole

    // Black player
    private int[] holesB;
    private int kazanB;
    private int tuzB;


    //TODO: Refactor

    public Board() {
        kazanW = 0;
        kazanB = 0;

        holesW = new int[9];
        holesB = new int[9];

        tuzW = -1;
        tuzB = -1;

        Arrays.fill(holesW, 9);
        Arrays.fill(holesB, 9);
    }

    public int getKazanW() {
        return kazanW;
    }

    public int getKazanB() {
        return kazanB;
    }

    public int[] getHolesW() {
        return holesW;
    }

    public int[] getHolesB() {
        return holesB;
    }

    public int getTuzW() {
        return tuzW;
    }

    public int getTuzB() {
        return tuzB;
    }

    /**
     * Helper function, prints state of the board
     */
    public void printBoard() {
        System.out.print("\nBlack: ");
        for (int i = 8; i >= 0; i--) {
            System.out.print(" " + holesB[i]);
        }
        System.out.print("\nWhite: ");
        for (int i : holesW) {
            System.out.print(" " + i);
        }
        System.out.println("\nkazanB: " + kazanB);
        System.out.println("kazanW: " + kazanW);
    }


    /**
     * Represents making a move on the board
     * @param hole The hole the move starts with
     * @param isWhiteTurn True if it is white player's turn, false if it is black player's turn
     * @return Status of the board
     */
    public BoardStatus makeMove(int hole, boolean isWhiteTurn) {
        if (!checkIfMovePossible(isWhiteTurn)) {
            return BoardStatus.MOVE_IMPOSSIBLE;
        }
        boolean isWhiteCurrent = isWhiteTurn;
        int korgools;
        if (isWhiteCurrent) {
            korgools = holesW[hole];
            holesW[hole] = 0;
        }
        else {
            korgools = holesB[hole];
            holesB[hole] = 0;
        }

        if (korgools == 0) {
            return BoardStatus.MOVE_UNSUCCESSFUL;
        }
        if (korgools == 1) {
            if (hole == 8) {
                hole = 0;
                isWhiteCurrent = !isWhiteCurrent;
            }
            else {
                hole++;
            }

            if (isWhiteCurrent) {
                holesW[hole] += 1;
            }
            else {
                holesB[hole] += 1;
            }

        } else {
            for (int i = korgools; i > 0; --i) {

                if (isWhiteCurrent) {
                    holesW[hole] += 1;
                }
                else {
                    holesB[hole] += 1;
                }
                if (i == 1) {
                    break;
                }
                if (hole == 8) {
                    isWhiteCurrent = !isWhiteCurrent;
                    hole = 0;
                }
                else {
                    hole++;
                }
            }
        }
        return endMove(hole, isWhiteCurrent, isWhiteTurn);
    }

    /**
     * Helper function determines whether given player can make a move,
     * ie if there are any non-empty hole on player's board
     * @param isWhiteTurn True if it is white player's turn, false if it is black player's turn
     * @return true if move is possible, false otherwise
     */
    private boolean checkIfMovePossible(boolean isWhiteTurn) {
        int[] player;
        if (isWhiteTurn) {
            player = holesW;
        } else {
            player = holesB;
        }
        for (int hole : player) {
            if (hole != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function implementing ending conditions of a move
     * @param lastHoleFilled The index of hole that was filled last
     * @param isOnWhiteSide True if the last hole was on white player's side, false if it was on black player's side
     * @param isWhiteTurn True if it is white player's turn, false if it is black player's turn
     * @return Status of the board
     */
    private BoardStatus endMove(int lastHoleFilled, boolean isOnWhiteSide, boolean isWhiteTurn) {
        if (isWhiteTurn && !isOnWhiteSide && lastHoleFilled != tuzW) {
            if (holesB[lastHoleFilled] == 3 && tuzW == -1 && tuzB != lastHoleFilled) {
                tuzW = lastHoleFilled;
            }
            else if (holesB[lastHoleFilled] % 2 == 0) {
                kazanW += holesB[lastHoleFilled];
                holesB[lastHoleFilled] = 0;
            }
        }
        else if (!isWhiteTurn && isOnWhiteSide && lastHoleFilled != tuzB) {
            if (holesW[lastHoleFilled] == 3 && tuzB == -1 && tuzW != lastHoleFilled) {
                tuzB = lastHoleFilled;
            }
            else if (holesW[lastHoleFilled] % 2 == 0) {
                kazanB += holesW[lastHoleFilled];
                holesW[lastHoleFilled] = 0;
            }
        }

        if (tuzW != -1) {
            kazanW += holesB[tuzW];
            holesB[tuzW] = 0;
        }
        if (tuzB != -1) {
            kazanB += holesW[tuzB];
            holesW[tuzB] = 0;
        }

        return checkResult();

    }

    /**
     * Helper function checking status of the board after a move
     * @return Status of the board
     */
    private BoardStatus checkResult() {
        if (kazanW >= 82) {
            return BoardStatus.W_WON;
        } else if (kazanB >= 82) {
            return BoardStatus.B_WON;
        } else if (kazanW == 81 && kazanB == 81) {
            return BoardStatus.DRAW;
        } else {
            return BoardStatus.SUCCESSFUL;
        }

    }


}

/**
 * Enum class representing statuses a board can have
 */
enum BoardStatus {
    SUCCESSFUL, //move went well but the game is not finished, next player should make a move
    MOVE_UNSUCCESSFUL,
    MOVE_IMPOSSIBLE,
    B_WON,
    W_WON,
    DRAW
}
