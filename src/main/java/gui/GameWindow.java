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
import logic.GameManager;

/*
 * Presents the main game window for the user
 *
 * @version 0.1 26/11/2018
 */
public class GameWindow extends JFrame {

    private HashMap<String, Hole> buttonMap;
    private Hole kazanRight, kazanLeft;
    private Hole rightTuz, leftTuz;
    private JPanel root;
    private JLayeredPane layeredPane;
    private HashMap<String, Hole> kazans;
    private GameManager manager;
    private JLabel infoLabel;

    /**
     * Construct the game window
     */
    public GameWindow(GameManager managerIn) {
        root = new JPanel();
        manager = managerIn;
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
    }

	public GameWindow() {
        this(null);
    }

    private void resizeWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int minW = (int) (screenSize.getWidth() * 0.7);
        int minH = (int) (screenSize.getHeight() * 0.75);
        int newW = getContentPane().getSize().width < minW ? minW : getContentPane().getSize().width;
        int newH = getContentPane().getSize().height < minH ? minH : getContentPane().getSize().height;
        root.setSize(newW, newH);
    }

    /**
     * Get layered pane which is needed for animations.
     *
     * @return
     */
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    /**
     * Set the properties of the window
     */
    private void setFrameProperties() {
        setTitle("Toguz Korgol");
        setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int minW = (int) (screenSize.getWidth() * 0.7) + 50;
        int minH = (int) (screenSize.getHeight() * 0.75) + 80;
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
        String[] FileMenuItems = {"CustomInput", "NewGame", "Save", "Load", "Quit"};
        JMenu FileMenu = new JMenu("File");
		FileMenu.setName("filemenu");
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
        JPanel lowerPanel = new TiledPanel(TiledPanel.WHITE);
        lowerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));//Set Padding around the Bottom Panel
        lowerPanel.setOpaque(false);
        GridLayout bottomButtons = new GridLayout(0, 9, 10, 10);//Set padding around individual buttons
        lowerPanel.setLayout(bottomButtons);
        fillPanelWithButtons(lowerPanel, "W");
        root.add(lowerPanel, BorderLayout.SOUTH);
    }

    /**
     * Fills a given JPanel with ImageIcons that act as buttons.
     *
     * @param panel The Panel where the buttons are to be added
     * @param color A single digit string to define the color of the added image
     */
    private void fillPanelWithButtons(JPanel panel, String color) {
        for (int i = 1; i < 10; ++i) {
            Hole button = new Hole(OvalButton.SHAPE_CAPSULE, OvalButton.VERTICAL, false);
            button.setName(color + i);
            buttonMap.put(button.getName(), button);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            button.setPreferredSize(new Dimension(30, (int) (screenSize.getHeight() * 0.2)));
            button.addActionListener(e -> holeOnClickAction(button.getName()));
            if (color.equals("B")) {
                button.setEnabled(false);
            }

            panel.add(button);
        }
    }

    private void setUpTuzMarkers() {
        JPanel left = createSingleMarker("left");
        JPanel right = createSingleMarker("right");

        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.EAST);
    }

    private JPanel createSingleMarker(String side) {
        Hole tuz;
        if (side.equals("left")) {
            leftTuz = new Hole(OvalButton.SHAPE_OVAL, OvalButton.VERTICAL, true);
            tuz = leftTuz;
        }
        else {
            rightTuz = new Hole(OvalButton.SHAPE_OVAL, OvalButton.VERTICAL, true);
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
        }
        else {
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
     *
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
        kazanLeft = new Hole(OvalButton.SHAPE_CAPSULE, OvalButton.HORIZONTAL, true);
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
        kazanRight = new Hole(OvalButton.SHAPE_CAPSULE, OvalButton.HORIZONTAL,true);
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
        infoLabel.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.40), (int) (screenSize.getHeight() * 0.25)));
        infoLabel.setFont(new Font("Monaco", Font.BOLD, 20));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setVerticalAlignment(SwingConstants.CENTER);
        infoLabel.setOpaque(false);
        infoLabel.setBackground(Color.WHITE);
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
			if(manager!=null){
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
			case "NewGame":
				JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new game?");
				manager.loadGame("src\\main\\resources\\newGameFile1.csv","src\\main\\resources\\newGameFile2.csv");
                break;
            case "CustomInput":
				if(manager!=null){
                    new CustomInputWindow(Color.LIGHT_GRAY, manager);
				}
                break;
            case "Save":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to save the game?");
                manager.saveGame();
                break;
            case "Load":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to load the latest save state?");
                manager.loadGame("src\\main\\resources\\saveFile.csv","src\\main\\resources\\saveFile2.csv","src\\main\\resources\\saveFile3.csv","src\\main\\resources\\saveFile4.csv");
                break;
            case "Quit":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?");
                dispose();
        }
    }

    /**
     * Set the number of korgools in this hole. The method will remove all korgools currently in this hole
     * and create new ones.
     *
     * @param holeId Id of the hole.
     * @param numOfKorgools Number of korgools that we want to have in this hole.
     */
    public void populateWithKorgools(String holeId, int numOfKorgools) {
        Hole hole;
        if (holeId.equals("left") || holeId.equals("leftKazan")) {
            hole = kazanLeft;
        }
        else if (holeId.equals("right") || holeId.equals("rightKazan")) {
            hole = kazanRight;
        }
        else {
            hole = buttonMap.get(holeId);
        }

        hole.emptyHole();
        hole.createAndAdd(numOfKorgools);
        hole.repaint();
    }

    /**
     * Place two new korgools in the tuz markers.
     */
    public void resetTuzes() {
        leftTuz.emptyHole();
        rightTuz.emptyHole();

        Korgool left = new Korgool(leftTuz, Color.RED);
        left.setName("leftTuzKorgool");
        leftTuz.addKorgool(left);

        Korgool right = new Korgool(rightTuz, Color.RED);
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
        }
        else {
            name = "left";
            leftTuz.emptyHole();
        }
        Korgool k = new Korgool(buttonMap.get(holeId), Color.RED);
        k.setName(name + "TuzKorgool");
        buttonMap.get(holeId).addKorgool(k);
    }

    /**
     * Display message in the middle of the board.
     *
     * @param message Message to display.
     */
    public void displayMessage(String message) {
        infoLabel.setText(message);
    }

    /**
     *
     * @return Kazan of the black player.
     */
	public Hole getKazanLeft() {
        return kazanLeft;
    }

    /**
     *
     * @return Kazan of the left player.
     */
	public Hole getKazanRight() {
        return kazanRight;
    }

    /**
     *
     * @return Tuz marker hole for the black player.
     */
    public Hole getLeftTuz() {
        return leftTuz;
    }

    /**
     *
     * @return Tuz marker hole for the white player.
     */
    public Hole getRightTuz() {
        return rightTuz;
    }
	
	public void setButtonMap(HashMap<String, Hole> buttonMap1){
		buttonMap=buttonMap1;
	}
	
	public void setKazans(HashMap<String, Hole> kazan1){
		kazans=kazan1;
	}

}
