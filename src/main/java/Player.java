import java.util.Arrays;

/**
 * Class representing a player on the board
 *
 * @author Karolina Szafranek
 * @version 13 November 2018
 */
public class Player {

    private int[] holes;
    private int kazan; // number of korgools in player's kazan
    private int tuz; // index of player's tuz hole

    public Player() {
        kazan = 0;
        holes = new int[9];
        tuz = -1;

        Arrays.fill(holes, 9);
    }

    public int[] getHoles() {
        return holes;
    }

    public int getHoleAt(int index) {
        return holes[index];
    }

    public int getKazan() {
        return kazan;
    }

    public int getTuz() {
        return tuz;
    }

    public void setHole(int index, int value) {
        holes[index] = value;
    }

    public void incrementHole(int index) {
        holes[index]++;
    }
    public void setKazan(int kazan) {
        this.kazan = kazan;
    }

    public void setTuz(int tuz) {
        this.tuz = tuz;
    }

    public void printHoles() {
        for (int i : holes) {
            System.out.print(" " + i);
        }
    }

    public void printHolesReversed() {
        for (int i = 8; i >= 0; i--) {
            System.out.print(" " + holes[i]);
        }
    }
}

