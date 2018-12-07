package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import logic.AnimationController;
import logic.GameManager;

/*
 * Presents the main game window for the user
 *
 * @version 0.1 26/11/2018
 */
public class GameWindow extends JFrame {

    private static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY, TOP_PANEL_COLOR = Color.GRAY;
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
        int newW = getContentPane().getSize().width < 1401 ? 1401 : getContentPane().getSize().width;
        int newH = getContentPane().getSize().height < 677 ? 677 : getContentPane().getSize().height;
        root.setSize(newW, newH);
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    /**
     * Set the properties of the window
     */
    private void setFrameProperties() {
        setTitle("Toguz Korgol");
        setResizable(true);
        setPreferredSize(new Dimension(1501, 790));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        root.setBackground(BACKGROUND_COLOR);
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
        menuBar.setBackground(BACKGROUND_COLOR);
        setJMenuBar(menuBar);
    }

    /**
     * @return The map containing all the ButtonIds
     */
    public HashMap<String, Hole> getButtonMap() {
        return buttonMap;
    }

    /**
     * Function to set up 9 buttons on the top panel, put them in a HashMap
     * and then add an ActionListener to each individual button.
     */
    private void setUpTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));//Set Padding around the Top Panel
        topPanel.setBackground(TOP_PANEL_COLOR);
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
        JPanel lowerPanel = new JPanel();
        lowerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));//Set Padding around the Bottom Panel
        lowerPanel.setBackground(BACKGROUND_COLOR);
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
            Hole button = new Hole(false);
            button.setName(color + i);
            buttonMap.put(button.getName(), button);
            button.setPreferredSize(new Dimension(30, 200));
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
            leftTuz = new Hole(true);
            tuz = leftTuz;
        }
        else {
            rightTuz = new Hole(true);
            tuz = rightTuz;
        }

        tuz.setName(side + "Tuz");
        tuz.setEnabled(false);
        tuz.setBorder(new EmptyBorder(10, 10, 10, 10));
        tuz.setPreferredSize(new Dimension(200, 200));

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(new JPanel());
        panel.add(tuz);
        panel.add(new JPanel());
        panel.setMinimumSize(new Dimension(200, 200));
        return panel;
    }

    /**
     * Helper function to create a JPanel containing both Kazans
     *
     */
    private void setUpKazans() {
        JPanel kazanPanel = new JPanel(new BorderLayout());
        kazanPanel.setBackground(BACKGROUND_COLOR);
        kazanPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        kazanLeft = new Hole(true);
        kazanLeft.setColorBorderNormal(new Color(160,82,45));
        kazanLeft.setName("leftKazan");
        kazanLeft.setEnabled(false);
        kazanLeft.setPreferredSize(new Dimension(400,300));
        kazanLeft.setEnabled(false);

        kazanRight = new Hole(true);
        kazanRight.setColorBorderNormal(new Color(160,82,45));
        kazanRight.setName("rightKazan");
        kazanRight.setEnabled(false);
        kazanRight.setPreferredSize(new Dimension(400,300));
        kazanRight.setEnabled(false);

        kazans.put(kazanRight.getName(), kazanRight);
        kazans.put(kazanLeft.getName(), kazanLeft);

        infoLabel = new JLabel();
        infoLabel.setPreferredSize(new Dimension(300, 300));
        infoLabel.setFont(new Font("Monaco", Font.BOLD, 20));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setVerticalAlignment(SwingConstants.CENTER);

        kazanPanel.add(kazanLeft, BorderLayout.WEST);
        kazanPanel.add(infoLabel, BorderLayout.CENTER);
        kazanPanel.add(kazanRight, BorderLayout.EAST);
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
				loadGame("src\\main\\java\\newGameFile1.csv","src\\main\\java\\newGameFile2.csv");
                break;
            case "CustomInput":
				if(manager!=null){
					new CustomInputWindow(BACKGROUND_COLOR, manager);
				}
                break;
            case "Save":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to save the game?");
                saveGame();
                break;
            case "Load":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to load the latest save state?");
                loadGame("src\\main\\java\\saveFile.csv","src\\main\\java\\saveFile2.csv");
                break;
            case "Quit":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?");
                dispose();
        }
    }

    private PrintWriter getPrintWriter(String filetoOpen) throws FileNotFoundException{
        File saveFile=new File(filetoOpen);
        FileOutputStream fos=new FileOutputStream(saveFile);
        PrintWriter pw=new PrintWriter(fos);
        return pw;
    }

    private void closePrintWriter(PrintWriter pw){
        pw.flush();
        pw.close();
    }

    private void saveGame(){
        try{
            PrintWriter pw = getPrintWriter("src\\main\\java\\saveFile.csv");
            for(Map.Entry<String,Hole> entries :buttonMap.entrySet()){
                pw.println(entries.getKey()+","+entries.getValue().getNumberOfKorgools()+","+entries.getValue().isTuz());
            }
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }
		
		try{
            PrintWriter pw = getPrintWriter("src\\main\\java\\saveFile2.csv");
            for(Map.Entry<String,Hole> entries :kazans.entrySet()){
                pw.println(entries.getKey()+","+entries.getValue().getNumberOfKorgools());
            }
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadGame(String file1, String file2){
        try{
            File toRead=new File(file1);
            FileInputStream fis=new FileInputStream(toRead);
    
            Scanner sc=new Scanner(fis);
    
            String placeholder = "";
            while(sc.hasNextLine()){
                placeholder=sc.nextLine();
                StringTokenizer st = new StringTokenizer(placeholder,",",false);
                Hole button = buttonMap.get(st.nextToken());
                populateWithKorgools(button.getName(), Integer.valueOf(st.nextToken()));
				if(st.nextToken().equals("true")){
					button.setTuz(true);
				}else{
                    button.setTuz(false);
				}
            }
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
		
		try{
            File toRead=new File(file2);
            FileInputStream fis=new FileInputStream(toRead);
    
            Scanner sc=new Scanner(fis);
    
            String placeholder = "";
            while(sc.hasNextLine()){
                placeholder=sc.nextLine();
                StringTokenizer st = new StringTokenizer(placeholder,",",false);
                populateWithKorgools(st.nextToken(), Integer.valueOf(st.nextToken()));
            }
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
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
        // TODO: would it be faster to only create the new once/delete the excess???
        hole.emptyHole();
        hole.createAndAdd(numOfKorgools);
    }

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

    public void setTuz(String holeId) {
        String name;
        if (holeId.startsWith("W")) { //white player claimed tuz
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

    public void displayMessage(String message) {
        infoLabel.setText(message);
    }
	
	public Hole getKazanLeft() {
        return kazanLeft;
    }
	
	public Hole getKazanRight() {
        return kazanRight;
    }

    public Hole getLeftTuz() {
        return leftTuz;
    }

    public Hole getRightTuz() {
        return rightTuz;
    }

}
