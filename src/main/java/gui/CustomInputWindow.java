package gui;
/*
 * A Modal JDialog for the custom input window
 *
 */


import logic.GameManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

public class CustomInputWindow extends JDialog {

    private Color bgColour;
    private ButtonGroup radioOptionsTop, radioOptionsBottom;
    private HashMap<String, JSpinner> spinnerMap = new HashMap<>();
    private int selectedTuzWhite, selectedTuzBlack, numberOfKorgools;
    private JTextArea outputLog;
    private JLabel infoLabel;
    private GameManager manager;

    /**
     * Construct an object of type gui.CustomInputWindow
     *
     * @param bgColour the background colour.
     */
    public CustomInputWindow(Color bgColour, GameManager managerIn) {
        manager = managerIn;
        this.bgColour = bgColour;
        initialSetUp();
        setModal(true);
    }

    public CustomInputWindow() {
        manager = null;
        this.bgColour = Color.GRAY;
        initialSetUp();
        setModal(true);
    }

    public void initialSetUp() {
        selectedTuzBlack = selectedTuzWhite = -1;
        numberOfKorgools = 0;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setTitle("CustomInput");
        radioOptionsTop = new ButtonGroup();
        radioOptionsBottom = new ButtonGroup();
        getContentPane().setBackground(bgColour);
        setPreferredSize(new Dimension(854, 480));
        setUpBottomBar();
        setUpInputArea();
        pack();
        setVisible(true);
    }

    /**
     * Function to set up the input area of the dialog box
     */
    private void setUpInputArea() {
        JPanel inputArea = new JPanel(new GridLayout(0, 9));
        inputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int i = 9; i > 0; --i) {
            inputArea.add(inputCell("B" + i));
        }
        inputArea.add(new JLabel());
        inputArea.add(inputCell("BlackKazan"));
        inputArea.add(new JLabel("<html>No white<br>Tuz</html>"));
        for (int i = 0; i < 4; ++i) inputArea.add(new JLabel());
        inputArea.add(inputCell("WhiteKazan"));
        inputArea.add(new JLabel("<html>No black<br>Tuz</html>"));
        for (int i = 1; i < 10; ++i) {
            inputArea.add(inputCell("W" + i));
        }
        getContentPane().add(inputArea, BorderLayout.CENTER);
        infoLabel = new JLabel("<html>Please enter in the number of Korgools per hole, and use the radio buttons to indicate which holes are Tuz.<br> Current number of Korgools: " + numberOfKorgools + "</html>");
        infoLabel.setName("infoLabel");
        getContentPane().add(infoLabel, BorderLayout.NORTH);
    }

    /**
     * Function to set up the bottom bar of the dialog box
     */
    private void setUpBottomBar() {
        JPanel bottomBar = new JPanel(new BorderLayout());
        JButton confirmButton = new JButton("Confirm input");
        confirmButton.setName("confirm");
        confirmButton.addActionListener(e -> confirmAction());
        outputLog = new JTextArea();
        outputLog.setEditable(false);
        outputLog.setName("outputLog");
        bottomBar.add(outputLog, BorderLayout.CENTER);
        bottomBar.add(confirmButton, BorderLayout.EAST);
        getContentPane().add(bottomBar, BorderLayout.SOUTH);
    }

    /**
     * Function to validate user input and apply changes if input is valid
     */
    private void confirmAction() {
        if (selectedTuzBlack == selectedTuzWhite && selectedTuzWhite != -1) {
            outputLog.setText("The Tuz cannot be the same hole on opposite sides");
        } else if (selectedTuzWhite == 9 || selectedTuzBlack == 9) {
            outputLog.setText("Hole 9 cannot be a Tuz");
        } else if (numberOfKorgools != 162) {
            outputLog.setText("Please ensure the number of Korgools adds to 162");
        } else {
            int blackHoles[] = new int[9];
            int whiteHoles[] = new int[9];
            int blackKazan = (int) spinnerMap.get("BlackKazan").getValue();
            int whiteKazan = (int) spinnerMap.get("WhiteKazan").getValue();
            for (int i = 1; i <= 9; ++i) {
                blackHoles[i - 1] = (int) spinnerMap.get("B" + i).getValue();
                whiteHoles[i - 1] = (int) spinnerMap.get("W" + i).getValue();
            }
            if (manager != null) {
                manager.resetTuzes();
                manager.populateInitialBoard(whiteHoles, blackHoles, selectedTuzWhite, selectedTuzBlack, whiteKazan, blackKazan);
            }
            dispose();
        }
    }

    /**
     * Helper function to construct a cell containing a spinner and radio button
     *
     * @param componentId the Id of the hole/kazan this cell represents
     * @return JPanel containing the input cell
     */
    private JPanel inputCell(String componentId) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setName("C_" + componentId);
        cell.setMaximumSize(new Dimension(getWidth() / 10, getHeight() / 4));
        cell.setBackground(bgColour);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 162, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(e -> {
            numberOfKorgools = 0;
            for (JSpinner currentSpinner : spinnerMap.values()) numberOfKorgools += (int) currentSpinner.getValue();
            infoLabel.setText("<html>Please enter in the number of Korgools per hole, and use the radio buttons to indicate which holes are Tuz.<br> Current number of Korgools: " + numberOfKorgools + "</html>");
        });
        spinner.setFont(spinner.getFont().deriveFont(20L));
        spinner.setName(componentId);
        spinnerMap.put(componentId, spinner);
        JRadioButton radio = new JRadioButton();
        radio.setName("R_" + componentId);
        if (componentId.startsWith("B")) radioOptionsTop.add(radio);
        else if (componentId.startsWith("W")) radioOptionsBottom.add(radio);
        else if (componentId.equals("BlackKazan")) {
            radioOptionsTop.add(radio);
        } else if (componentId.equals("WhiteKazan")) {
            radioOptionsBottom.add(radio);
        }
        radio.setActionCommand(componentId);
        radio.addActionListener(e -> updateSelectedTuz(e.getActionCommand()));
        cell.add(radio, BorderLayout.EAST);

        cell.add(spinner, BorderLayout.CENTER);
        cell.add(new JLabel(componentId), BorderLayout.NORTH);
        return cell;
    }

    /**
     * Action function for the radio buttons
     *
     * @param componentId the ID of the selected radio button
     */
    private void updateSelectedTuz(String componentId) {
        if (componentId.equals("BlackKazan")) selectedTuzWhite = -1;
        else if (componentId.equals("WhiteKazan")) selectedTuzBlack = -1;
        else if (componentId.startsWith("W")) selectedTuzBlack = Integer.parseInt(componentId.substring(1));
        else if (componentId.startsWith("B")) selectedTuzWhite = Integer.parseInt(componentId.substring(1));
    }

}
