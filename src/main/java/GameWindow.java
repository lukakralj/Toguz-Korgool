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

public class GameWindow extends JFrame {

    private static final Color bgColor = Color.LIGHT_GRAY;
    private JTextArea kazanRight, kazanLeft;
    BufferedImage buttonIcon;

    /**
     * Construct the game window
     */
    public GameWindow() {
        setTitle("Toguz Korgol");
        try{
            buttonIcon = ImageIO.read(new File("oval_button.png"));
        }catch(Exception e){
            System.out.print("can't read file");
        }
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
     * Function to set up Top panel containing top buttons
     */
    private void setUpTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
        topPanel.setBackground(bgColor);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        GridLayout topButtons = new GridLayout(0, 9, 10, 10);
        topPanel.setLayout(topButtons);
        for (int i = 9; i > 0; --i) {
            JButton button = new JButton(new ImageIcon(buttonIcon));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setToolTipText(Integer.toString(i));
            button.setName("T" + i);
            button.setPreferredSize(new Dimension(30, 160));
            button.addActionListener(e -> {
                holeOnClickAction(button);
            });
            topPanel.add(button);
        }
    }

    /**
     * Function to set up Lower panel containing bottom buttons and kazans
     */
    private void setUpLowerPanel() {
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        lowerPanel.setBackground(bgColor);
        getContentPane().add(lowerPanel, BorderLayout.CENTER);
        JPanel lowerButtonPanel = new JPanel();
        GridLayout botButtons = new GridLayout(0, 9, 10, 10);
        lowerButtonPanel.setLayout(botButtons);
        lowerButtonPanel.setBackground(bgColor);
        lowerPanel.add(lowerButtonPanel, BorderLayout.SOUTH);
        lowerPanel.add(setUpKazans(), BorderLayout.CENTER);
        for (int i = 1; i < 10; ++i) {
            JButton button = new JButton(new ImageIcon(buttonIcon));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            button.setToolTipText(Integer.toString(i));
            button.setName("B" + i);
            button.setPreferredSize(new Dimension(30, 160));
            button.addActionListener(e -> {
                holeOnClickAction(button);
            });
            lowerButtonPanel.add(button);
        }
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
     * @param buttonClicked the button clicked
     */
    private void holeOnClickAction(JButton buttonClicked) {
        String buttonId = buttonClicked.getName();
        System.out.println(buttonId + " Clicked");
        if (buttonId.startsWith("B")) kazanRight.append("\n" + buttonId + " Clicked");
        else if (buttonId.startsWith("T")) kazanLeft.append("\n" + buttonId + " Clicked");
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
}