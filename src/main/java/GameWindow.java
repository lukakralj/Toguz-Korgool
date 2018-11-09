/*
* A Swing graphical user interface for the Team Platypus Agile Project
*
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;


public class GameWindow extends JFrame {

    private static final Color bgColor = Color.LIGHT_GRAY;

    public GameWindow(){
        setTitle("Toguz Korgol");
        setResizable(false);
        setPreferredSize(new Dimension(1280,720));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);
        BorderLayout bl = new BorderLayout();
        getContentPane().setLayout(bl);
        menuSetup();
        setUpTopButtons();
        setupBottomBar();
        setUpBotButtons();
        pack();
        setVisible(true);
    }

    private void setUpTopButtons(){
        JPanel topButtonPanel = new JPanel();
        topButtonPanel.setBorder(new EmptyBorder(20,20,20,20));
        topButtonPanel.setBackground(bgColor);
        getContentPane().add(topButtonPanel,BorderLayout.NORTH);
        GridLayout topButtons = new GridLayout(0,9,10,10);
        topButtonPanel.setLayout(topButtons);
        for(int i=9;i>0;--i){
            JButton button = new JButton(Integer.toString(i));
            button.setPreferredSize(new Dimension(40,100));
            topButtonPanel.add(button);
        }
    }

    private void setUpBotButtons(){
        JPanel botButtonPanel = new JPanel();
        botButtonPanel.setBorder(new EmptyBorder(10,10,10,10));
        botButtonPanel.setBackground(bgColor);
        getContentPane().add(botButtonPanel,BorderLayout.CENTER);
        BorderLayout border = new BorderLayout();
        botButtonPanel.setLayout(border);
        JPanel lowerPane = new JPanel();
        GridLayout botButtons = new GridLayout(0,9,10,10);
        lowerPane.setLayout(botButtons);
        lowerPane.setBackground(bgColor);
        botButtonPanel.add(lowerPane,BorderLayout.SOUTH);
        for(int i=1;i<10;++i){
            JButton button = new JButton(Integer.toString(i));
            button.setPreferredSize(new Dimension(40,100));
            lowerPane.add(button);
        }
    }

    private void setupBottomBar(){
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10,10,10,10));
        bottomPanel.setBackground(bgColor);
        getContentPane().add(bottomPanel,BorderLayout.SOUTH);
        BorderLayout bl = new BorderLayout();
        bottomPanel.setLayout(bl);
        JButton makeMove = new JButton("Make this move");
        bottomPanel.add(makeMove,BorderLayout.EAST);
    }

    private void menuSetup(){
        ArrayList<JMenuItem> menuItems = new ArrayList<>();
        JMenu menu = new JMenu("File");
        JMenuItem newGame = new JMenuItem("New Game");
        menu.add(newGame);
        menuItems.add(newGame);
        JMenuItem loadGame = new JMenuItem("Load Game");
        menu.add(loadGame);
        menuItems.add(loadGame);
        JMenuItem saveGame = new JMenuItem("Save Game");
        menu.add(saveGame);
        menuItems.add(saveGame);
        JMenuItem customInput = new JMenuItem("Apply Custom Scenario");
        menu.add(customInput);
        menuItems.add(customInput);
        customInput.addActionListener(e-> { new CustomInputWindow(bgColor);}); //TODO: Prompt the user whether they want to save their game before making changes.
        JMenuItem preferences = new JMenuItem("Preferences");
        menu.add(preferences);
        menuItems.add(preferences);
        JMenuItem quit = new JMenuItem("Quit");
        menu.add(quit);
        menuItems.add(quit);
        quit.addActionListener( e-> {dispose();}); // TODO: Implement a 'save before quit' functionality.
        JMenuBar menuBar = new JMenuBar();
        for(JMenuItem mi: menuItems) mi.setFont(menu.getFont().deriveFont(16F));
        menu.setFont(menu.getFont().deriveFont(16F));
        menuBar.add(menu);
        menuBar.setBackground(bgColor);
        setJMenuBar(menuBar);
    }


}