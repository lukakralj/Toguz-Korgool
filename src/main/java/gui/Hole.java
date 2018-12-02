package gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a hole in the game. It can be used either for player holes
 * or for their kazans.
 *
 * @author Luka Kralj
 * @version 21 November 2018
 */
public class Hole extends OvalButton {

    private List<Korgool> korgools;
    private boolean isTuz;

    private Random rand;
    private Rectangle korgoolArea;
    private Dimension korgoolSize;

    /**
     * Construct an empty hole. To add korgools to it, use one of the functions.
     */
    public Hole() {
        korgools = new ArrayList<>(32);
        rand = new Random();
        korgoolArea = new Rectangle(0,0,10,10);
        korgoolSize = new Dimension(10, 10);
        setLayout(null);
        isTuz = false;
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);

        // Update korgools size and location only when the hole is moved/resized.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateKorgoolArea();
                for (Korgool k : korgools) {
                    k.setSize(korgoolSize);
                    //TODO: the korgools should stay in the same place once they are in the hole. For now the
                    //TODO: for now they are always randomly placed.
                    k.setLocation(calculateKorgoolLocation());
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                updateKorgoolArea();
                for (Korgool k : korgools) {
                    k.setSize(korgoolSize);
                    //TODO: the korgools should stay in the same place once they are in the hole. For now the
                    //TODO: for now they are always randomly placed.
                    k.setLocation(calculateKorgoolLocation());
                }
            }
        });
    }

    /**
     * Adds a korgool to this hole. All hole and korgool paramenters are updated accordingly.
     *
     * @param k Korgool to add.
     */
    public void addKorgool(Korgool k) {
        updateKorgoolArea();
        add(k);
        korgools.add(k);
        k.setParentHole(this);
        k.setSize(korgoolSize);
        k.setLocation(calculateKorgoolLocation());
        revalidate();
        repaint();
    }

    /**
     * Adds all korgools in the list to this hole.
     *
     * @param korgoolList Korgools to add.
     */
    public void addKorgools(List<Korgool> korgoolList) {
        for (Korgool k : korgoolList) {
            addKorgool(k);
        }
    }

    /**
     * A convenience method for instantiating and adding new korgools to the hole.
     *
     * @param numOfKorgools Number of new korgools that we want to add to this hole.
     */
    public void createAndAdd(int numOfKorgools) {
        for (int i = 0; i < numOfKorgools; i++) {
            addKorgool(new Korgool(this));
        }
    }

    /**
     *
     * @return Number of korgools in this hole.
     */
    public int getNumberOfKorgools() {
        return korgools.size();
    }

    /**
     * Returns the list of korgools in the hole but does not remove them.
     *
     * @return List of korgools in this hole.
     */
    public List<Korgool> getKorgools() {
        return korgools;
    }

    /**
     * Removes and destroys all korgools in this hole.
     */
    public void emptyHole() {
        korgools.forEach(this::remove);
        korgools = new ArrayList<>(32);
    }

    /**
     * Removes and returns all korgools in this hole.
     *
     * @return List of korgools that were in this hole.
     */
    public List<Korgool> releaseKorgools() {
        List<Korgool> toReturn = korgools;
        korgools = new ArrayList<>(32);
        return toReturn;
    }

    /**
     *
     * @return True if hole is set as tuz, false if not.
     */
    public boolean isTuz() {
        return isTuz;
    }

    /**
     * Decide if this hole is a tuz or not. The hole border will be highlighted accordingly.
     *
     * @param isTuz True if you want to make this hole a tuz, false otherwise.
     */
    public void setTuz(boolean isTuz) {
        this.isTuz = isTuz;
        if (isTuz) {
            setHighlightedBorder(true);
        }
        else {
            setHighlightedBorder(false);
        }
    }

    /**
     * Updates the area on which the korgools can be drawn.
     * It also updates the size of korgools.
     */
    private void updateKorgoolArea() {
        /*
        The formulas below are derived from formulas for a linear function and ellipse.

        ellipse:  ((x - x')^2 / a^2) + ((y - y')^2 / b^2) = 1

                  (x', y') is the center of ellipse, which is (hole-width/2, hole-height/2)
                  Follows: semi-major axis a = hole-width/2
                           semi-minor axis b = hole-height/2

        linear:   y = kx + n
                  The origin is top left corner which is (0,0) so n = 0.
                  I take the one liner function that goes through the centre of ellipse,
                  from which follow: y = (b/a) x

        Insert linear function in ellipse equation and we can then calculate the intersect at (x, y).
        This is origin of the new korgoolsArea.
         */
        if (getSize().height == 0 || getSize().width == 0) {
            return;
        }

        double sqrt2 = 1.4142135623730951;

        double a = (double)getSize().width / 2;
        double b = (double)getSize().height / 2;

        double x = ((a*(2 - sqrt2)) / 2) + getBorderThickness();
        double y = ((b*(2 - sqrt2)) / 2) + getBorderThickness();

        double newW = (double)getSize().width - 2*x;
        double newH = (double)getSize().height - 2*y;

        double diameter = (newW < newH) ? (newW / 4) : (newH / 4);
        korgoolSize = new Dimension((int)diameter, (int)diameter);

        korgoolArea = new Rectangle((int)x, (int)y, (int)(newW - diameter), (int)(newH - diameter));
    }

    /**
     * Randomly allocates korgools within the valid korgool area.
     *
     * @return Point of the top left corner of where the korgool should be placed.
     */
    public Point calculateKorgoolLocation() {
        double x = korgoolArea.x + rand.nextDouble() * korgoolArea.width;
        double y = korgoolArea.y + rand.nextDouble() * korgoolArea.height;

        return new Point((int)x, (int)y);
    }

    /**
     * Takes care of rendering the oval button correctly.
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.drawString("" + korgools.size(), (int)(getSize().width * 0.08), (int)(getSize().height * 0.95));
    }

}
