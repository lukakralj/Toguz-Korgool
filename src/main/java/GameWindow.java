import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

/**
 * A Swing graphical user interface for the Team Platypus Agile Project
 *
 */
public class GameWindow extends JFrame {

    private static final Color backgroundColor = Color.LIGHT_GRAY, topPanelColor = Color.GRAY;
    private HashMap<String, Hole> buttonMap;
    private JTextArea kazanRight, kazanLeft;
    private GameManager manager;

    /**
     * Construct the game window
     */
    GameWindow(GameManager managerIn) {
        manager = managerIn;
        setFrameProperties();
        buttonMap = new HashMap<>();
        setUpMenu();
        setUpTopPanel();
        setUpBottomBar();
        setUpLowerPanel();
        pack();
        setVisible(true);
    }
	
	GameWindow() {
        setFrameProperties();
        buttonMap = new HashMap<>();
        setUpMenu();
        setUpTopPanel();
        setUpBottomBar();
        setUpLowerPanel();
        pack();
        setVisible(true);
    }

    /**
     * Set the properties of the window
     */
    private void setFrameProperties() {
        setTitle("Toguz Korgol");
        setResizable(true);
        setPreferredSize(new Dimension(1280, 720));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);
        getContentPane().setLayout(new BorderLayout());
    }

    /**
     * Helper function to set up the menu bar
     * <p>
     * Sets up the menu from a an array of strings and adds an
     * ActionListener to each item in the menu
     */
    private void setUpMenu() {
        String[] FileMenuItems = {"CustomInput", "Quit"};
        JMenu FileMenu = new JMenu("File");
        FileMenu.setFont(FileMenu.getFont().deriveFont(16F));
        for (String menuItem : FileMenuItems) {
            JMenuItem item = new JMenuItem(menuItem);
            item.setName(menuItem);
            item.setFont(FileMenu.getFont());
            item.addActionListener(e -> menuOnClickAction(item.getName()));
            FileMenu.add(item);
        }
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(FileMenu);
        menuBar.setBackground(backgroundColor);
        setJMenuBar(menuBar);
    }

    public HashMap<String, Hole> getButtonMap() {
        return buttonMap;
    }

    /**
     * Function to set up 9 buttons on the top panel, put them in a HashMap
     * and then add an ActionListener to each individual button
     */
    private void setUpTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(10, 10, 20, 10));//Set Padding around the Top Panel
        topPanel.setBackground(topPanelColor);
        GridLayout topButtons = new GridLayout(0, 9, 10, 10);//Set padding around individual buttons
        topPanel.setLayout(topButtons);
        fillPanelWithButtons(topPanel, "B");
        getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Function to set up 9 buttons on the bottom panel, put them in a HashMap
     * and then add an ActionListener to each individual button
     */
    private void setUpLowerPanel() {
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));//Set Padding around the Bottom Panel
        lowerPanel.setBackground(backgroundColor);
        getContentPane().add(lowerPanel, BorderLayout.CENTER);
        JPanel lowerButtonPanel = new JPanel();
        GridLayout botButtons = new GridLayout(0, 9, 10, 10);//Set padding around individual buttons
        lowerButtonPanel.setLayout(botButtons);
        lowerButtonPanel.setBackground(backgroundColor);
        lowerPanel.add(lowerButtonPanel, BorderLayout.SOUTH);
        lowerPanel.add(setUpKazans(), BorderLayout.CENTER);
        fillPanelWithButtons(lowerButtonPanel, "W");
    }

    /**
     * Fills a given JPanel with ImageIcons that act as buttons
     *
     * @param panel The Panel where the buttons are to be added
     * @param color A single digit string to define the color of the added image
     */
    private void fillPanelWithButtons(JPanel panel, String color) {
        for (int i = 1; i < 10; ++i) {
            Hole button = new Hole();
            button.createAndAdd(9);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.CENTER);
            button.setName(color + i);
            buttonMap.put(button.getName(), button);
            button.setPreferredSize(new Dimension(30, 160));
            button.addActionListener(e -> holeOnClickAction(button.getName()));
            panel.add(button);
        }
    }

    /**
     * Sets a button to be tuz by changing its color
     *
     * @param hole The button to be set to a Tuz
     */
    private void setTuz(Hole hole) {
        hole.setTuz(true);
    }

    /**
     * Sets a button to be tuz by changing its color
     *
     * @param hole The button to be set to a Tuz
     */
    private void unsetTuz(Hole hole) {
        hole.setTuz(true);
    }

    /**
     * Function to construct the bottom bar of the GUI.
     */
    private void setUpBottomBar() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(backgroundColor);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        BorderLayout bl = new BorderLayout();
        bottomPanel.setLayout(bl);
        JButton makeMove = new JButton("Make this move");
        makeMove.setName("NEXT");
        makeMove.addActionListener(e -> genericOnClickAction(makeMove.getName()));
        bottomPanel.add(makeMove, BorderLayout.CENTER);
    }

    /**
     * Helper function to create a JPanel containing both Kazans
     *
     * @return JPanel containing both Kazans
     */
    private JPanel setUpKazans() {
        JPanel kazanPanel = new JPanel(new BorderLayout());
        kazanPanel.setBackground(backgroundColor);
        kazanPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        kazanRight = new JTextArea();
        kazanRight.setLineWrap(true);
        kazanRight.setPreferredSize(new Dimension(620, kazanPanel.getHeight() - 10));
        kazanLeft = new JTextArea();
        kazanLeft.setLineWrap(true);
		kazanLeft.setName("leftKazan");
        kazanLeft.setPreferredSize(new Dimension(620, kazanPanel.getHeight() - 10));
        kazanPanel.add(kazanRight, BorderLayout.EAST);
        kazanPanel.add(kazanLeft, BorderLayout.WEST);
        return kazanPanel;
    }

    /**
     * onClick action for clicking a hole. Inclusion of buttonId allows for identification of which
     * hole the player wishes to make a move from.
     *
     * @param buttonId the id of the most recently clicked button
     */
    private void holeOnClickAction(String buttonId) {
        if (buttonId.startsWith("W")) {
            kazanLeft.append("\n" + buttonId + " Clicked");
			if(manager!=null){
				manager.makeMove(buttonId.substring(1), true);
			}
        }
    }

    /**
     * onClick action for clicking any non-hole button. Inclusion of buttonId allows for identification of
     * the button that was clicked
     *
     * @param buttonId the ID of the button clicked
     */
    private void genericOnClickAction(String buttonId) {

    }

    /**
     * onClick action for clicking any menu item. Inclusion of menuItemId allows for identification of
     * the menu item that was clicked
     *
     * @param menuItemId the ID of the menu item clicked
     */
    private void menuOnClickAction(String menuItemId) {
        switch (menuItemId) {
            case "CustomInput":
				if(manager!=null){
					new CustomInputWindow(backgroundColor, manager);
				}
                break;
            case "Quit":
                dispose();
        }
    }

    /**
     * Function to set the text of a specific hole by ID.
     *
     * @param buttonId the ID of the button to set
     * @param input    the text to make the button display
     */
    public void setHoleText(String buttonId, String input) {
        JButton button = buttonMap.get(buttonId);
        if (button != null) {
            button.setText(input);
        }
    }

    /**
     * Function to set the text of the right kazan
     *
     * @param input the text to make the kazan display
     */
    public void setKazanRightText(String input) {
        kazanRight.setText(input);
    }

    /**
     * Function to set the text of the left kazan
     *
     * @param input the text to make the kazan display
     */
    public void setKazanLeftText(String input) {
        kazanLeft.setText(input);
    }

    public void makeTuz(String buttonId) {
        setTuz(buttonMap.get(buttonId));
    }

}
