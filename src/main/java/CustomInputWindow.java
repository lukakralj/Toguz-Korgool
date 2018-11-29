import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

/*
 * A Modal JDialog for the custom input window
 *
 */
class CustomInputWindow extends JDialog {

    private Color bgColour;
    private ButtonGroup radioOptionsBlack = new ButtonGroup(), radioOptionsWhite = new ButtonGroup();
    private HashMap<String, JSpinner> spinnerMap = new HashMap<>();
    private HashMap<String, JRadioButton> radioButtonMap = new HashMap<>();
    private int selectedTuzWhite, selectedTuzBlack, numberOfKorgools, blackKazan, whiteKazan;
    private int blackHoles[] = new int[9];
    private int whiteHoles[] = new int[9];
    private JTextArea outputLog;
    private JLabel infoLabel;
    private GameManager manager;

    /**
     * Construct an object of type CustomInputWindow
     *
     * @param bgColour the background colour.
     */
    CustomInputWindow(Color bgColour, GameManager managerIn) {
        manager = managerIn;
        this.bgColour = bgColour;
        initialSetUp();
        setModal(true);
    }

    CustomInputWindow() {
        this(Color.lightGray, null);
    }

    private void initialSetUp() {
        selectedTuzBlack = selectedTuzWhite = -1;
        numberOfKorgools = 0;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("Apply Custom Input");
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
        inputArea.setBackground(bgColour);
        inputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int i = 9; i > 0; --i) inputArea.add(inputCell("B" + i));
        inputArea.add(Box.createHorizontalGlue());
        inputArea.add(inputCell("BlackKazan"));
        inputArea.add(new JLabel("<html>No white<br>Tuz</html>")); //TODO: Work on these labels, they dont look so great
        for (int i = 0; i < 4; ++i) inputArea.add(Box.createHorizontalGlue());
        inputArea.add(inputCell("WhiteKazan"));
        inputArea.add(new JLabel("<html>No black<br>Tuz</html>"));
        for (int i = 1; i < 10; ++i) inputArea.add(inputCell("W" + i));
        getContentPane().add(inputArea, BorderLayout.CENTER);
        infoLabel = new JLabel("<html>Please enter in the number of Korgools per hole, and use the radio buttons to indicate which holes are Tuz.<br> Current number of Korgools: " + numberOfKorgools + "</html>");
        infoLabel.setName("infoLabel");
        infoLabel.setBackground(bgColour);
        getContentPane().add(infoLabel, BorderLayout.NORTH);
        radioButtonMap.get("R_BlackKazan").setSelected(true);
        radioButtonMap.get("R_WhiteKazan").setSelected(true);
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
        cell.setBackground(bgColour);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 162, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setName(componentId);
        spinner.addChangeListener(e -> updateSpinners(spinner.getName()));
        spinner.setFont(spinner.getFont().deriveFont(20L));
        spinnerMap.put(componentId, spinner);
        JRadioButton radio = new JRadioButton();
        radio.setName("R_" + componentId);
        radio.setBackground(bgColour);
        if (componentId.startsWith("B")) radioOptionsBlack.add(radio);
        else if (componentId.startsWith("W")) radioOptionsWhite.add(radio);
        else if (componentId.equals("BlackKazan")) radioOptionsBlack.add(radio);
        else if (componentId.equals("WhiteKazan")) radioOptionsWhite.add(radio);
        radio.setActionCommand(componentId);
        radio.addActionListener(e -> updateSelectedTuz(e.getActionCommand()));
        radioButtonMap.put(radio.getName(), radio);
        JLabel titleLabel = new JLabel(componentId, SwingConstants.CENTER);
        titleLabel.setBackground(bgColour);
        if (componentId.startsWith("W")) {
            cell.setBackground(Color.WHITE);
            radio.setBackground(Color.WHITE);
            titleLabel.setBackground(Color.WHITE);
        } else if (componentId.startsWith("B")) {
            cell.setBackground(Color.BLACK);
            radio.setBackground(Color.BLACK);
            titleLabel.setBackground(Color.BLACK);
            titleLabel.setForeground(Color.WHITE);
        }
        cell.add(radio, BorderLayout.EAST);
        cell.add(spinner, BorderLayout.CENTER);
        cell.add(titleLabel, BorderLayout.NORTH);
        cell.add(Box.createHorizontalStrut(20), BorderLayout.WEST);
        cell.add(new JLabel("<html></html>"), BorderLayout.SOUTH);
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

    //TODO: javadocs , error messages?
    private void updateSpinners(String componentId) {
        numberOfKorgools = 0;
        for (JSpinner currentSpinner : spinnerMap.values()) numberOfKorgools += (int) currentSpinner.getValue();
        infoLabel.setText("<html>Please enter in the number of Korgools per hole, and use the radio buttons to indicate which holes are Tuz.<br> Current number of Korgools: " + numberOfKorgools + "</html>");
        if (componentId.equals("BlackKazan")) blackKazan = (int) spinnerMap.get(componentId).getValue();
        else if (componentId.equals("WhiteKazan")) whiteKazan = (int) spinnerMap.get(componentId).getValue();
        else if (componentId.startsWith("W"))
            whiteHoles[Integer.parseInt(componentId.substring(1)) - 1] = (int) spinnerMap.get(componentId).getValue();
        else if (componentId.startsWith("B"))
            blackHoles[Integer.parseInt(componentId.substring(1)) - 1] = (int) spinnerMap.get(componentId).getValue();
    }

}

