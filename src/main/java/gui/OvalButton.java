package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.geom.Area;

/**
 * This class represents an oval button. This means that the button will only trigger
 * an acton if it is clicked within the oval shape drawn on it.
 *
 * @author Luka Kralj
 * @version 11 December 2018
 */
public class OvalButton extends JButton implements MouseListener, MouseMotionListener {

    public static final int SHAPE_OVAL = 0;
    public static final int SHAPE_CAPSULE = 1;
    public static final int VERTICAL = 2;
    public static final int HORIZONTAL = 4;

    private Color colorNormal;
    private Color colorHighlighted;
    private Color colorBorderNormal;
    private Color colorBorderHighlighted;
    private int borderThickness;
    private boolean borderHighlighted;
    private boolean backgroundHighlighted;
    private Color currentBackground;
    protected final int shape;
    protected final int orientation;
    protected double radius;

    /**
     * Construct a default oval button.
     */
    public OvalButton() {
        this(SHAPE_OVAL, VERTICAL);
    }

    /**
     * Construct an oval button with this text.
     *
     */
    public OvalButton(int shape, int orientation) {
        this(shape, orientation, Color.WHITE, Color.LIGHT_GRAY, Color.BLACK, Color.RED);
    }

    /**
     * Construct an oval button and specify its custom colour.
     *
     * @param shape Shape of the button. Select one of the constants.
     * @param colorNormal The main color of the button.
     * @param colorHighlighted The color of the button when it is highlighted (hovered over etc.).
     * @param colorBorderNormal The main border color of the button.
     * @param colorBorderHighlighted The border color that will show whenever the button is marked as being
     *                               highlighted (will remain this color even after the mouse exits).
     */
    public OvalButton(int shape, int orientation, Color colorNormal, Color colorHighlighted, Color colorBorderNormal, Color colorBorderHighlighted) {
        super();
        if (shape != SHAPE_CAPSULE && shape != SHAPE_OVAL) {
            throw new IllegalArgumentException("Invalid shape chosen for OvalButton.");
        }
        if (orientation != VERTICAL && orientation != HORIZONTAL) {
            throw new IllegalArgumentException("Invalid orientation chosen for OvalButton.");
        }
        this.shape = shape;
        this.orientation = orientation;
        radius = 0.5;
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
     * Set radius of the button. 1 means same as SHAPE_OVAL.
     * @param newRadius New radius: between 0 and 1.
     */
    public void setRadius(double newRadius) {
        if (newRadius < 0 || newRadius > 1) {
            throw new IllegalArgumentException("Invalid radius: " + newRadius);
        }
        radius = newRadius;
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
            if (isValidClickPosition(MouseInfo.getPointerInfo().getLocation())) {
                l.actionPerformed(e);
            }
        });
    }

    protected void holeClicked(ActionEvent e) {
        if (isValidClickPosition(MouseInfo.getPointerInfo().getLocation())) {
            super.actionListener.actionPerformed(e);
        }
    }

    protected boolean isValidClickPosition(Point screenPosition) {
        if (shape == SHAPE_OVAL) {
            return isInOval(screenPosition);
        }
        else {
            return isInCapsule(screenPosition);
        }
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
     * Check if the specified point is withing the capsule of the button.
     *
     * @param p Point to check.
     * @return
     */
    private boolean isInCapsule(Point p) {
        double x = p.x;
        double y = p.y;

        if (orientation == VERTICAL) {
            Double r = 0.5 * radius * getSize().height;
            double buttonX = getLocationOnScreen().x;
            double buttonY = getLocationOnScreen().y;
            if (y < buttonY + r) {
                // check if in upper ellipse

                // center of ellipse, semi-major and semi-minor axis
                double s1 = buttonX + getSize().width / 2;
                double s2 = buttonY + r;
                double a = getSize().width / 2;
                double b = r;

                return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
            }
            else if (y > buttonY + getSize().height - r) {
                // check if in lower ellipse

                // center of ellipse, semi-major and semi-minor axis
                double s1 = buttonX + getSize().width / 2;
                double s2 = buttonY + getSize().height - r;
                double a = getSize().width / 2;
                double b = r;

                return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
            }
            else {
                return true;
            }
        }
        else {
            Double r = 0.5 * radius * getSize().width;
            double buttonX = getLocationOnScreen().x;
            double buttonY = getLocationOnScreen().y;
            if (x < buttonX + r) {
                // check if in upper ellipse

                // center of ellipse, semi-major and semi-minor axis
                double s1 = buttonX + r;
                double s2 = buttonY + getSize().height / 2;
                double a = r;
                double b = getSize().height / 2;

                return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
            }
            else if (x > buttonX + getSize().width - r) {
                // check if in lower ellipse

                // center of ellipse, semi-major and semi-minor axis
                double s1 = buttonX + getSize().width - r;
                double s2 = buttonY + getSize().height / 2;
                double a = r;
                double b = getSize().height / 2;

                return ((((x - s1)*(x - s1)) / (a*a)) + (((y - s2)*(y - s2)) / (b*b))) <= 1;
            }
            else {
                return true;
            }
        }
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
        if (shape == SHAPE_OVAL) {
            paintOval(gr);
        }
        else if (shape == SHAPE_CAPSULE) {
            paintCapsule(gr);
        }
    }

    /**
     * Paints oval with border on the button.
     *
     * @param g Graphic to paint the oval on.
     */
    private void paintOval(Graphics2D g) {
        Dimension d = getSize();

        setMainColor(g);
        g.fillOval(0, 0, d.width, d.height);
        Shape border = createOvalBorder();

        if (borderHighlighted) {
            g.setColor(colorBorderHighlighted);
        }
        else {
            g.setColor(colorBorderNormal);
        }
        g.fill(border);
    }

    /**
     * Creates an oval border shape (like a ring).
     *
     * @return Border shape.
     */
    private Shape createOvalBorder() {
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

    /**
     * Sets the main background color of the button according to its state.
     *
     * @param g Graphic to set the color for.
     */
    private void setMainColor(Graphics g) {
        if (isEnabled() && backgroundHighlighted) {
            g.setColor(colorHighlighted);
        }
        else if (isEnabled()){
            g.setColor(currentBackground);
        }
        else {
            g.setColor(colorNormal);
        }
    }

    /**
     * Paints a capsule shape with border to the button.
     *
     * @param g Graphic to paint the capsule on.
     */
    private void paintCapsule(Graphics2D g) {
        Dimension d = getSize();

        Shape mainCapsule = createCapsule(0, 0, d.width, d.height);
        setMainColor(g);
        g.fill(mainCapsule);

        Shape border = createCapsuleBorder();
        if (borderHighlighted) {
            g.setColor(colorBorderHighlighted);
        }
        else {
            g.setColor(colorBorderNormal);
        }
        g.fill(border);
    }

    /**
     * Creates the capsule shape that corresponds to the current button state (vertical/horizontal).
     *
     * @param x x coordinate of the top left corner of the capsule.
     * @param y x coordinate of the top left corner of the capsule.
     * @param width Width of the capsule.
     * @param height Height of the capsule.
     * @return Capsule shape.
     */
    private Shape createCapsule(double x, double y, double width, double height) {
        double r;
        Ellipse2D upper;
        Rectangle2D middle;
        Ellipse2D lower;
        if (orientation == VERTICAL) {
            r = 0.5 * radius * height;
            upper = new Ellipse2D.Double(x, y, width, 2 * r);
            middle = new Rectangle2D.Double(x, y + r, width, height - 2 * r);
            lower = new Ellipse2D.Double(x, y + (height - 2 * r), width, 2 * r);
        }
        else {
            r = 0.5 * radius * width;
            upper = new Ellipse2D.Double(x, y, 2 * r, height);
            middle = new Rectangle2D.Double(x + r, y, width - 2 * r, height);
            lower = new Ellipse2D.Double(x + (width - 2 * r), y, 2 * r, height);
        }

        Area capsule = new Area(upper);
        capsule.add(new Area(middle));
        capsule.add(new Area(lower));

        return capsule;
    }

    /**
     * Creates a capsule border shape.
     *
     * @return Border shape.
     */
    private Shape createCapsuleBorder() {
        double width = getSize().width;
        double height = getSize().height;
        Shape outer = createCapsule(0, 0, width, height);
        double inX = (width/2) - (width/2 - borderThickness);
        double inY = (height/2) - (height/2 - borderThickness);
        double inW = width - 2*borderThickness;
        double inH = height - 2*borderThickness;
        Shape inner = createCapsule(inX, inY, inW, inH);
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        return area;
    }

    //==============================================
    // Needed for highlighting the button correctly.
    //==============================================

    @Override
    public void mousePressed(MouseEvent e) {
        if (isValidClickPosition(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted.darker();
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isValidClickPosition(e.getLocationOnScreen())) {
            currentBackground = colorHighlighted;
        }
        else {
            currentBackground = colorNormal;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isValidClickPosition(e.getLocationOnScreen())) {
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
        if (isValidClickPosition(e.getLocationOnScreen())) {
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
        System.out.println("===== NO ACTION: ovalButton mouse clicked.");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("===== NO ACTION: ovalButton dragged.");
    }
}
