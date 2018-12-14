package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * This class represents korgools in the game.
 *
 * @author Luka Kralj
 * @version 11 December 2018
 */
public class Korgool extends OvalButton {

    private Hole parentHole;
    private boolean isInDrag;
    private Point startDrag;
    private BufferedImage korgoolImage;

    /**
     * Construct a korgool.
     *
     * @param parentHole The hole this korgool belongs too.
     */
    public Korgool(Hole parentHole, BufferedImage img) {
        this(parentHole, Color.BLUE, 2, img);
    }

    /**
     * Construct a korgool.
     *
     * @param parentHole The hole this korgool belongs too.
     * @param color Color of the korgool.
     */
    public Korgool(Hole parentHole, Color color) {
        this(parentHole, color, 2, null);
    }


    /**
     * Construct a korgool.
     *
     * @param parentHole The hole this korgool belongs too.
     * @param color Color of the korgool.
     * @param borderThickness How thick do we want the korgool border to be.
     */
    public Korgool(Hole parentHole, Color color, int borderThickness, BufferedImage img) {
        super(SHAPE_OVAL, VERTICAL, color, Color.yellow, Color.BLACK, Color.yellow); // if we see yellow color something went wrong
        this.parentHole = parentHole;
        korgoolImage = img;
        setBorderThickness(borderThickness);
    }

    @Override
    public BufferedImage getBackgroundImage() {
        return korgoolImage;
    }

    /**
     * Change the hole that this korgool belongs to.
     *
     * @param newParent New hole for this korgool.
     */
    public void setParentHole(Hole newParent) {
        parentHole = newParent;
    }

    //==============================================
    // Needed for highlighting the hole correctly.
    //==============================================

    @Override
    public void mousePressed(MouseEvent e) {
        parentHole.mousePressed(e);
        isInDrag = true;
        startDrag = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        parentHole.mouseReleased(e);
        if (isInDrag) {
            Point endPoint = e.getPoint();
            isInDrag = false;
            if (Math.abs(endPoint.x - startDrag.x) < 10 && Math.abs(endPoint.y - startDrag.y) < 10) {
                parentHole.holeClicked(new ActionEvent(e.getSource(), e.getID(), e.paramString()));
            }
            else {
                System.out.println("===== NO ACTION: korgool dragged too far (in release).");;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        parentHole.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        parentHole.mouseMoved(e);
        isInDrag = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        parentHole.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isInDrag) {
            parentHole.holeClicked(new ActionEvent(e.getSource(), e.getID(), e.paramString()));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isValidClickPosition(e.getLocationOnScreen())) {
            // mouse exited
            isInDrag = false;
            System.out.println("===== NO ACTION: korgool dragged too far.");
        }
    }
}
