import static org.junit.Assert.assertEquals;
import gui.GameWindow;
import gui.Hole;
import logic.AnimationController;
import logic.GameManager;
import logic.Player;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.athaydes.automaton.Swinger;
import static org.junit.Assert.*;
import static com.athaydes.automaton.assertion.AutomatonMatcher.hasText;
import com.athaydes.automaton.Speed;

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
        gameManager.setRandomSeed();
        //Swinger.setDEFAULT(Speed.FAST);
        AnimationController.setRunTime(2);
        swinger.pause(500);
        swinger.clickOn("name:W1").pause(600);
        swinger.clickOn("name:W2").pause(600);
        swinger.clickOn("name:W4").pause(600);
        swinger.clickOn("name:W5").pause(600);
        swinger.clickOn("name:W3").pause(600);
        swinger.clickOn("name:W5").pause(600);
        swinger.clickOn("name:W6").pause(600);
        swinger.clickOn("name:W4").pause(600);
        swinger.clickOn("name:W5").pause(600);
        swinger.clickOn("name:W6").pause(600);
        swinger.clickOn("name:W9").pause(600);
        swinger.clickOn("name:W8").pause(600);
        swinger.clickOn("name:W7").pause(600);
        swinger.clickOn("name:W1").pause(600);
        swinger.clickOn("name:W8").pause(600);
        swinger.clickOn("name:W9").pause(600);
        swinger.clickOn("name:W1").pause(600);
        swinger.clickOn("name:W9").pause(600);
        swinger.clickOn("name:W4").pause(600);
        swinger.clickOn("name:W5").pause(600);
        swinger.clickOn("name:W3").pause(600);
        swinger.clickOn("name:W8").pause(600);
        swinger.clickOn("name:W8").pause(600);
        swinger.clickOn("name:W6").pause(600);
        swinger.clickOn("name:W7").pause(600);
        swinger.clickOn("name:W9").pause(600);
        swinger.clickOn("name:W9").pause(900);  //not tested yet

//        swinger.clickOn("name:W5").pause(5000).clickOn("name:W6").pause(5000);
//        swinger.clickOn("name:W7").pause(5000).clickOn("name:W8").pause(5000);
//        swinger.clickOn("name:W9").pause(5000).clickOn("name:W1").pause(5000);
//        swinger.clickOn("name:W
// 2").pause(5000).clickOn("name:W3").pause(5000);
//        swinger.clickOn("name:W4").pause(5000).clickOn("name:W5").pause(5000);
//        swinger.clickOn("name:W6").pause(5000).clickOn("name:W7").pause(5000);
//        swinger.clickOn("name:W8").pause(5000).clickOn("name:W9").pause(5000);
//        swinger.clickOn("name:W1").pause(5000).clickOn("name:W2").pause(5000);
//        swinger.clickOn("name:W3").pause(5000).clickOn("name:W4").pause(5000);
//        swinger.clickOn("name:W5").pause(5000).clickOn("name:W6").pause(5000);
//        swinger.clickOn("name:W7").pause(5000).clickOn("name:W8").pause(5000);
//        swinger.clickOn("name:W9").pause(5000).clickOn("name:W1").pause(5000);
//        swinger.clickOn("name:W2").pause(5000).clickOn("name:W3").pause(5000);
//        swinger.clickOn("name:W4").pause(5000).clickOn("name:W5").pause(5000);
//        swinger.clickOn("name:W6").pause(5000).clickOn("name:W7").pause(5000);
//        swinger.clickOn("name:W8").pause(5000).clickOn("name:W9").pause(5000);

//        swinger.clickOn(“name:W3”).pause(2000).clickOn(“name:W3").pause(2000).clickOn(“name:W3”).pause(2000).clickOn(“name:W3").pause(2000)
//                .clickOn(“name:W3”).pause(2000).clickOn(“name:W3").pause(2000).clickOn(“name:W3”).pause(2000).clickOn(“name:W3").pause(2000);
//                .clickOn(“name:W1”).pause(2000).clickOn(“name:W2").pause(2000).clickOn(“name:W3”).pause(2000).clickOn(“name:W4").pause(2000)
//                .clickOn(“name:W1”).pause(2000).clickOn(“name:W2").pause(2000).clickOn(“name:W3”).pause(2000).clickOn(“name:W4").pause(2000)
//                .clickOn(“name:W1”).pause(2000).clickOn(“name:W2").pause(2000).clickOn(“name:W3”).pause(2000).clickOn(“name:W4").pause(2000);

    }

}
