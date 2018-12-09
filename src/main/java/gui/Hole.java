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
 * @version 07 December 2018
 */
public class Hole extends OvalButton {

    private List<Korgool> korgools;
    private boolean isKazan;
    private List<Location> korgoolLocations;
    private Korgool tuzKorgool;
    private Random rand;
    private Rectangle korgoolArea;
    private static Dimension korgoolSize;

    /**
     * Construct an empty hole. To add korgools to it, use one of the functions.
     */
    public Hole(boolean isKazan) {
        korgools = new ArrayList<>(32);
        rand = new Random();
        korgoolLocations = generateLocations();
        korgoolArea = new Rectangle(0,0,10,10);
        korgoolSize = new Dimension(10, 10);
        setLayout(null);
        this.isKazan = isKazan;
        // Update korgools size and location only when the hole is moved/resized.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeKorgools();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                resizeKorgools();
            }
        });
    }

    private void resizeKorgools() {
        double oldW = korgoolArea.width;
        double oldH = korgoolArea.height;
        updateKorgoolArea();
        double newW = korgoolArea.width;
        double newH = korgoolArea.height;
        double coefW = newW / oldW;
        double coefH = newH / oldH;

        for (int i = 0; i < korgools.size(); i++) {
            Korgool k = korgools.get(i);
            k.setSize(korgoolSize);
            k.setLocation((int)Math.round((k.getLocation().x * coefW)), (int)Math.round((k.getLocation().y * coefH)));
        }

        if (tuzKorgool != null) {
            tuzKorgool.setSize(korgoolSize);
            tuzKorgool.setLocation((int)Math.round((tuzKorgool.getLocation().x * coefW)), (int)Math.round((tuzKorgool.getLocation().y * coefH)));
        }
    }

    /**
     * Adds a korgool to this hole. All hole and korgool paramenters are updated accordingly.
     *
     * @param k Korgool to add.
     */
    public void addKorgool(Korgool k) {
        add(k);
        Point next = getNextLocation(0);
        if (k.getName() != null && (k.getName().equals("rightTuzKorgool") || k.getName().equals("leftTuzKorgool"))) {
            tuzKorgool = k;
            next = getNextLocation(165 - getNumberOfKorgools());
        }
        else {
            korgools.add(k);
        }
        k.setParentHole(this);
        k.setSize(korgoolSize);
        k.setLocation(next);
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
        if (tuzKorgool != null) {
            remove(tuzKorgool);
        }
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
        return tuzKorgool != null;
    }

    public Korgool releaseTuzKorgool() {
        Korgool toReturn = tuzKorgool;
        tuzKorgool = null;
        return toReturn;
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
        if (!isKazan) {
            korgoolSize = new Dimension((int)diameter, (int)diameter);
        }
        else if (korgoolSize != null){
            diameter = korgoolSize.width;
        }

        korgoolArea = new Rectangle((int)x, (int)y, (int)(newW - diameter), (int)(newH - diameter));
    }

    private List<Location> generateLocations() {
        List<Location> locations = new ArrayList<>(170);
        for (int i = 0; i < 170; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            locations.add(new Location(x, y));
        }
        return locations;
    }

    /**
     *
     * @param offset Numbers of korgools that need to be added before this position will be valid.
     * @return Location of the korgool that would be added after 'offset' korgools.
     */
    public Point getNextLocation(int offset) {
        updateKorgoolArea();
        Location next = korgoolLocations.get(getNumberOfKorgools() + offset);
        double x = korgoolArea.x + next.x * korgoolArea.width;
        double y = korgoolArea.y + next.y * korgoolArea.height;

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

    private class Location {
        double x;
        double y;

        private Location(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

}
