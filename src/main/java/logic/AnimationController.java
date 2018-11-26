package logic;

import gui.GameWindow;
import gui.Hole;
import gui.Korgool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationController {
    public static final int MOVE_ALL = 256;
    public static final int EMPTY_HOLE = 512;
    public static final int MOVE_KORGOOLS = 1024;
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    // Time in milliseconds, how long we want the animation to be.
    private static final int RUN_TIME = 1000;
    private long startTime;

    private List<AnimEvent> events;
    private GameWindow animateFor;
    private JPanel glassPane;
    private int numOfNotLocated;
    private boolean wasResizable;

    public AnimationController(GameWindow animateFor) {
        if (animateFor == null) {
            throw new NullPointerException("Animation controller cannot work without a GameWindow.");
        }
        events = new ArrayList<>();
        numOfNotLocated = 0;
        this.animateFor = animateFor;
    }

    /**
     * Add animation event to the queue. The event will be executed when run() is called.
     *
     * @param eventType Use one of the constants EMPTY_HOLE or MOVE_KORGOOLS.
     * @param holeId Use hole id or one of the constants LEFT or RIGHT for kazans.
     * @param numOfKorgools Number of korgools we want to move.
     */
    public void addEvent(int eventType, String holeId, int numOfKorgools) {
        events.add(new AnimEvent(eventType, holeId, numOfKorgools));
        System.out.println("=======Event added.");
    }

    /**
     * Add animation event to the queue. The event will be executed when run() is called.
     * This event will involve all the korgools available. EMPTY_HOLE will always move all.
     *
     * @param eventType Use one of the constants EMPTY_HOLE or MOVE_KORGOOLS.
     * @param holeId Use hole id or one of the constants LEFT or RIGHT for kazans.
     */
    public void addEvent(int eventType, String holeId) {
        addEvent(eventType, holeId, MOVE_ALL);
    }

    public void runEvents() {
        System.out.println("=======Run events.");
        // make frame temporarily not resizable
        wasResizable = animateFor.isResizable();
        animateFor.setResizable(false);
        System.out.println("=======Resizing disabled.");
        // create top transparent layer to block all actions
        glassPane = new JPanel();
        glassPane.setLayout(null);
        glassPane.setOpaque(false);
        glassPane.setSize(animateFor.getContentPane().getSize());
        glassPane.setLocation(0, 0);
        animateFor.getLayeredPane().add(glassPane, new Integer(1));
        System.out.println("=======Glass pane added.");
        // iterate and execute all event
        for (AnimEvent e : events) {
            if (e.type == EMPTY_HOLE) {
                emptyEvent(e.id);
            }
        }


        events.clear(); // only remove events once for performance
        System.out.println("=======Events cleared.");
    }

    private void emptyEvent(String id) {
        if (id == LEFT || id == RIGHT) {
            System.out.println("Tried to animate emptying of the kazans - not defined.");
            return;
        }

        Hole hole = animateFor.getButtonMap().get(id);
        List<Korgool> toMove = hole.releaseKorgools();
        numOfNotLocated += toMove.size();
        Point paneLoc = animateFor.getContentPane().getLocationOnScreen();
        toMove.forEach(k -> {
            Point kLoc = k.getLocationOnScreen();
            glassPane.add(k);
            k.setLocation(kLoc.x - paneLoc.x, kLoc.y - paneLoc.y);
        });

        // Animate all korgools:
        toMove.forEach(k -> {
            performMove(k, k.getLocation(), new Point(animateFor.getContentPane().getSize().width/2, animateFor.getContentPane().getSize().height/2), null);
        });
    }

    private void performMove(Korgool k, Point start, Point target, Hole newParent) {
        /*startTime = System.currentTimeMillis();
        boolean stop = false;
        while (!stop) {
            long duration = System.currentTimeMillis() - startTime;
            double progress = (double)duration / (double)RUN_TIME;
            if (progress > 1f) {
                progress = 1f;
                stop = true;
                if (newParent != null) {
                    newParent.addKorgool(k);
                }
            }
            Point newLocation = newPoint(start, target, progress);
            k.setLocation(newLocation);
            glassPane.repaint();
        }*/

        Timer timer = new Timer(0, e -> {
            long duration = System.currentTimeMillis() - startTime;
            double progress = (double)duration / (double)RUN_TIME;
            if (progress > 1f) {
                progress = 1f;
                ((Timer)e.getSource()).stop();
                if (newParent != null) {
                    newParent.addKorgool(k);
                    numOfNotLocated--;
                }
                if (numOfNotLocated == 0) {
                    // remove top layer to allow the game to continue
                    animateFor.getLayeredPane().remove(glassPane);
                    glassPane = null;
                    System.out.println("=======Glass pane destroyed.");
                    // reset the window's resizable property
                    animateFor.setResizable(wasResizable);
                }
            }
            Point newLocation = newPoint(start, target, progress);
            k.setLocation(newLocation);
            glassPane.repaint();
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        startTime = System.currentTimeMillis();
        timer.start();
    }


    /**
     * Calculates new location for the korgool.
     *
     * @param startPoint Starting location.
     * @param targetPoint Location we want to reach.
     * @param progress Relative fraction from the beginning of animation.
     * @return New location of the korgool.
     */
    private Point newPoint(Point startPoint, Point targetPoint, double progress) {
        Point point = new Point();
        point.x = newValue(startPoint.x, targetPoint.x, progress);
        point.y = newValue(startPoint.y, targetPoint.y, progress);
        return point;
    }

    /**
     * Calculates new values according to distance and current progress.
     *
     * @param startValue Value at which the animation started.
     * @param targetValue  Value we want to reach.
     * @param fraction Progress of the animation.
     * @return New value.
     */
    private int newValue(int startValue, int targetValue, double fraction) {
        int value;
        int distance = targetValue - startValue;
        value = (int)Math.round((double)distance * fraction);
        value += startValue;

        return value;
    }















    private class AnimEvent {
        private int type;
        private String id;
        private int numOfKorgools;

        private AnimEvent(int type, String id, int numOfKorgools) {
            this.type = type;
            this.id = id;
            this.numOfKorgools = numOfKorgools;
        }

    }

}
