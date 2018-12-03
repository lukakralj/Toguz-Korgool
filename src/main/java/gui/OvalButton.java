package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * This class represents an oval button. This means that the button will only trigger
 * an acton if it is clicked within the oval shape drawn on it.
 *
 * @author Luka Kralj
 * @version 18 November 2018
 */
public class OvalButton extends JButton implements MouseListener, MouseMotionListener {

    private Color colorNormal;
    private Color colorHighlighted;
    private Color colorBorderNormal;
    private Color colorBorderHighlighted;
    private int borderThickness;
    private boolean borderHighlighted;
    private boolean backgroundHighlighted;
    private Color currentBackground;

    /**
     * Construct a default oval button.
     */
    public OvalButton() {
        this("");
    }

    /**
     * Construct an oval button with this text.
     *
     * @param text Text to display.
     */
    public OvalButton(String text) {
        this(text, Color.WHITE, Color.LIGHT_GRAY, Color.BLACK, Color.RED);
    }

    /**
     * Construct an oval button and specify its custom colour.
     *
     * @param text Text to display.
     * @param colorNormal The main color of the button.
     * @param colorHighlighted The color of the button when it is highlighted (hovered over etc.).
     * @param colorBorderNormal The main border color of the button.
     * @param colorBorderHighlighted The border color that will show whenever the button is marked as being
     *                               highlighted (will remain this color even after the mouse exits).
     */
    public OvalButton(String text, Color colorNormal, Color colorHighlighted, Color colorBorderNormal, Color colorBorderHighlighted) {
        super(text);
        this.colorNormal = colorNormal;
        this.currentBackground = colorNormal;
        this.colorHighlighted = colorHighlighted;
        this.colorBorderNormal = colorBorderNormal;
        this.colorBorderHighlighted = colorBorderHighlighted;
        borderThickness = 5;
        borderHighlighted = false;
        backgroundHighlighted = false;
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        // TODO: buttons get highlighted also when the click is outside the oval
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Set the main color of the button.
     *
     * @param colorNormal
     */
    public void setColorNormal(Color colorNormal) {
        this.colorNormal = colorNormal;
    }

    /**
     *
     * @return Main color of this button.
     */
    public Color getColorNormal() {
        return colorNormal;
    }

    /**
     * Set the color of the button when it is highlighted (hovered over etc.).
     *
     * @param colorHighlighted
     */
    public void setColorHighlighted(Color colorHighlighted) {
        this.colorHighlighted = colorHighlighted;
    }

    /**
     * Set the main border color of the button.
     *
     * @param colorBorderNormal
     */
    public void setColorBorderNormal(Color colorBorderNormal) {
        this.colorBorderNormal = colorBorderNormal;
    }

    /**
     * Set the border color that will show whenever the button is marked as being
     * highlighted (will remain this color even after the mouse exits).
     *
     * @param colorBorderHighlighted
     */
    public void setColorBorderHighlighted(Color colorBorderHighlighted) {
        this.colorBorderHighlighted = colorBorderHighlighted;
    }

    /**
     * Set the thickness of the border on the button.
     *
     * @param borderThickness Thickness in pixels.
     */
    public void setBorderThickness(int borderThickness) {
        if (borderThickness < 0) {
            this.borderThickness = 0;
        }
        else {
            this.borderThickness = borderThickness;
        }
    }

    /**
     *
     * @return Thickness of the current border on the oval.
     */
    public int getBorderThickness() {
        return borderThickness;
    }

    /**
     *
     * @param isHighlighted True if you want this button's border to be highlighted, false otherwise.
     */
    public void setHighlightedBorder(boolean isHighlighted) {
        borderHighlighted = isHighlighted;
    }

    /**
     *
     * @param isHighlighted True if you want this button's background to be highlighted, false otherwise.
     */
    public void setHighlightedBackground(boolean isHighlighted) {
        backgroundHighlighted = isHighlighted;
    }

    /**
     * This method is overridden because it adds additional checks before executing the
     * action specified by the user of the button.
     *
     * @param l ActionListener specified by the user.
     */
    @Override
    public void addActionListener(ActionListener l) {
        super.addActionListener(e -> {
            if (isInOval(MouseInfo.getPointerInfo().getLocation())) {
                l.actionPerformed(e);
            }
        });
    }

    /**
     * Check if the specified point is within the oval of the button.
     *
     * @param p Point to check.
     * @return True if the point is within the borders or on the border of the oval, false if it is outside of it.
     */
    private boolean isInOval(Point p) {
        double x = p.x;
        double y = p.y;

        // Calculate centre of the ellipse.
        double s1 = getLocationOnScreen().x + getSize().width / 2;
        double s2 = getLocationOnScreen().y + getSize().height / 2;

        // Calculate semi-major and semi-minor axis
        double a = getSize().width / 2;
        double b = getSize().height / 2;

        // Check if the given point is withing the specified ellipse:
        return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
    }

    /**
     * Takes care of rendering the oval button correctly.
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;
        Dimension d = getSize();

        if (backgroundHighlighted) {
            gr.setColor(colorHighlighted);
        }
        else {
            gr.setColor(currentBackground);
        }

        gr.fillOval(0, 0, d.width, d.height);

        Shape border = createBorder();
        if (borderHighlighted) {
            gr.setColor(colorBorderHighlighted);
        }
        else {
            gr.setColor(colorBorderNormal);
        }
        gr.fill(border);
    }

    /**
     * Creates a border shape (like a ring).
     *
     * @return Border shape.
     */
    private Shape createBorder() {
        double width = getSize().width;
        double height = getSize().height;
        Ellipse2D outer = new Ellipse2D.Double(0, 0, width, height);
        double inX = (width/2) - (width/2 - borderThickness);
        double inY = (height/2) - (height/2 - borderThickness);
        double inW = width - 2*borderThickness;
        double inH = height - 2*borderThickness;
        Ellipse2D inner = new Ellipse2D.Double(inX, inY, inW, inH);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }

    //==============================================
    // Needed for highlighting the button correctly.
    //==============================================

    @Override
    public void mousePressed(MouseEvent e) {
        if (isInOval(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted.darker();
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isInOval(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted;
        }
        else {
            currentBackground = colorNormal;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isInOval(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted;
        }
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        currentBackground = colorNormal;
        backgroundHighlighted = false;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isInOval(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted;
        }
        else {
            currentBackground = colorNormal;
            backgroundHighlighted = false;
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }
}
