import java.util.Arrays;

public class Board {
    boolean isFinished;
    private int kazanW;
    private int kazanB;
    private int[] holesW;
    private int[] holesB;
    private int tuzW;
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
        isFinished = false;
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

    public boolean isFinished() {
        return isFinished;
    }

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

    // from game manager run this to check if any move is possible -> skip the turn if not possible
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

    // return true if move successful
    public boolean makeMove(int hole, boolean isWhiteTurn) {
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
            return false;
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
        endMove(hole, isWhiteCurrent, isWhiteTurn);
        return true;
    }

    private void endMove(int lastHoleFilled, boolean isOnWhiteSide, boolean isWhiteTurn) {
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
        isFinished = kazanW >= 82 || kazanB >= 82 || (kazanW == 81 && kazanB == 81);
    }


}
