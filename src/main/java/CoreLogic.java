import java.util.Arrays;

public class CoreLogic {

    private int kazanW;
    private int kazanB;
    private int[] holesW;
    private int[] holesB;


    public CoreLogic() {
        kazanW = 0;
        kazanB = 0;
        holesW = new int[9];
        holesB = new int[9];
        Arrays.fill(holesW, 9);
        Arrays.fill(holesB, 9);
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

    public void makeMove(int hole, boolean isWhiteTurn) {
        int korgools = 0;
        if (isWhiteTurn) {
            korgools = holesW[hole];
            holesW[hole] = 0;
        }
        else {
            korgools = holesB[hole];
            holesB[hole] = 0;
        }


        for (int i = korgools; i > 0; --i) {

            if (isWhiteTurn) {
                holesW[hole] += 1;
            }
            else {
                holesB[hole] += 1;
            }
            if (hole == 8) {
                isWhiteTurn = !isWhiteTurn;
                hole = 0;
            }
            else {
                hole++;
            }
        }
    }

    public void selectB(int hole) {
    }

}
