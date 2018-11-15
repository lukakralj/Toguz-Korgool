/*
 * A Modal JDialog for the custom input window
 *
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;

class CustomInputWindow extends JDialog {

    private Color bgColour;
    private ButtonGroup radioOptionsTop, radioOptionsBottom;
    private HashMap<String, JSpinner> spinnerMap = new HashMap<>();
    private int selectedTuzWhite, selectedTuzBlack, numberOfKorgools;
    private JTextArea outputLog;
    private JLabel infoLabel;
    //private GameManager manager; TODO: ADD MANAGER AS PARAMETER

    /**
     * Construct an object of type CustomInputWindow
     *
     * @param bgColour the background colour.
     */
    CustomInputWindow(Color bgColour) {
        selectedTuzBlack = selectedTuzWhite = -1;
        numberOfKorgools = 0;
        this.bgColour = bgColour;
        setModal(true);
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
            inputArea.add(inputCell("B" + i, true));
        }
        inputArea.add(new JLabel());
        inputArea.add(inputCell("BlackKazan", false));
        for (int i = 0; i < 5; ++i) inputArea.add(new JLabel());
        inputArea.add(inputCell("WhiteKazan", false));
        inputArea.add(new JLabel());
        for (int i = 1; i < 10; ++i) {
            inputArea.add(inputCell("W" + i, true));
        }
        getContentPane().add(inputArea, BorderLayout.CENTER);
        infoLabel = new JLabel("Please enter in the number of Korgools per hole, and use the radio buttons to indicate which holes are Tuz. Current number of Korgools: " + numberOfKorgools);
        getContentPane().add(infoLabel, BorderLayout.NORTH);
    }

    /**
     * Function to set up the bottom bar of the dialog box
     */
    private void setUpBottomBar() {
        JPanel bottomBar = new JPanel(new BorderLayout());
        JButton confirmButton = new JButton("Confirm input");
        confirmButton.addActionListener(e -> {
            confirmAction();
        });
        outputLog = new JTextArea();
        bottomBar.add(outputLog, BorderLayout.CENTER);
        bottomBar.add(confirmButton, BorderLayout.EAST);
        getContentPane().add(bottomBar, BorderLayout.SOUTH);
    }

    /**
     * Function to validate user input and apply changes if input is valid
     */
    private void confirmAction() {
        if (selectedTuzWhite == -1 || selectedTuzBlack == -1) {
            outputLog.setText("Please select a Tuz for both sides");
        } else if (selectedTuzWhite == 9 || selectedTuzBlack == 9) {
            outputLog.setText("Hole 9 cannot be a Tuz");
        } else if (numberOfKorgools != 162) {
            outputLog.setText("Please ensure the number of Korgools adds to 162");
        } else {
            // TODO: run necessary game manager code!
            dispose();
        }
    }

    /**
     * Helper function to construct a cell containing a spinner and radio button
     *
     * @param componentId the Id of the hole/kazan this cell represents
     * @param menuItem    boolean to determine whether a radio button is required
     * @return JPanel containing the input cell
     */
    private JPanel inputCell(String componentId, boolean menuItem) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setMaximumSize(new Dimension(getWidth() / 10, getHeight() / 4));
        cell.setBackground(bgColour);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 162, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(e -> {
            numberOfKorgools = 0;
            for (JSpinner currentSpinner : spinnerMap.values()) numberOfKorgools += (int) currentSpinner.getValue();
            infoLabel.setText("Please enter in the number of Korgools per hole, and use the radio buttons to indicate which holes are Tuz. Current number of Korgools: " + numberOfKorgools);
            System.out.println(spinner.getValue());
        });
        spinner.setFont(spinner.getFont().deriveFont(20L));
        spinner.setName(componentId);
        spinnerMap.put(componentId, spinner);
        if (menuItem) {
            JRadioButton radio = new JRadioButton();
            if (componentId.startsWith("B")) radioOptionsTop.add(radio);
            else radioOptionsBottom.add(radio);
            radio.setActionCommand(componentId);
            radio.addActionListener(e -> {
                updateSelectedTuz(e.getActionCommand());
            });
            cell.add(radio, BorderLayout.EAST);
        }
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
        if (componentId.startsWith("B")) selectedTuzBlack = Integer.parseInt(componentId.substring(1));
        else selectedTuzWhite = Integer.parseInt(componentId.substring(1));
    }
}
