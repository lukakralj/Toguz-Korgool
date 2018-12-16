import com.athaydes.automaton.Speed;
import gui.GameWindow;
import gui.Hole;
import logic.AnimationController;
import logic.GameManager;
import org.junit.*;

import static org.junit.Assert.*;
import com.athaydes.automaton.Swinger;
import static com.athaydes.automaton.assertion.AutomatonMatcher.hasText;


/**
 * Class that contains tests for the GUI.
 *
 */
public class GUITest {
	private GameWindow testWindow;
	private Swinger swinger;

	@Before
	public void setUp() {
		// Speed up animations.
		testWindow = new GameWindow(null);
		AnimationController.setRunTime(5);
		swinger = Swinger.getUserWith(testWindow);
		swinger.pause(250);
		Swinger.setDEFAULT(Speed.VERY_FAST);
	}

	@After
	public void tearDown() {
		swinger.pause(250);
		testWindow = null;
	}


	/**
	 * Test that the GUI sets up correctly without crashing.
	 */
	@Test
	public void GUISetup(){
		assertNotNull(testWindow);
	}

	/**
	 * Test that the hashmap is correctly initialised to a size of 18.
	 */
	@Test
	public void TestHashMapHasCorrectSize(){
		assertEquals("Hashmap incorrectly initialised", 18, testWindow.getButtonMap().size());
	}

	/**
	 * Test that the hasmap of Buttons contains the correct contents.
	 */
	@Test
	public void TestHashMapContents(){
		assertEquals("Hashmap has incorrect contents", "B1", testWindow.getButtonMap().get("B1").getName());
		assertEquals("Hashmap has incorrect contents", "B9", testWindow.getButtonMap().get("B9").getName());//edge case
		assertEquals("Hashmap has incorrect contents", "B3", testWindow.getButtonMap().get("B3").getName());
		assertEquals("Hashmap has incorrect contents", "B5", testWindow.getButtonMap().get("B5").getName());
		assertEquals("Hashmap has incorrect contents", "B7", testWindow.getButtonMap().get("B7").getName());
		assertEquals("Hashmap has incorrect contents", "W1", testWindow.getButtonMap().get("W1").getName());
		assertEquals("Hashmap has incorrect contents", "W9", testWindow.getButtonMap().get("W9").getName());//edge case
		assertEquals("Hashmap has incorrect contents", "W4", testWindow.getButtonMap().get("W4").getName());
	}

	/**
	 * Test that user can press all of the white side buttons.
	 */
	@Test
	public void testCorrectButtonInteraction() {
		swinger.pause(250).clickOn("name:W9");
		swinger.pause(250).clickOn("name:W8");
		swinger.pause(250).clickOn("name:W7");
		swinger.pause(250).clickOn("name:W6");
		swinger.pause(250).clickOn("name:W5");
		swinger.pause(250).clickOn("name:W4");
		swinger.pause(250).clickOn("name:W3");
		swinger.pause(250).clickOn("name:W2");
		swinger.pause(250).clickOn("name:W1");
	}


	/**
	 * Test that the menu can be clicked on without the application crashing.
	 */
	@Test
	public void testMenuInteraction() {
		swinger.pause(250).clickOn("name:fileMenu");
		swinger.pause(150);
	}

	/**
	 * Test that the help menu can be correctly opened.
	 */
	@Test
	public void testHelpOption() {
		swinger.pause(250).clickOn("name:fileMenu");
		swinger.pause(250).clickOn("name:Help");
		swinger.pause(250).clickOn("text:OK");
	}

	/**
	 * Test setting the Tuz on either side, and resetting the Tuzes.
	 */
	@Test
	public void testSetAndResetTuz() {
		testWindow.setTuz("W1");
		testWindow.setTuz("B1");
		testWindow.resetTuzes();
	}

	/**
	 * Test that the central info label works as intended.
	 */
	@Test
	public void testDisplay() {
		testWindow.displayMessage("test");
		assertThat(swinger.getAt("name:infoLabel"), hasText("<html><div style='text-align: center; color: white; -webkit-text-stroke-width: 1px;'>test</div></html>"));
	}

	/**
	 * Test populating each Hole/Kazan with Korgools
	 */
	@Test
	public void testPopulatingWithKorgools() {
		for (int i = 1; i < 10; ++i) {
			testWindow.populateWithKorgools("B" + i, 1);
			testWindow.populateWithKorgools("W" + i, 1);
		}
		testWindow.populateWithKorgools("leftKazan", 1);
		Assert.assertEquals(testWindow.getKazanLeft().getNumberOfKorgools(), 1);
		testWindow.populateWithKorgools("left", 1);
		testWindow.populateWithKorgools("rightKazan", 1);
		Assert.assertEquals(testWindow.getKazanRight().getNumberOfKorgools(), 1);
		testWindow.populateWithKorgools("right", 1);
	}



}
