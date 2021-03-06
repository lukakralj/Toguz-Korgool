package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashMap;
import logic.AnimationController;
import logic.GameManager;

/*
 * A Modal JDialog for the custom input window.
 *
 * @author Yasir Soleja
 * @version 01/12/2018
 */
public class CustomInputWindow extends JDialog {

    private static final Color BG_COLOUR = Color.LIGHT_GRAY;
    private ButtonGroup radioOptionsBlackSide = new ButtonGroup(), radioOptionsWhiteSide = new ButtonGroup();
    private HashMap<String, JSpinner> spinnerMap = new HashMap<>();
    private HashMap<String, JRadioButton> radioButtonMap = new HashMap<>();
    private int selectedTuzWhite, selectedTuzBlack, numberOfKorgools, blackKazanCount, whiteKazanCount;
    private int blackHoleContents[] = new int[9];
    private int whiteHoleContents[] = new int[9];
    private JTextPane outputLog;
    private JLabel informationLabel;
    private GameManager manager;

    /**
     * Constructs an object of type CustomInputWindow.
     *
     * @param managerIn          the game manager associated with the parent window.
     */
    public CustomInputWindow(GameManager managerIn) {
        selectedTuzBlack = selectedTuzWhite = -1;
        numberOfKorgools = 0;
        for (int i = 0; i < 9; ++i) blackHoleContents[i] = 9;
        for (int i = 0; i < 9; ++i) whiteHoleContents[i] = 9;
        manager = managerIn;
        if (manager == null) setModal(false);
        else setModal(true);
        initialSetUp();
    }

    /**
     * Constructs an object of type CustomInputWindow.
     * Default constructor (No game manager, default colour.)
     */
    public CustomInputWindow() {
        this(null);
    }

    /**
     * Performs initial set up of the frame content pane.
     */
    private void initialSetUp() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);
        setTitle("Apply Custom Input");
        getContentPane().setBackground(BG_COLOUR);
        setPreferredSize(new Dimension(1024, 576));
        setUpTopBar();
        setUpInputArea();
        setUpBottomBar();
        pack();
        setVisible(true);
    }

    /**
     * Sets up the top bar of the dialog box.
     */
    private void setUpTopBar() {
        informationLabel = new JLabel("", SwingConstants.CENTER);
        informationLabel.setName("infoLabel");
        updateInformationLabel();
        getContentPane().add(informationLabel, BorderLayout.NORTH);
    }

    /**
     * Sets up the main input area of the dialog box.
     */
    private void setUpInputArea() {
        JPanel inputArea = new JPanel(new GridLayout(0, 9));
        inputArea.setBackground(BG_COLOUR);
        inputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int i = 9; i > 0; --i) inputArea.add(inputCell("B" + i));
        inputArea.add(Box.createHorizontalGlue());
        inputArea.add(inputCell("BlackKazan"));
        inputArea.add(new JLabel("<html><p style=\"text-align:center\">No Black<br>Tuz Selection</p></html>"));
        for (int i = 0; i < 4; ++i) inputArea.add(Box.createHorizontalGlue());
        inputArea.add(inputCell("WhiteKazan"));
        inputArea.add(new JLabel("<html><p style=\"text-align:center\">No White<br>Tuz Selection</p></html>"));
        for (int i = 1; i < 10; ++i) inputArea.add(inputCell("W" + i));
        getContentPane().add(inputArea, BorderLayout.CENTER);
        radioButtonMap.get("R_BlackKazan").setSelected(true);
        radioButtonMap.get("R_WhiteKazan").setSelected(true);
        updateHoleValues("all");
    }

    /**
     * Sets up the bottom bar of the dialog box.
     */
    private void setUpBottomBar() {
        JPanel bottomBar = new JPanel(new BorderLayout());
        outputLog = createOutputLog();
        bottomBar.add(outputLog, BorderLayout.CENTER);
        bottomBar.add(createConfirmButton(), BorderLayout.EAST);
        getContentPane().add(bottomBar, BorderLayout.SOUTH);
    }

    /**
     * Sets up a confirm button to confirm submission of input.
     *
     * @return JButton for the confirm button.
     */
    private JButton createConfirmButton() {
        JButton confirmButton = new JButton("Confirm input");
        confirmButton.setName("confirm");
        confirmButton.addActionListener(e -> confirmAction());
        confirmButton.setBackground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        return confirmButton;
    }

    /**
     * Sets up an output log to display error text.
     *
     * @return JTextPane for the output log.
     */
    private JTextPane createOutputLog() {
        JTextPane outputLog = new JTextPane();
        outputLog.setBackground(BG_COLOUR);
        outputLog.setEditable(false);
        outputLog.setName("outputLog");
        StyledDocument doc = outputLog.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        return outputLog;
    }

    /**
     * Validates user input, applies changes and disposes window if input is valid.
     */
    private void confirmAction() {
        if (selectedTuzBlack == selectedTuzWhite && selectedTuzWhite != -1)
            outputLog.setText("Tuz may not exist as the same hole on opposite sides");
        else if (selectedTuzWhite == 9 || selectedTuzBlack == 9)
            outputLog.setText("Hole 9 may not become a Tuz");
        else if (numberOfKorgools != 162)
            outputLog.setText("Please ensure the total number of Korgools is 162");
        else {
            if (AnimationController.instance().isRunning()) {
                JOptionPane.showMessageDialog(this, "Animations are currently running. Please wait for them to finish.", "", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            if (manager != null) {
                manager.populateInitialBoard(whiteHoleContents, blackHoleContents, selectedTuzWhite, selectedTuzBlack, whiteKazanCount, blackKazanCount);
            }
            dispose();
        }
    }

    /**
     * Constructs a cell containing a title, spinner, and radio button.
     *
     * @param componentId the ID of the hole/kazan this cell represents.
     * @return JPanel for the input cell
     */
    private JPanel inputCell(String componentId) {
        JPanel inputCell = new JPanel(new BorderLayout());
        inputCell.setName("C_" + componentId);
        inputCell.setBackground(BG_COLOUR);
        inputCell.add(createRadioButton(componentId), BorderLayout.EAST);
        inputCell.add(createSpinner(componentId), BorderLayout.CENTER);
        inputCell.add(new JLabel(generateCellTitle(componentId), SwingConstants.CENTER), BorderLayout.NORTH);
        inputCell.add(Box.createHorizontalStrut(20), BorderLayout.WEST);
        inputCell.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
        return inputCell;
    }

    /**
     * Generates a title for the input cell
     *
     * @param componentId the component ID the cell represents
     * @return a proper title for this cell
     */
    private String generateCellTitle(String componentId) {
        if (componentId.equals("BlackKazan"))
            return "Black Kazan";
        else if (componentId.equals("WhiteKazan"))
            return "White Kazan";
        else if (componentId.startsWith("W"))
            return "White Hole " + componentId.substring(1);
        else if (componentId.startsWith("B"))
            return "Black Hole " + componentId.substring(1);
        return "";
    }

    /**
     * Constructs a spinner for an input cell.
     *
     * @param componentId the ID of the hole/kazan this spinner represents.
     * @return JSpinner for the input cell.
     */
    private JSpinner createSpinner(String componentId) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 162, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setName(componentId);
        if (componentId.length() == 2) {
            spinner.setValue(9);
        }
        spinner.addChangeListener(e -> updateHoleValues(spinner.getName()));
        spinner.setFont(spinner.getFont().deriveFont(20L));
        spinnerMap.put(componentId, spinner);
        return spinner;
    }

    /**
     * Constructs a radio button for an input cell.
     *
     * @param componentId the ID of the hole/kazan this radio button represents.
     * @return JRadioButton for the input cell.
     */
    private JRadioButton createRadioButton(String componentId) {
        JRadioButton radio = new JRadioButton();
        radio.setName("R_" + componentId);
        radio.setBackground(BG_COLOUR);
        if (componentId.equals("BlackKazan"))
            radioOptionsWhiteSide.add(radio);
        else if (componentId.equals("WhiteKazan"))
            radioOptionsBlackSide.add(radio);
        else if (componentId.startsWith("B"))
            radioOptionsBlackSide.add(radio);
        else if (componentId.startsWith("W"))
            radioOptionsWhiteSide.add(radio);
        radio.setActionCommand(componentId);
        radio.addActionListener(e -> updateSelectedTuz(e.getActionCommand()));
        radioButtonMap.put(radio.getName(), radio);
        return radio;
    }

    /**
     * Action listener for the radio buttons.
     * Updates the selected tuz values based on the button ID.
     * NOTE: selecting a 'WHITE' radio button updates the 'BLACK' tuz selection (and vice versa)
     *
     * @param radioButtonId the ID of the selected radio button.
     */
    private void updateSelectedTuz(String radioButtonId) {
        if (radioButtonId.equals("BlackKazan"))
            selectedTuzBlack = -1;
        else if (radioButtonId.equals("WhiteKazan"))
            selectedTuzWhite = -1;
        else if (radioButtonId.startsWith("W"))
            selectedTuzBlack = Integer.parseInt(radioButtonId.substring(1));
        else if (radioButtonId.startsWith("B"))
            selectedTuzWhite = Integer.parseInt(radioButtonId.substring(1));
        updateInformationLabel();
    }

    /**
     * Action listener for the spinners.
     * Updates the hole value arrays / kazan values based on the spinner content.
     *
     * @param componentId the component ID of the spinner
     */
    private void updateHoleValues(String componentId) {
        numberOfKorgools = 0;
        for (JSpinner currentSpinner : spinnerMap.values()) {
            numberOfKorgools += (int) currentSpinner.getValue();
        }
        updateInformationLabel();
        if (componentId.equals("BlackKazan"))
            blackKazanCount = (int) spinnerMap.get(componentId).getValue();
        else if (componentId.equals("WhiteKazan"))
            whiteKazanCount = (int) spinnerMap.get(componentId).getValue();
        else if (componentId.startsWith("W"))
            whiteHoleContents[Integer.parseInt(componentId.substring(1)) - 1] = (int) spinnerMap.get(componentId).getValue();
        else if (componentId.startsWith("B"))
            blackHoleContents[Integer.parseInt(componentId.substring(1)) - 1] = (int) spinnerMap.get(componentId).getValue();
    }

    /**
     * Updates the content of the information label.
     */
    private void updateInformationLabel() {
        informationLabel.setText("<html><p style=\"text-align:center\">Please enter in the number of Korgools per hole, and use the radio buttons to indicate which holes are Tuz." +
                "<br> White Player Tuz Selection: " + generateTuzLabel(selectedTuzWhite) + "<br>Current number of Korgools: " + numberOfKorgools + "<br>" +
                "Black Player Tuz Selection: " + generateTuzLabel(selectedTuzBlack) + "</p></html>");
    }

    /**
     * Generates an appropriate label for each selected Tuz
     *
     * @param selectedTuz the hole number of the selected Tuz
     * @return an appropriate label for the Tuz.
     */
    private String generateTuzLabel(int selectedTuz) {
        if (selectedTuz > 0) return "Hole " + selectedTuz;
        else return "None";
    }


}

