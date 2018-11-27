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

    public void setHoles(int[] holes) {
        this.holes = holes;
    }

    public void setKazan(int numOfKorgools) {
        this.kazan = numOfKorgools;
    }

    public void setTuz(int holeIndex) {
        this.tuz = holeIndex;
    }

    public void incrementHole(int index) {
        holes[index]++;
    }

    /**
     * Set the hole specified to contain specific number of korgools.
     *
     * @param index Index of the hole.
     * @param numOfKorgools Number of korgools in this hole.
     */
    public void setHole(int index, int numOfKorgools) {
        holes[index] = numOfKorgools;
    }

    /**
     * Reset player's holes to all have 0 as value
     */
    public void reset() {
        Arrays.fill(holes, 0);

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
