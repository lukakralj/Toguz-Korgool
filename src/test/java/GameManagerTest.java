import static org.junit.Assert.assertEquals;
import gui.GameWindow;
import gui.Hole;
import logic.GameManager;
import logic.Player;
import org.junit.Before;
import org.junit.Test;
import com.athaydes.automaton.Swinger;

import java.util.HashMap;


/**
 * Class that contains tests for the Game Manager - tests if changes in GUI reflect changes in logic
 *
 * @author Kayla Phillips Sanchez, Karolina Szafranek
 * @version 26 November 2018
 */

public class GameManagerTest {

    private GameManager gameManager;
    private GameWindow gameWindow;
    private Swinger swinger;

    @Before
    public void openWindow() {
        gameManager = new GameManager();
        gameWindow = gameManager.getWindow();
        swinger = Swinger.getUserWith(gameWindow);
        swinger.pause(200);
    }

    @Test
    public void testSetUp() {
        Player white =  gameManager.getCore().getWhitePlayer();
        Player black = gameManager.getCore().getBlackPlayer();

        // test holes setup
        for (int i = 1; i <=9; i++) {
            assertEquals(gameWindow.getButtonMap().get("W" + i).getNumberOfKorgools(),
                    white.getHoleAt(i - 1));
        }

        for (int i = 1; i <=9; i++) {
            assertEquals(gameWindow.getButtonMap().get("B" + i).getNumberOfKorgools(),
                    black.getHoleAt(9 - i));
        }

        // test tuzes setup
        HashMap<String, Hole> holes = gameWindow.getButtonMap();
        for (String hole : holes.keySet()) {
            if (holes.get(hole).isTuz()) {
                if (hole.startsWith("W")) {
                    int index = Integer.parseInt(hole.substring(1)) - 1;
                    assertEquals(index, white.getTuz());
                } else {
                    int index = 9 - Integer.parseInt(hole.substring(1));
                    assertEquals(index, black.getTuz());
                }
            }
        }

        // test kazans setup
        assertEquals(gameWindow.getKazanLeft().getNumberOfKorgools(), white.getKazan());
        assertEquals(gameWindow.getKazanRight().getNumberOfKorgools(), black.getKazan());


    }

    @Test
    public void testSingleGame() {


    }
}
