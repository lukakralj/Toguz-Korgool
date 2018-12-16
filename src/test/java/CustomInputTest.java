import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.athaydes.automaton.assertion.AutomatonMatcher.showing;

import com.athaydes.automaton.Swinger;

import static com.athaydes.automaton.selector.StringSelectors.matchingAny;

import javax.swing.*;
import java.awt.*;

import gui.*;

import static com.athaydes.automaton.assertion.AutomatonMatcher.hasText;

import com.athaydes.automaton.Speed;

/**
 * Class that contains tests for the custom input window.
 */
public class CustomInputTest {

    private Swinger swinger;
    private CustomInputWindow testWindow;

    /**
     * Set up a testing environment for the window.
     */
    @Before
    public void setUp() {
        testWindow = new CustomInputWindow();
        testWindow.toFront();
        testWindow.setAlwaysOnTop(true);
        testWindow.repaint();
        swinger = Swinger.getUserWith(testWindow);
        Swinger.setDEFAULT(Speed.VERY_FAST);
        swinger.pause(250);
    }

    /**
     * Set test variable to null after executing test
     */
    @After
    public void tearDown() {
        swinger.pause(250);
        testWindow.setAlwaysOnTop(false);
        testWindow.dispose();
        testWindow = null;
    }


    /**
     * Test that the  input dialog opens up correctly
     */
    @Test
    public void windowSetup() {
        assertNotNull(testWindow);
    }


    /**
     * Test that all cells were created successfully
     */
    @Test
    public void testCellsCreated() {
        for (int i = 1; i <= 9; ++i) {
            swinger.getAt("name:C_B" + i);
            swinger.getAt("name:C_W" + i);
        }
        swinger.getAt("name:C_BlackKazan");
        swinger.getAt("name:C_WhiteKazan");
    }


    /**
     * Test that all spinners were created successfully
     */

    @Test
    public void testSpinnersCreated() {
        for (int i = 1; i <= 9; ++i) {
            swinger.getAt("name:B" + i);
            swinger.getAt("name:W" + i);
        }
        swinger.getAt("name:BlackKazan");
        swinger.getAt("name:WhiteKazan");
    }


    /**
     * Test that all radio buttons were created successfully
     */

    @Test
    public void testRadioButtonsCreated() {
        swinger.pause(250);
        for (int i = 1; i <= 9; ++i) {
            swinger.getAt("name:R_B" + i);
            swinger.getAt("name:R_W" + i);
        }
        swinger.getAt("name:R_BlackKazan");
        swinger.getAt("name:R_WhiteKazan");
    }


    /**
     * Test situation where not enough korgools are allocated
     */
    @Test
    public void testNotEnoughKorgools() {
        JSpinner blackSpinner = (JSpinner) swinger.getAt("name:BlackKazan");
        blackSpinner.setValue(8);
        swinger.clickOn("name:confirm").pause(250);
        Component textArea = swinger.getAt("name:outputLog");
        assertThat(textArea, hasText("Please ensure the total number of Korgools is 162"));
    }

    /**
     * Test situation where too many korgools are allocated
     */
    @Test
    public void tooManyKorgools() {
        JSpinner blackSpinner = (JSpinner) swinger.getAt("name:BlackKazan");
        blackSpinner.setValue(10);
        swinger.clickOn("name:confirm").pause(200);
        Component textArea = swinger.getAt("name:outputLog");
        assertThat(textArea, hasText("Please ensure the total number of Korgools is 162"));
    }

    /**
     * Test situation where Tuz is same hole on both sides
     */
    @Test
    public void testTuzInSameHole() {
        swinger.clickOn("name:R_B1").pause(250);
        swinger.clickOn("name:R_W1").pause(250);
        swinger.clickOn("name:confirm").pause(250);
        Component textArea = swinger.getAt("name:outputLog");
        assertThat(textArea, hasText("Tuz may not exist as the same hole on opposite sides"));
    }

    /**
     * Test situation where either Tuz is allocated to hole 9
     */
    @Test
    public void testTuzAtHoleNine() {
        Component textArea = swinger.getAt("name:outputLog");
        swinger.clickOn("name:R_B9").pause(250);
        swinger.clickOn("name:confirm").pause(250);
        assertThat(textArea, hasText("Hole 9 may not become a Tuz"));
        swinger.clickOn("name:R_B8").pause(250);
        swinger.clickOn("name:R_W9").pause(250);
        swinger.clickOn("name:confirm").pause(250);
        assertThat(textArea, hasText("Hole 9 may not become a Tuz"));
    }

    /**
     * Test setting up the default board configuration
     */
    @Test
    public void testDefaultConfiguration() {
        for (int i = 1; i <= 9; ++i) {
            JSpinner blackSpinner = (JSpinner) swinger.getAt("name:B" + i);
            blackSpinner.setValue(9);
            JSpinner whiteSpinner = (JSpinner) swinger.getAt("name:W" + i);
            whiteSpinner.setValue(9);
        }
    }

    /**
     * Test setting and selecting kazan input cells
     */
    @Test
    public void testKazanCells() {
        JSpinner blackSpinner = (JSpinner) swinger.getAt("name:BlackKazan");
        blackSpinner.setValue(9);
        JSpinner whiteSpinner = (JSpinner) swinger.getAt("name:WhiteKazan");
        whiteSpinner.setValue(9);
        swinger.clickOn("name:R_B5").pause(250);
        swinger.clickOn("name:R_WhiteKazan").pause(250);
        swinger.clickOn("name:R_W5").pause(250);
        swinger.clickOn("name:R_BlackKazan").pause(250);
    }

}


