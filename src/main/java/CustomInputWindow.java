/*
* A Modal JDialog for the custom input window
*
 */

import javax.swing.*;
import java.awt.*;

public class CustomInputWindow extends JDialog {

    private Color bgColour;

    public CustomInputWindow(Color bgColour){
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("Apply Custom Scenario");
        getContentPane().setBackground(bgColour);
        setPreferredSize(new Dimension(854,480));
        pack();
        setVisible(true);
    }

}
