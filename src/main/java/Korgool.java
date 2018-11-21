import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Korgool extends OvalButton implements MouseListener, MouseMotionListener {

    private Hole parent;


    public Korgool(Hole parent) {
        super("", Color.BLUE, Color.yellow, Color.yellow, Color.yellow); // TODO: if yellow is shown something went wrong

        this.parent = parent;
        setBorderThickness(0);
        addActionListener(e -> {
            if (parent.getActionListeners().length > 0) {
                parent.getActionListeners()[0].actionPerformed(e);
            }
        });

        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }

    public void setParent(Hole parent) {
        this.parent = parent;
        for (ActionListener l : getActionListeners()) {
            removeActionListener(l);
        }
        addActionListener(e -> {
            if (parent.getActionListeners().length > 0) {
                parent.getActionListeners()[0].actionPerformed(e);
            }
        });
    }








    //==============================================
    // Needed for highlighting the button correctly.
    //==============================================

    @Override
    public void mousePressed(MouseEvent e) {
        parent.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        parent.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        parent.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        parent.mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        parent.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
