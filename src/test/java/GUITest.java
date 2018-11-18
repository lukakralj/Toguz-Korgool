import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class that contains tests for the GUI.
 *
 * @version 1.1
 */
public class GUITest {

    /**
     * Test that the GUI sets up correctly without crashing
     */
    @Test
    public void GUISetup(){
        new GameWindow();
    }

    /**
     * Test that the hashmap is correctly initialised to a size of 18
     */
    @Test
    public void TestHashMapHasCorrectSize(){
        GameWindow gamewindow = new GameWindow();
        assertEquals("Hashmap incorrectly initialised",18,gamewindow.getButtonMap().size());
    }

    /**
     * Test that the hasmap of Buttons contains the correct contents
     */
    @Test
    public void TestHashMapContents(){
        GameWindow gamewindow = new GameWindow();
        assertEquals("Hashmap has incorrect contents","B1",gamewindow.getButtonMap().get("B1").getName());
        assertEquals("Hashmap has incorrect contents","B9",gamewindow.getButtonMap().get("B9").getName());//edge case
        assertEquals("Hashmap has incorrect contents","B3",gamewindow.getButtonMap().get("B3").getName());
        assertEquals("Hashmap has incorrect contents","B5",gamewindow.getButtonMap().get("B5").getName());
        assertEquals("Hashmap has incorrect contents","B7",gamewindow.getButtonMap().get("B7").getName());
        assertEquals("Hashmap has incorrect contents","W1",gamewindow.getButtonMap().get("W1").getName());
        assertEquals("Hashmap has incorrect contents","W9",gamewindow.getButtonMap().get("W9").getName());//edge case
        assertEquals("Hashmap has incorrect contents","W4",gamewindow.getButtonMap().get("W4").getName());
    }
}
