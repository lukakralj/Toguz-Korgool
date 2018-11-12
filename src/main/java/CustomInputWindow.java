/*
 * A Modal JDialog for the custom input window
 *
 */

import javax.swing.*;
import java.awt.*;

public class CustomInputWindow extends JDialog {

    private Color bgColour;

    public CustomInputWindow(Color bgColour) { //TODO: complete this window
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("CustomInput");
        getContentPane().setBackground(bgColour);
        setPreferredSize(new Dimension(854, 480));
        pack();
        setVisible(true);
    }
}
