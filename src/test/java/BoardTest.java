import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class that contains tests for Board.java.
 *
 * @author Luka Kralj
 * @version 12 November 2018
 */
public class BoardTest {

    private Board board;

    @Before
    public void start() {
        board = new Board();
    }

    @After
    public void finish() {
        board = null;
    }

    @Test
    public void testIfAllFieldsInitialisedCorrectly() {
        assertEquals("White kazan initialised as: " + board.getKazanW() + " - incorrect.", 0, board.getKazanW());
        assertEquals("Black kazan initialised as: " + board.getKazanB() + " - incorrect.", 0, board.getKazanB());
        assertEquals("White tuz initialised as: " + board.getTuzW() + " - incorrect.", -1, board.getTuzW());
        assertEquals("Black tuz initialised as: " + board.getTuzB() + " - incorrect.", -1, board.getTuzB());

        for (int i : board.getHolesW()) {
            assertEquals("White holes initialised incorrectly.", 9, i);
        }

        for (int i : board.getHolesB()) {
            assertEquals("Black holes initialised incorrectly.", 9, i);
        }
    }
}
