/*
 * A Swing graphical user interface for the Team Platypus Agile Project
 *
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;
import javax.swing.ImageIcon;

public class GameWindow extends JFrame {

    private static final Color bgColor = Color.LIGHT_GRAY;
    private static final Color topPanelColor = Color.GRAY;
    private HashMap<String, JButton> buttonMap;
    private JTextArea kazanRight, kazanLeft;
    BufferedImage darkButtonIcon;
    BufferedImage lightButtonIcon;
    BufferedImage altButtonIcon;

    /**
     * Construct the game window
     */
    public GameWindow() {
        setTitle("Toguz Korgol");
        loadImages();
        buttonMap = new HashMap<String, JButton>();
        setResizable(false);
        setPreferredSize(new Dimension(1280, 720));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);
        getContentPane().setLayout(new BorderLayout());
        menuSetup();
        setUpTopPanel();
        setUpBottomBar();
        setUpLowerPanel();
        pack();
        setVisible(true);
    }

    /**
     * Helper function to set up the menu bar
     * 
     * Sets up the menu from a an array of strings and adds an
     * ActionListner to each item in the menu
     */
    private void menuSetup() {
        String[] FileMenuItems = {"New Game", "Load Game", "Save Game", "CustomInput", "Preferences", "Quit"};
        JMenu FileMenu = new JMenu("File");
        FileMenu.setFont(FileMenu.getFont().deriveFont(16F));
        for (String menuItem : FileMenuItems) {
            JMenuItem item = new JMenuItem(menuItem);
            item.setName(menuItem);
            item.setFont(FileMenu.getFont());
            item.addActionListener(e -> {
                menuOnClickAction(item.getName());
            });
            FileMenu.add(item);
        }
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(FileMenu);
        menuBar.setBackground(bgColor);
        setJMenuBar(menuBar);
    }

    /**
     * Tries to load up the required image file from the specified destination
     * If it cannot find the images, prints the stack trace to the terminal
     * 
     */
    private void loadImages(){
        try{
            darkButtonIcon = ImageIO.read(new File("src/main/resources/oval_button_dark.png"));
            lightButtonIcon = ImageIO.read(new File("src/main/resources/oval_button_light.png"));
            altButtonIcon = ImageIO.read(new File("src/main/resources/oval_button_alt.png"));
        }catch(Exception e){
            System.out.print("Error opening files!");
            e.printStackTrace();
        }
    }

    /**
     * Function to set up 9 buttons on the top panel, put them in a HashMap 
     * and then add an ActionListner to each individual button
     */
    private void setUpTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(10, 10, 20, 10));//Set Padding around the Top Panel
        topPanel.setBackground(topPanelColor);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        GridLayout topButtons = new GridLayout(0, 9, 10, 10);//Set padding around invidual buttons
        topPanel.setLayout(topButtons);
        fillPanelWithButtons(topPanel, darkButtonIcon, "B");
    }

    /**
     * Function to set up 9 buttons on the bottom panel, put them in a HashMap 
     * and then add an ActionListner to each individual button
     */
    private void setUpLowerPanel() {
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));//Set Padding around the Bottom Panel
        lowerPanel.setBackground(bgColor);
        getContentPane().add(lowerPanel, BorderLayout.CENTER);
        JPanel lowerButtonPanel = new JPanel();
        GridLayout botButtons = new GridLayout(0, 9, 10, 10);//Set padding around invidual buttons
        lowerButtonPanel.setLayout(botButtons);
        lowerButtonPanel.setBackground(bgColor);
        lowerPanel.add(lowerButtonPanel, BorderLayout.SOUTH);
        lowerPanel.add(setUpKazans(), BorderLayout.CENTER);
        fillPanelWithButtons(lowerButtonPanel, lightButtonIcon, "W");
    }

    /**
     * 
     * Fills a given JPanel with ImageIcons that act as buttons
     * 
     * @param panel The Panel where the buttons are to be added
     * @param image The Image used as the button
     * @param color A single digit string to define the color of the added image
     */
    private void fillPanelWithButtons(JPanel panel, BufferedImage image, String color){
        for (int i = 1; i < 10; ++i) {
            JButton button;
            if (color.equals("W")) {
                button = new OvalButton(Integer.toString(i));
            }
            else {
                button = new JButton(Integer.toString(i), new ImageIcon(image));
            }
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.CENTER);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setName(color + i);
            buttonMap.put(button.getName(), button);
            button.setPreferredSize(new Dimension(30, 160));
            button.addActionListener(e -> {
                holeOnClickAction(button.getName());
            });
            panel.add(button);
        }
    }

    /**
     * Sets a button to be tuz by changing its color
     * 
     * @param button The button to be set to a Tuz
     */
    private void setTuz(JButton button){
        ImageIcon image = new ImageIcon(altButtonIcon);
        button.setIcon(image);
    }

    /**
     * Function to construct the bottom bar of the GUI.
     */
    private void setUpBottomBar() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(bgColor);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        BorderLayout bl = new BorderLayout();
        bottomPanel.setLayout(bl);
        JButton makeMove = new JButton("Make this move");
        makeMove.setName("NEXT");
        makeMove.addActionListener(e -> {
            genericOnClickAction(makeMove.getName());
        });
        bottomPanel.add(makeMove, BorderLayout.CENTER);
    }

    /**
     * Helper function to create a JPanel containing both Kazans
     *
     * @return JPanel containing both Kazans
     */
    private JPanel setUpKazans() {
        JPanel kazanPanel = new JPanel(new BorderLayout());
        kazanPanel.setBackground(bgColor);
        kazanPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        kazanRight = new JTextArea();
        kazanRight.setLineWrap(true);
        kazanRight.setPreferredSize(new Dimension(620, kazanPanel.getHeight() - 10));
        kazanLeft = new JTextArea();
        kazanLeft.setLineWrap(true);
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
        System.out.println(buttonId + " Clicked");
        if (buttonId.startsWith("B")) kazanRight.append("\n" + buttonId + " Clicked");
        else if (buttonId.startsWith("W")) kazanLeft.append("\n" + buttonId + " Clicked");
    }

    /**
     * onClick action for clicking any non-hole button. Inclusion of buttonId allows for identification of
     * the button that was clicked
     *
     * @param buttonId the ID of the button clicked
     */
    private void genericOnClickAction(String buttonId) {
        System.out.println(buttonId + " Clicked");
        kazanRight.append("\n" + buttonId + " Clicked");
    }

    /**
     * onClick action for clicking any menu item. Inclusion of menuItemId allows for identification of
     * the menu item that was clicked
     *
     * @param menuItemId the ID of the menu item clicked
     */
    private void menuOnClickAction(String menuItemId) {
        System.out.println(menuItemId + " Clicked");
        kazanRight.append("\n" + menuItemId + " Clicked");
        switch (menuItemId) {
            case "CustomInput":
                new CustomInputWindow(bgColor); //TODO: Prompt the user whether they want to save their game before making changes.
                break;
            case "Quit":
                dispose(); // TODO: Implement a 'save before quit' functionality.
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
        } else System.out.println("Invalid button ID");
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
}