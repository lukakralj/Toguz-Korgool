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

/**
 * Class that contains tests for the custom input window.
 */
public class CustomInputTest {

    private JFrame testFrame;
    private Swinger swinger;
    private CustomInputWindow testWindow;

    /**
     * Set up a testing environment for the window. Necessary as the window itself is a Dialog
     */
    @Before
    public void setUp() {
        testWindow = null;
        testFrame = null;
        testFrame = new JFrame("Test helper");
        JButton testButton = new JButton("Click me!");
        testButton.setName("openDialog");
        testFrame.getContentPane().add(testButton, BorderLayout.CENTER);
        testButton.addActionListener(e -> {
            testWindow = new CustomInputWindow();
        });
        testFrame.setPreferredSize(new Dimension(300, 200));
        testFrame.pack();
        testFrame.setVisible(true);
        swinger = Swinger.forSwingWindow();
    }


    /**
     * Test that the  input dialog opens up correctly
     */
    @Test
    public void GUISetup() {
        swinger.clickOn("name:openDialog").pause(200);
        assertNotNull(testWindow);
    }

    /**
     * Test that all cells were created successfully
     */
    @Test
    public void testCellsCreated() {
        swinger.clickOn("name:openDialog").pause(200).setRoot(testWindow);
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
        swinger.clickOn("name:openDialog").pause(200).setRoot(testWindow);
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
        swinger.clickOn("name:openDialog").pause(200).setRoot(testWindow);
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
        swinger.clickOn("name:openDialog").pause(200).setRoot(testWindow);
        swinger.clickOn("name:confirm").pause(50);
        Component textArea = swinger.getAt("name:outputLog");
        assertThat(textArea, hasText("Please ensure the number of Korgools adds to 162"));
    }

    /**
     * Test situation where too many korgools are allocated
     */
    @Test
    public void tooManyKorgools() {
        swinger.clickOn("name:openDialog").pause(200).setRoot(testWindow);
        for (int i = 1; i <= 9; ++i) {
            JSpinner blackSpinner = (JSpinner) swinger.getAt("name:B" + i);
            blackSpinner.setValue(10);
            JSpinner whiteSpinner = (JSpinner) swinger.getAt("name:W" + i);
            whiteSpinner.setValue(10);
        }
        swinger.clickOn("name:confirm").pause(50);
        Component textArea = swinger.getAt("name:outputLog");
        assertThat(textArea, hasText("Please ensure the number of Korgools adds to 162"));
    }

    /**
     * Test situation where Tuz is same hole on both sides
     */
    @Test
    public void testTuzInSameHole() {
        swinger.clickOn("name:openDialog").pause(200).setRoot(testWindow);
        swinger.clickOn("name:R_B1").pause(50);
        swinger.clickOn("name:R_W1").pause(50);
        swinger.clickOn("name:confirm").pause(50);
        Component textArea = swinger.getAt("name:outputLog");
        assertThat(textArea, hasText("The Tuz cannot be the same hole on opposite sides"));
    }

    /**
     * Test situation where either Tuz is allocated to hole 9
     */
    @Test
    public void testTuzAtHoleNine() {
        swinger.clickOn("name:openDialog").pause(200).setRoot(testWindow);
        Component textArea = swinger.getAt("name:outputLog");
        swinger.clickOn("name:R_B9").pause(50);
        swinger.clickOn("name:confirm").pause(50);
        assertThat(textArea, hasText("Hole 9 cannot be a Tuz"));
        swinger.clickOn("name:R_B8").pause(50);
        swinger.clickOn("name:R_W9").pause(50);
        swinger.clickOn("name:confirm").pause(50);
        assertThat(textArea, hasText("Hole 9 cannot be a Tuz"));
    }

    /**
     * Test setting up the default board configuration
     */
    @Test
    public void testDefaultConfiguration() {
        swinger.clickOn("name:openDialog").pause(200).setRoot(testWindow);
        for (int i = 1; i <= 9; ++i) {
            JSpinner blackSpinner = (JSpinner) swinger.getAt("name:B" + i);
            blackSpinner.setValue(9);
            JSpinner whiteSpinner = (JSpinner) swinger.getAt("name:W" + i);
            whiteSpinner.setValue(9);
        }
        assertThat(swinger.getAt("name:infoLabel"), hasText("<html>Please enter in the number of Korgools per hole, and use the radio buttons to indicate which holes are Tuz.<br> Current number of Korgools: 162</html>"));
    }

}


