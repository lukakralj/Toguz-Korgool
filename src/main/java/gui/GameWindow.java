package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
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
    private JPanel root;
    private JLayeredPane layeredPane;
    private HashMap<String, Hole> kazans;
    private GameManager manager;

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
        setUpBottomBar();
        setUpLowerPanel();

        layeredPane = new JLayeredPane();
        root.setSize(new Dimension(1280, 720));
        root.setLocation(0, 0);
        layeredPane.add(root, new Integer(0));

        getContentPane().add(layeredPane);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                root.setSize(getContentPane().getSize());
            }
        });

        addWindowStateListener(e -> {
            revalidate();
            root.setSize(getContentPane().getSize());
            repaint();
        });
        pack();
        setVisible(true);
    }

	public GameWindow() {
        this(null);
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
        setPreferredSize(new Dimension(1280, 720));
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
        String[] FileMenuItems = {"CustomInput", "Save", "Load", "Quit"};
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
        topPanel.setBorder(new EmptyBorder(10, 10, 20, 10));//Set Padding around the Top Panel
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
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));//Set Padding around the Bottom Panel
        lowerPanel.setBackground(BACKGROUND_COLOR);
        root.add(lowerPanel, BorderLayout.CENTER);
        JPanel lowerButtonPanel = new JPanel();
        GridLayout botButtons = new GridLayout(0, 9, 10, 10);//Set padding around individual buttons
        lowerButtonPanel.setLayout(botButtons);
        lowerButtonPanel.setBackground(BACKGROUND_COLOR);
        lowerPanel.add(lowerButtonPanel, BorderLayout.SOUTH);
        lowerPanel.add(setUpKazans(), BorderLayout.CENTER);
        fillPanelWithButtons(lowerButtonPanel, "W");
    }

    /**
     * Fills a given JPanel with ImageIcons that act as buttons.
     *
     * @param panel The Panel where the buttons are to be added
     * @param color A single digit string to define the color of the added image
     */
    private void fillPanelWithButtons(JPanel panel, String color) {
        for (int i = 1; i < 10; ++i) {
            Hole button = new Hole();
            button.setName(color + i);
            buttonMap.put(button.getName(), button);
            button.setPreferredSize(new Dimension(30, 160));
            button.addActionListener(e -> holeOnClickAction(button.getName()));
            panel.add(button);
        }
    }

    /**
     * Unsets all the tuzes to being normal buttons by changing its color
     *
     */
    public void unsetTuzes() {
        for (Hole hole : buttonMap.values()) {
            hole.setTuz(false);
        }
    }

    /**
     * Function to construct the bottom bar of the GUI.
     */
    private void setUpBottomBar() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        root.add(bottomPanel, BorderLayout.SOUTH);
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
        kazanPanel.setBackground(BACKGROUND_COLOR);
        kazanPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        kazanRight = new Hole();
        kazanRight.setColorHighlighted(kazanRight.getColorNormal()); // TODO: this is only a temporary "fix"
        kazanRight.setColorBorderNormal(new Color(160,82,45));
		kazanRight.setName("rightKazan");
        kazanRight.setPreferredSize(new Dimension(620, kazanPanel.getHeight() - 10));
        kazanLeft = new Hole();
        kazanLeft.setColorHighlighted(kazanLeft.getColorNormal()); // TODO: this is only a temporary "fix"
        kazanLeft.setColorBorderNormal(new Color(160,82,45));
		kazanLeft.setName("leftKazan");
        kazanLeft.setPreferredSize(new Dimension(620, kazanPanel.getHeight() - 10));
        kazans.put(kazanRight.getName(), kazanRight);
        kazans.put(kazanLeft.getName(), kazanLeft);
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
        // TODO: used for debugging buttons ids
        buttonMap.forEach((k, v) -> {
            if (k.startsWith("B")) {
                System.out.print(k + " = " + v.getLocationOnScreen().x + " ");
            }
        });
        System.out.println();
        buttonMap.forEach((k, v) -> {
            if (k.startsWith("W")) {
                System.out.print(k + " = " + v.getLocationOnScreen().x + " ");
            }
        });
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
					new CustomInputWindow(BACKGROUND_COLOR, manager);
				}
                break;
            case "Save":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to save the game?");
                saveGame();
                break;
            case "Load":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to load the latest save state?");
                loadGame();
                break;
            case "Quit":
                JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?");
                dispose();
        }
    }

    private void saveGame(){
        try{
            File saveFile=new File("src\\main\\java\\saveFile.csv");
            FileOutputStream fos=new FileOutputStream(saveFile);
            PrintWriter pw=new PrintWriter(fos);
        
            for(Map.Entry<String,Hole> entries :buttonMap.entrySet()){
                pw.println(entries.getKey()+","+entries.getValue().getNumberOfKorgools()+","+entries.getValue().isTuz());
            }
        
            pw.flush();
            pw.close();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
		
		try{
            File saveFile=new File("src\\main\\java\\saveFile2.csv");
            FileOutputStream fos=new FileOutputStream(saveFile);
            PrintWriter pw=new PrintWriter(fos);
        
            for(Map.Entry<String,Hole> entries :kazans.entrySet()){
                pw.println(entries.getKey()+","+entries.getValue().getNumberOfKorgools());
            }
        
            pw.flush();
            pw.close();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadGame(){
        try{
            File toRead=new File("src\\main\\java\\saveFile.csv");
            FileInputStream fis=new FileInputStream(toRead);
    
            Scanner sc=new Scanner(fis);
    
            String placeholder = "";
            while(sc.hasNextLine()){
                placeholder=sc.nextLine();
                StringTokenizer st = new StringTokenizer(placeholder,",",false);
                Hole button = buttonMap.get(st.nextToken());
				button.emptyHole();
				button.createAndAdd(Integer.valueOf(st.nextToken()));
				if(st.nextToken().equals(true)){
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
            File toRead=new File("src\\main\\java\\saveFile2.csv");
            FileInputStream fis=new FileInputStream(toRead);
    
            Scanner sc=new Scanner(fis);
    
            String placeholder = "";
            while(sc.hasNextLine()){
                placeholder=sc.nextLine();
                StringTokenizer st = new StringTokenizer(placeholder,",",false);
                Hole button = kazans.get(st.nextToken());
				button.emptyHole();
				button.createAndAdd(Integer.valueOf(st.nextToken()));
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
        if (holeId.equals("left")) {
            hole = kazanLeft;
        }
        else if (holeId.equals("right")) {
            hole = kazanRight;
        }
        else {
            hole = buttonMap.get(holeId);
        }
        // TODO: would it be faster to only create the new once/delete the excess???
        hole.emptyHole();
        hole.createAndAdd(numOfKorgools);
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
	
	public Hole getKazanLeft() {
        return kazanLeft;
    }
	
	public Hole getKazanRight() {
        return kazanRight;
    }

    public void makeTuz(String buttonId) {
        buttonMap.get(buttonId).setTuz(true);
    }

}
