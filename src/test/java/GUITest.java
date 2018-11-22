import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.athaydes.automaton.Swinger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import javax.swing.ImageIcon;
import static com.athaydes.automaton.assertion.AutomatonMatcher.hasText;

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
		new GameManager();
		Swinger swinger = Swinger.forSwingWindow();
		swinger.pause(200).clickOn("name:W1")
		.pause(200).clickOn("name:W2");
		swinger.pause(100);
	}
	
	@Test
	public void testLegitimateValsInKazans() {
		new GameManager();
		Swinger swinger = Swinger.forSwingWindow();
		swinger.pause(200).clickOn("name:W1")
		.pause(200).clickOn("name:W2")
		.pause(200).clickOn("name:W3");
		JTextArea txt = (JTextArea)swinger.getAt("name:leftKazan");
		int actual = Integer.parseInt(txt.getText());
		System.out.println(actual);
		assertTrue(actual>=0);
		swinger.pause(100);
	}
	
	@Test
	public void TestW2Correct() {
		new GameManager();
		Swinger swinger = Swinger.forSwingWindow();
		swinger.pause(200).clickOn("name:W2")
		.pause(800);
		JTextArea txt2 = (JTextArea)swinger.getAt("name:rightKazan");
		int actual = Integer.parseInt(txt2.getText());
		System.out.println(actual);
		assertTrue(actual==10);
		swinger.pause(100);
		swinger.pause(100);
	}
	
	@Test
	public void TestW8Correct() {
		new GameManager();
		Swinger swinger = Swinger.forSwingWindow();
		swinger.pause(200).clickOn("name:W8")
		.pause(800);
		JTextArea txt2 = (JTextArea)swinger.getAt("name:rightKazan");
		int actual = Integer.parseInt(txt2.getText());
		System.out.println(actual);
		assertTrue(actual==10);
		swinger.pause(100);
		swinger.pause(100);
	}
	
	@Test
	public void TestW9Correct() {
		new GameManager();
		Swinger swinger = Swinger.forSwingWindow();
		swinger.pause(200).clickOn("name:W9")
		.pause(800);
		JTextArea txt2 = (JTextArea)swinger.getAt("name:rightKazan");
		int actual = Integer.parseInt(txt2.getText());
		System.out.println(actual);
		assertTrue(actual==10);
		swinger.pause(100);
		swinger.pause(100);
	}
	
	@Test
	public void TestW2W3Sequence() {
		new GameManager();
		Swinger swinger = Swinger.forSwingWindow();
		swinger.pause(200).clickOn("name:W2")
		.pause(200).clickOn("name:W3")
		.pause(800);
		JTextArea txt2 = (JTextArea)swinger.getAt("name:rightKazan");
		int actual = Integer.parseInt(txt2.getText());
		System.out.println(actual);
		assertTrue(actual>=10);
		swinger.pause(100);
	}
	
	@Test
	public void TestFullSequence() {
		new GameManager();
		Swinger swinger = Swinger.forSwingWindow();
		swinger.pause(200).clickOn("name:W1")
		.pause(200).clickOn("name:W2")
		.pause(200).clickOn("name:W3")
		.pause(200).clickOn("name:W4")
		.pause(200).clickOn("name:W5")
		.pause(200).clickOn("name:W6")
		.pause(200).clickOn("name:W7")
		.pause(200).clickOn("name:W8")
		.pause(200).clickOn("name:W9")
		.pause(800);
		JTextArea txt2 = (JTextArea)swinger.getAt("name:rightKazan");
		int actual = Integer.parseInt(txt2.getText());
		System.out.println(actual);
		assertTrue(actual>=10);
		swinger.pause(100);
	}
}
