import gui.GameWindow;
import gui.Hole;
import logic.AnimationController;
import logic.GameManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.athaydes.automaton.Swinger;
import static com.athaydes.automaton.assertion.AutomatonMatcher.hasText;


/**
 * Class that contains tests for the GUI.
 *
 * @version 1.1
 */
public class GUITest {


    @Before
    public void start() {
        // Speed up animations.
        AnimationController.setRunTime(5);
    }

    /**
     * Test that the GUI sets up correctly without crashing
     */
    @Test
    public void GUISetup(){
        new GameManager();
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
	
	/**
	* Test that the first and second button can be pressed without the application crashing
	*/
	@Test
	public void testCorrectButtonInteraction() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W1")
		.pause(1000).clickOn("name:W2");
		swinger.pause(100);
	}
	
	/**
	 * Test that only numbers are present in the Kazans and that
	 * any numbers stored are always positive
	 */
	@Test
	public void testLegitimateValsInKazans() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W1")
		.pause(1000).clickOn("name:W2")
		.pause(1000).clickOn("name:W3");
		Hole kazan = m.getKazanLeft();
		int actual = kazan.getNumberOfKorgools();
		assertEquals(0, actual);
		swinger.pause(150);
	}
	
	/**
	 * Test that the menu can be clicked on without the application crashing
	 */
	@Test
	public void testMenuInteraction() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:filemenu");
		swinger.pause(150);
	}
	
	//**
    //* Test that the "Make this move" button works as expected
	 //*/
	/*@Test
	public void testButtonInteraction() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:NEXT");
		swinger.pause(150);
		Hole txt2 = m.getKazanRight();
		String actual = txt2.getText();
		System.out.println(actual);
		assertTrue(actual.contains("Make move button pressed")); // TODO: this test is outdated
		swinger.pause(150);
	}*/

	/**
	 * Test that the 1st button works as intended and gives the correct value in the right kazan
	 */
	@Test
	public void TestW1Correct() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W1")
		.pause(1000);
		Hole kazan = m.getKazanRight();
        int actual = kazan.getNumberOfKorgools();
        assertEquals(0, actual);
		swinger.pause(150);
	}
	
	/**
	 * Test that the 2nd button works as intended and gives the correct value in the right kazan
	 */
	@Test
	public void TestW2Correct() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W2")
		.pause(1000);
		Hole kazan = m.getKazanRight();
        int actual = kazan.getNumberOfKorgools();
        assertEquals(10, actual);
		swinger.pause(150);
	}
	
	/**
	 * Test that the 5th button works as intended and gives the correct value in the right kazan
	 */
	@Test
	public void TestW5Correct() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W5")
		.pause(1000);
		Hole kazan = m.getKazanRight();
        int actual = kazan.getNumberOfKorgools();
        assertEquals(10, actual);
		swinger.pause(150);
	}
	
	/**
	 * Test that the 8th button works as intended and gives the correct value in the right kazan
	 */
	@Test
	public void TestW8Correct() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W8")
		.pause(1000);
		Hole kazan = m.getKazanRight();
        int actual = kazan.getNumberOfKorgools();
        assertEquals(10, actual);
		swinger.pause(100);
		swinger.pause(100);
	}
	
	/**
	 * Test that the last button works as intended and gives the correct value in the right kazan
	 */
	@Test
	public void TestW9Correct() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W9")
		.pause(1000);
		Hole kazan = m.getKazanRight();
        int actual = kazan.getNumberOfKorgools();
        assertEquals(10, actual);
		swinger.pause(100);
		swinger.pause(100);
	}
	
	/**
	 * Test that clicking on the 2nd and then the third button works as intended.
	 * 
	 * Note that since the AI behaviour is random, we cannot assume anything about
	 * the precise value in the right kazan other than it is greater than or equal
	 * to 10
	 */
	@Test
	public void TestW2W3Sequence() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W2")
		.pause(1000).clickOn("name:W3")
		.pause(1000);
		Hole kazan = m.getKazanRight();
		int actual = kazan.getNumberOfKorgools();
		assertTrue(actual >= 10);
		swinger.pause(100);
	}
	
	/**
	 * Test that clicking on every button in a row works as intended to simulate an
	 * actual game. 
	 * 
	 * Note that since the AI behaviour is random, we cannot assume anything 
	 * about the precise value in the right kazan other than it is greater than or equal
	 * to 10
	 */
	@Test
	public void TestFullSequence() {
		GameManager m = new GameManager();
		Swinger swinger = Swinger.getUserWith(m.getWindow());
		swinger.pause(1000).clickOn("name:W1")
		.pause(1000).clickOn("name:W2")
		.pause(1000).clickOn("name:W3")
		.pause(1000).clickOn("name:W4")
		.pause(1000).clickOn("name:W5")
		.pause(1000).clickOn("name:W6")
		.pause(1000).clickOn("name:W7")
		.pause(1000).clickOn("name:W8")
		.pause(1000).clickOn("name:W9")
		.pause(1000);
		Hole kazan = m.getKazanRight();
		int actual = kazan.getNumberOfKorgools();
		assertTrue(actual >= 10);
		swinger.pause(100);
	}
}
