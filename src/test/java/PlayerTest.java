import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class that contains tests for Player.java.
 *
 * @author Karolina Szafranek
 * @version 17 November 2018
 */
public class PlayerTest {

    /**
     * Test if all fields of a player were initialised correctly.
     */
    @Test
    public void testIfAllFieldsInitialisedCorrectly() {
        Player player = new Player();
        assertEquals("Kazan initialised as: " + player.getKazan() + " - incorrect.", 0, player.getKazan());
        assertEquals("Tuz initialised as: " + player.getTuz() + " - incorrect.", -1, player.getTuz());
        assertEquals("Player has " + player.getHoles().length + "holes but should have 9", 9, player.getHoles().length);

        for (int i : player.getHoles()) {
            assertEquals("Holes initialised incorrectly.", 9, i);
        }
    }

    /**
     * Test if calling increment method sets value of given hole to 1 more than it was.
     */
    @Test
    public void testIfIncrementingWorksCorrectly() {
        Player player = new Player();
        int before = player.getHoleAt(3);
        int expected = before + 1;
        player.incrementHole(3);
        assertEquals("Incrementing hole did not work properly. Value after incrementing should be" + expected + "and is: " + player.getHoleAt(3), 1, player.getHoleAt(3) - before);
    }

    /**
     * Test if setting tuz and kazan to desired values works correctly
     */
    @Test
    public void testSettingTuzAndKazan() {
        Player player = new Player();
        int[] tuzCases = {0,4,8};
        for (int i : tuzCases) {
            player.setTuz(i);
            assertEquals("Setting tuz to " + i + " did not work. Tuz is: " + player.getTuz(), i, player.getTuz());
        }

        player.setKazan(20);
        assertEquals("Setting kazan to 20 did not work. Kazan is: " + player.getKazan(), 20, player.getKazan());
    }

    /**
     * Test setting array of holes to existing array works correctly
     */
    @Test
    public void testSettingHolesToExistingArray() {
        int[] expected = {2,4,6,8,10,12,14,16,18};
        Player player = new Player();
        player.setHoles(expected);
        assertEquals(expected, player.getHoles());
    }

    /**
     * Test resetting holes to all have value 0
     */
    @Test
    public void testResettingHoles() {
        Player player = new Player();
        player.reset();
        for (int h : player.getHoles()) {
            assertEquals(0, h);
        }
    }

    /**
     * Test if setting holes at valid indices to desired values works correctly
     */
    @Test
    public void testSettingHolesForValidIndices() {
        Player player = new Player();
        int[] validIndices = {0,4,8};
        for (int i : validIndices) {
            player.setHole(i,10);
            assertEquals("Setting hole at index " + i + " did not work. Hole is: " + player.getHoleAt(i), 10, player.getHoleAt(i));
        }
    }

    /**
     * Test if setting holes at invalid indices throws exception
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSettingHolesForInvalidIndices() {
        Player player = new Player();
        int[] invalidIndices = {-1, 9, 20};
        for (int i : invalidIndices) {
            player.setHole(i,10);
        }
    }

}