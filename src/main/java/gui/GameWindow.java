package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

import javafx.scene.layout.TilePane;
import logic.AnimationController;
import logic.GameManager;

/*
 * Presents the main game window for the user
 *
 * @version 0.1 26/11/2018
 */
public class GameWindow extends JFrame {

    private static BufferedImage lightHole;
    private static BufferedImage darkHole;
    private static BufferedImage blueKorgool;
    private static BufferedImage redKorgool;

    private HashMap<String, Hole> buttonMap;
    private Hole kazanRight, kazanLeft;
    private Hole rightTuz, leftTuz;
    private JPanel root;
    private JLayeredPane layeredPane;
    private HashMap<String, Hole> kazans;
    private GameManager manager;
    private JLabel infoLabel;
    private JSlider slider;
    private boolean midGameMessageDisplayed;


    /**
     * Construct the game window
     */
    public GameWindow(GameManager managerIn) {
        if (lightHole == null || darkHole == null) {
            loadImages();
        }
        root = new JPanel();
        manager = managerIn;
        midGameMessageDisplayed = false;
        setFrameProperties();
        buttonMap = new HashMap<>();
        kazans = new HashMap<>();
        setUpMenu();
        setUpTopPanel();
        setUpTuzMarkers();
        setUpKazans();
        setUpLowerPanel();
        layeredPane = new JLayeredPane();
        root.setLocation(0, 0);
        layeredPane.add(root, new Integer(0));

        getContentPane().add(layeredPane);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeWindow();
            }
        });

        addWindowStateListener(e -> {
            revalidate();
            resizeWindow();
            repaint();
        });
        pack();
        setVisible(true);

        setState(JFrame.ICONIFIED);
        setState(JFrame.NORMAL);

    }

    public GameWindow() {
        this(null);
    }

    private static void loadImages() {
        try {
            darkHole = ImageIO.read(new File("src/main/resources/dark_hole.jpg"));
            lightHole = ImageIO.read(new File("src/main/resources/light_hole.jpg"));
            blueKorgool = ImageIO.read(new File("src/main/resources/blue_korgool.png"));
            redKorgool = ImageIO.read(new File("src/main/resources/red_korgool.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resizeWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int minW = (int) (screenSize.getWidth() * 0.7);
        int minH = (int) (screenSize.getHeight() * 0.75);
        int newW = getContentPane().getSize().width < minW ? minW : getContentPane().getSize().width;
        int newH = getContentPane().getSize().height < minH ? minH : getContentPane().getSize().height;
        root.setSize(newW, newH);
        slider.setPreferredSize(new Dimension(root.getSize().width / 2, 40));
    }

    /**
     *
     * @return Layered pane which is needed for animations.
     */
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    /**
     * Set the properties of the window
     */
    private void setFrameProperties() {
        setTitle("Toguz Korgool");
        setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int minW = (int) (screenSize.getWidth() * 0.7) + 50;
        int minH = (int) (screenSize.getHeight() * 0.75) + 80;
        //int minH = (int) (screenSize.getHeight() * 0.75) + 50;
        setPreferredSize(new Dimension(minW, minH));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        root.setLayout(new BorderLayout());
    }

    /**
     * Helper function to set up the menu bar.
     *
     * Sets up the menu from a an array of strings and adds an
     * ActionListener to each item in the menu.
     */
    private void setUpMenu() {
        String[] FileMenuItems = {"New Game", "Save Game", "Load Game", "Custom Input", "Help", "Quit Game"};
        JMenu FileMenu = new JMenu("File");
        FileMenu.setName("fileMenu");
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
        setJMenuBar(menuBar);
    }

    /**
     * @return The map containing all the ButtonIds
     */
    public HashMap<String, Hole> getButtonMap() {
        return buttonMap;
    }

    public HashMap<String, Hole> getKazans() {
        return kazans;
    }

    /**
     * Function to set up 9 buttons on the top panel, put them in a HashMap
     * and then add an ActionListener to each individual button.
     */
    private void setUpTopPanel() {
        JPanel topPanel = new TiledPanel(TiledPanel.BLACK);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));//Set Padding around the Top Panel
        topPanel.setOpaque(false);
        GridLayout topButtons = new GridLayout(0, 9, 10, 10);//Set padding around invidual buttons
        topPanel.setLayout(topButtons);
        fillPanelWithButtons(topPanel, "B");
        root.add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Function to set up 9 buttons on the bottom panel, put them in a HashMap
     * and then add an ActionListner to each individual button.
     */
    private void setUpLowerPanel() {
        JPanel buttonsPanel = new TiledPanel(TiledPanel.WHITE);
        buttonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));//Set Padding around the Bottom Panel
        buttonsPanel.setOpaque(false);
        GridLayout bottomButtons = new GridLayout(0, 9, 10, 10);//Set padding around individual buttons
        buttonsPanel.setLayout(bottomButtons);
        fillPanelWithButtons(buttonsPanel, "W");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonsPanel, BorderLayout.CENTER);

        JPanel sliderPanel = new TiledPanel(TiledPanel.NEUTRAL);
        sliderPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel sliderInfo = new JLabel("<html><font size='3' color='white' face='Monaco'>Speed of animations:</font></html>");
        sliderPanel.add(sliderInfo);

        slider = new JSlider(1, 2000, 250);
        slider.addChangeListener(e -> AnimationController.setRunTime(slider.getValue()));
        Hashtable<Integer, Component> labels = new Hashtable<>();
        labels.put(1, new JLabel("<html><font size='3' color='white' face='Monaco'>Lightning</font></html>"));
        labels.put(250, new JLabel("<html><font size='3' color='white' face='Monaco'>Default</font></html>"));
        labels.put(2000, new JLabel("<html><font size='3' color='white' face='Monaco'>Relax - it'll take a while</font></html>"));
        slider.setLabelTable(labels);
        slider.setOpaque(false);
        slider.setPaintLabels(true);
        sliderPanel.add(slider);

        panel.add(sliderPanel, BorderLayout.SOUTH);
        root.add(panel, BorderLayout.SOUTH);
    }

    /**
     * Fills a given JPanel with ImageIcons that act as buttons.
     *
     * @param panel The Panel where the buttons are to be added
     * @param color A single digit string to define the color of the added image
     */
    private void fillPanelWithButtons(JPanel panel, String color) {
        BufferedImage img;
        Color col;
        if (color.equals("W")) {
            img = lightHole;
            col = Color.BLACK;
        } else {
            img = darkHole;
            col = Color.WHITE;
        }
        for (int i = 1; i < 10; ++i) {
            Hole button = new Hole(OvalButton.SHAPE_CAPSULE, OvalButton.VERTICAL, false, img);
            button.setTextColor(col);
            button.setName(color + i);
            buttonMap.put(button.getName(), button);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            button.setPreferredSize(new Dimension(30, (int)(screenSize.getHeight() * 0.2)));
            button.addActionListener(e -> holeOnClickAction(button.getName()));
            if (color.equals("B")) {
                button.setEnabled(false);
            }

            panel.add(button);
        }
    }

    /**
     * Create and add holes for red tuz-marking korgools.
     */
    private void setUpTuzMarkers() {
        JPanel left = createSingleMarker("left");
        JPanel right = createSingleMarker("right");

        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.EAST);
    }

    /**
     * Creates one hole for a red tuz-marking korgool.
     *
     * @param side Should be "left" or "right".
     * @return A panel with the marker.
     */
    private JPanel createSingleMarker(String side) {
        Hole tuz;
        if (side.equals("left")) {
            leftTuz = new Hole(OvalButton.SHAPE_OVAL, OvalButton.VERTICAL, true, darkHole);
            tuz = leftTuz;
        } else {
            rightTuz = new Hole(OvalButton.SHAPE_OVAL, OvalButton.VERTICAL, true, lightHole);
            tuz = rightTuz;
        }

        tuz.setName(side + "Tuz");
        tuz.setEnabled(false);
        tuz.setBorder(new EmptyBorder(10, 10, 10, 10));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        tuz.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.10), 200));
        JPanel panel;
        if (side.equals("left")) {
            panel = new TiledPanel(TiledPanel.BLACK);
        } else {
            panel = new TiledPanel(TiledPanel.WHITE);
        }
        panel.setLayout(new GridLayout(3, 1));
        panel.setOpaque(false);
        panel.add(Box.createVerticalGlue());
        panel.add(tuz);
        panel.add(Box.createVerticalGlue());
        panel.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.10), 200));

        return panel;
    }

    /**
     * Helper function to create a JPanel containing both Kazans
     */
    private void setUpKazans() {
        JPanel kazanPanel = new JPanel(new BorderLayout());
        kazanPanel.setOpaque(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JPanel kazanLeftPanel = new TiledPanel(TiledPanel.BLACK);
        kazanLeftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        kazanLeftPanel.setLayout(new BorderLayout());
        kazanLeftPanel.setOpaque(false);
        kazanLeftPanel.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.2), 300));
        kazanLeft = new Hole(OvalButton.SHAPE_CAPSULE, OvalButton.HORIZONTAL, true, darkHole);
        kazanLeft.setTextColor(Color.WHITE);
        kazanLeft.setColorBorderNormal(Color.BLACK);
        kazanLeft.setName("leftKazan");
        kazanLeft.setEnabled(false);
        kazanLeft.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.2), 300));
        kazanLeftPanel.add(kazanLeft, BorderLayout.CENTER);

        JPanel kazanRightPanel = new TiledPanel(TiledPanel.WHITE);
        kazanRightPanel.setOpaque(false);
        kazanRightPanel.setLayout(new BorderLayout());
        kazanRightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        kazanRightPanel.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.2), 300));
        kazanRight = new Hole(OvalButton.SHAPE_CAPSULE, OvalButton.HORIZONTAL, true, lightHole);
        kazanRight.setTextColor(Color.BLACK);
        kazanRight.setColorBorderNormal(Color.BLACK);
        kazanRight.setName("rightKazan");
        kazanRight.setEnabled(false);
        kazanRight.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.2), 300));
        kazanRightPanel.add(kazanRight, BorderLayout.CENTER);

        kazans.put(kazanRight.getName(), kazanRight);
        kazans.put(kazanLeft.getName(), kazanLeft);

        JPanel infoPanel = new TiledPanel(TiledPanel.HALFSIES);
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setOpaque(false);
        infoLabel = new JLabel();
        infoLabel.setPreferredSize(new Dimension((int)(screenSize.getWidth() * 0.40), (int)(screenSize.getHeight() * 0.25)));
        infoLabel.setFont(new Font("Monaco", Font.BOLD, 20));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setVerticalAlignment(SwingConstants.CENTER);
        infoLabel.setOpaque(false);
        infoLabel.setBackground(Color.WHITE);
        infoLabel.setName("infoLabel");
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        kazanPanel.add(kazanLeftPanel, BorderLayout.WEST);
        kazanPanel.add(infoPanel, BorderLayout.CENTER);
        kazanPanel.add(kazanRightPanel, BorderLayout.EAST);
        root.add(kazanPanel, BorderLayout.CENTER);
    }

    /**
     * onClick action for clicking a hole. Inclusion of buttonId allows for identification of which
     * hole the player wishes to make a move from.
     *
     * @param buttonId the id of the most recently clicked button
     */
    private void holeOnClickAction(String buttonId) {
        if (buttonId.startsWith("W")) {
            if (manager != null) {
                if (midGameMessageDisplayed) {
                    displayMessage("");
                    midGameMessageDisplayed = false;
                }
                manager.makeMove(buttonId.substring(1), true);
            }
        }
    }

    /**
     * onClick action for clicking any menu item. Inclusion of menuItemId allows for identification of
     * the menu item that was clicked
     *
     * @param menuItemId the ID of the menu item clicked
     */
    private void menuOnClickAction(String menuItemId) {
        switch (menuItemId) {
            case "New Game":
                if (AnimationController.instance().isRunning()) {
                    JOptionPane.showMessageDialog(this, "Animations are currently running. Please wait for them to finish.", "", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                int newGameDialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to start a new game?");
                if (newGameDialogResult == JOptionPane.YES_OPTION) {
                    manager.loadGame("src\\main\\resources\\newGameFile1.csv", "src\\main\\resources\\newGameFile2.csv");
                    displayMessage("Started a new game");
                    midGameMessageDisplayed = true;
                }
                break;
            case "Custom Input":
                if (manager != null) new CustomInputWindow(manager);
                break;
            case "Save Game":
                if (AnimationController.instance().isRunning()) {
                    JOptionPane.showMessageDialog(this, "Animations are currently running. Please wait for them to finish.", "", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                int saveGameDialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to save the game?");
                if (saveGameDialogResult == JOptionPane.YES_OPTION) {
                    manager.saveGame();
                    displayMessage("Saved the current configuration");
                    midGameMessageDisplayed = true;
                }
                break;
            case "Load Game":
                if (AnimationController.instance().isRunning()) {
                    JOptionPane.showMessageDialog(this, "Animations are currently running. Please wait for them to finish.", "", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                int loadGameDialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to load the latest save state?");
                if (loadGameDialogResult == JOptionPane.YES_OPTION) {
                    manager.loadGame("src\\main\\resources\\saveFile.csv", "src\\main\\resources\\saveFile2.csv", "src\\main\\resources\\saveFile3.csv", "src\\main\\resources\\saveFile4.csv");
                    displayMessage("Loaded the last saved configuration");
                    midGameMessageDisplayed = true;
                }
                break;
            case "Help":
                JOptionPane.showMessageDialog(this, getGameRules(), "How To Play", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Quit Game":
                int quitGameResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?");
                if (quitGameResult == JOptionPane.YES_OPTION) System.exit(0);
        }
    }

    /**
     * Set the number of korgools in this hole. The method will remove all korgools currently in this hole
     * and create new ones.
     *
     * @param holeId        Id of the hole.
     * @param numOfKorgools Number of korgools that we want to have in this hole.
     */
    public void populateWithKorgools(String holeId, int numOfKorgools) {
        Hole hole;
        if (holeId.equals("left") || holeId.equals("leftKazan")) {
            hole = kazanLeft;
        } else if (holeId.equals("right") || holeId.equals("rightKazan")) {
            hole = kazanRight;
        } else {
            hole = buttonMap.get(holeId);
        }

        hole.emptyHole();
        hole.createAndAdd(numOfKorgools, blueKorgool);
        hole.repaint();
    }

    /**
     * Place two new korgools in the tuz markers.
     */
    public void resetTuzes() {
        leftTuz.emptyHole();
        rightTuz.emptyHole();

        Korgool left = new Korgool(leftTuz, redKorgool);
        left.setName("leftTuzKorgool");
        leftTuz.addKorgool(left);

        Korgool right = new Korgool(rightTuz, redKorgool);
        right.setName("rightTuzKorgool");
        rightTuz.addKorgool(right);
    }

    /**
     * Remove korgool from tuz marker and place it to the hole specified. No animations will be triggered.
     *
     * @param holeId Id of the hole we are marking as a tuz.
     */
    public void setTuz(String holeId) {
        String name;
        if (holeId.startsWith("B")) { //white player claimed tuz
            name = "right";
            rightTuz.emptyHole();
        } else {
            name = "left";
            leftTuz.emptyHole();
        }
        Korgool k = new Korgool(buttonMap.get(holeId), redKorgool);
        k.setName(name + "TuzKorgool");
        buttonMap.get(holeId).addKorgool(k);
    }

    /**
     * Display message in the middle of the board.
     *
     * @param message Message to display.
     */
    public void displayMessage(String message) {
        infoLabel.setText("<html><div style='text-align: center; color: white; -webkit-text-stroke-width: 1px;'>" + message + "</div></html>");
    }

    /**
     * @return Kazan of the black player.
     */
    public Hole getKazanLeft() {
        return kazanLeft;
    }

    /**
     * @return Kazan of the left player.
     */
    public Hole getKazanRight() {
        return kazanRight;
    }

    /**
     * @return Tuz marker hole for the black player.
     */
    public Hole getLeftTuz() {
        return leftTuz;
    }

    /**
     * @return Tuz marker hole for the white player.
     */
    public Hole getRightTuz() {
        return rightTuz;
    }

    /**
     * @return Game Rules in html format.
     * Source: https://en.wikipedia.org/wiki/Toguz_korgol
     */
    private String getGameRules() {
        return "" +
                "<html><div style='width: " + 400 + "px;'>" +
                "<p>Players move alternately. A move consists of taking stones from a hole and distributing them to other holes. On his/her turn, a player takes all the stones of one of his holes, which is not a tuz (see below), and distributes them anticlockwise, one by one, into the following holes. The first stone must be dropped into the hole which was just emptied. However, if the move began from a hole which contained only one stone, this stone is put into the next hole.</p>"
                + "<p><br>If the last stone falls into a hole on the opponent's side, and this hole then contains an even number of stones, these stones are captured and stored in the player's kazan. If the last stone falls into a hole of the opponent, which then has three stones, the hole is marked as a \"tuz\" (\"salt\" in Kyrgyz). There are a few restrictions on creating a tuz:</p>" +
                "<ol>" +
                "<li>A player may create only one tuz in each game.</li>" +
                "<li>The last hole of the opponent (his ninth or rightmost hole) cannot be turned into a tuz.</li>" +
                "<li>A tuz cannot be made if it is symmetrical to the opponent's one (for instance, if the opponent's third hole is a tuz, you cannot turn your third hole into one). It is permitted to make such a move, but it wouldn't create a tuz.</li>" +
                "</ol>" +
                "<p>The stones that fall into a tuz are captured by its owner. He may transfer its contents at any time to his kazan. The game ends when a player can't move at his turn because all the holes on his side, which are not tuz, are empty.</p>" +
                "<p><br>When the game is over, the remaining stones which are not yet in a kazan or in a tuz are won by the player on whose side they are. The winner is the player who, at the end of the game, has captured more stones in their tuz and their kazan. When each player has 81 stones, the game is a draw.</p>" +
                ""
                + "</div></html>";
    }

    /**
     *
     * @return Currently displayed message.
     */
    public String getMessage() {
        return infoLabel.getText();
    }
}
