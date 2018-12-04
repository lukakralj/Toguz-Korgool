package logic;

import gui.GameWindow;
import gui.Hole;
import gui.Korgool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class manages the animations of the korgools. To ensure that all classes are using the same instance of
 * the controller, the instance of this class is accessed via a static instance() method.
 *
 * To schedule a new event use one of the addEvent methods.
 * To start execution of the events call run() on the instance.
 * The events are being executed in order as they were added. New events can be added to the queue even after the
 * execution has started.
 * The animations can be stopped with stopAnimator(). However, the instance needs to be reset after that.
 *
 * @author Luka Kralj
 * @version 29 November 2018
 */
public class AnimationController extends Thread {
    /** Marks that all available korgools need to be included in the event. */
    public static final int MOVE_ALL = 256;
    /** Event type that specifies that one of the holes needs to be emptied. The korgools will be put in the centre. */
    public static final int EMPTY_HOLE = 512;
    /** Event type that specifies that korgools need to be moved from the centre to one of the holes. */
    public static final int MOVE_KORGOOLS = 1024;
    /** Use this constant as hole id to specify that the left kazan is involved in the event. */
    public static final String LEFT = "left";
    /** Use this constant as hole id to specify that the right kazan is involved in the event. */
    public static final String RIGHT = "right";

    private static AnimationController instance;

    // Time in milliseconds, how long we want the animation to be.
    private static int RUN_TIME = 1000;
    private long startTime;

    private List<AnimEvent> events;
    private GameWindow animateFor;
    private JPanel glassPane;
    private List<Korgool> toDistribute;
    private boolean stop;
    private int currentEvent; // index of the event that is currently being executed

    /**
     *
     * @return Currently active instance of this class.
     */
    public static AnimationController instance() {
        return instance;
    }

    /**
     * Resets the instance of this class.
     *
     * @param animateFor The window that this animator will be used for.
     * @return Currently active instance of this class.
     */
    public static AnimationController resetController(GameWindow animateFor) {
        if (instance != null) {
            instance.stopAnimator();
            if (instance.glassPane != null) {
                instance.animateFor.getLayeredPane().remove(instance.glassPane);
            }
            instance = null;
        }
        instance = new AnimationController(animateFor);
        return instance;
    }

    /**
     * Construct new controller.
     *
     * @param animateFor The window that this animator will be used for.
     */
    private AnimationController(GameWindow animateFor) {
        if (animateFor == null) {
            throw new NullPointerException("Animation controller cannot work without a GameWindow.");
        }
        events = new ArrayList<>();
        toDistribute = new ArrayList<>();
        this.animateFor = animateFor;
        stop = false;
        glassPane = new JPanel();
        glassPane.setLayout(null);
        glassPane.setOpaque(false);
        glassPane.setSize(animateFor.getContentPane().getSize());
        glassPane.setLocation(0, 0);
        animateFor.getLayeredPane().add(glassPane, new Integer(1));
        currentEvent = -1;
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

    /**
     * Prepares the korgools for an empty event (remove from hole, calculate targets).
     *
     * @param id Id of the hole we are removing from.
     */
    private void emptyEvent(String id) {
        if (id == LEFT || id == RIGHT) {
            System.out.println("Tried to animate emptying of the kazans - not defined.");
            return;
        }

        Hole hole = animateFor.getButtonMap().get(id);
        List<Korgool> toMove = hole.releaseKorgools();
        Point paneLoc = animateFor.getContentPane().getLocationOnScreen();
        toMove.forEach(k -> {
            Point kLoc = k.getLocationOnScreen();
            glassPane.add(k);
            toDistribute.add(k);
            k.setLocation(kLoc.x - paneLoc.x, kLoc.y - paneLoc.y);
        });

        List<AnimKorgool> animKorgools = toMove.stream().map(k ->
                new AnimKorgool(k, k.getLocation(), new Point(animateFor.getContentPane().getSize().width/2, animateFor.getContentPane().getSize().height/2))
                )
                .collect(Collectors.toList());
        performMove(animKorgools, null);
    }

    /**
     * Prepares the korools for a move event (calculate targets, add to the correct hole).
     *
     * @param id Id of the hole we are moving to.
     * @param numOfKorgools Number of korgools we want to move.
     */
    private void moveEvent(String id, int numOfKorgools) {
        Hole hole;
        if (id == LEFT) {
            hole = animateFor.getKazanLeft();
        }
        else if (id == RIGHT) {
            hole = animateFor.getKazanRight();
        }
        else {
            hole = animateFor.getButtonMap().get(id);
        }
        List<Korgool> toMove = new ArrayList<>();
        numOfKorgools = (numOfKorgools == MOVE_ALL) ? toDistribute.size() : numOfKorgools;
        for (int i = 0; i < numOfKorgools; i++) {
            if (toDistribute.isEmpty()) {
                break;
            }
            toMove.add(toDistribute.remove(toDistribute.size() - 1));
        }

        Point paneLoc = animateFor.getContentPane().getLocationOnScreen();

        List<AnimKorgool> animKorgools = toMove.stream().map(k ->
                new AnimKorgool(k, k.getLocation(),
                        new Point((hole.getLocationOnScreen().x - animateFor.getContentPane().getLocationOnScreen().x) + hole.getNextLocation().x,
                                  (hole.getLocationOnScreen().y - animateFor.getContentPane().getLocationOnScreen().y) + hole.getNextLocation().y))
                )
                .collect(Collectors.toList());
        //System.out.println("==== Korgool target: (" + animKorgools.get(0).target.x + ", " + animKorgools.get(0).target.y + ")");
        //System.out.println("==== Target hole: (" + (hole.getLocationOnScreen().x - animateFor.getContentPane().getLocationOnScreen().x) + ", " + (hole.getLocationOnScreen().y - animateFor.getContentPane().getLocationOnScreen().y) + ")");
        //System.out.println("==== Hole next loc: (" + hole.getNextLocation().x + ", " + hole.getNextLocation().y + ")");
        performMove(animKorgools, hole);
    }

    /**
     * Executes the moving of korgools in the list.
     *
     * @param korgools List of korgools to move.
     * @param newParent The hole we are moving the korgools to. Null if we are moving them in the centre.
     */
    private void performMove(List<AnimKorgool> korgools, Hole newParent) {
        startTime = System.currentTimeMillis();
        boolean endMove = false;
        while (!endMove) {
            long duration = System.currentTimeMillis() - startTime;
            double progress = (double)duration / (double)RUN_TIME;
            if (progress > 1f) {
                endMove = true;
                if (newParent != null) {
                    korgools.forEach(k -> newParent.addKorgool(k.korgool));
                }
            }
            else {
                korgools.forEach(k -> {
                    Point newLocation = newPoint(k.start, k.target, progress);
                    k.korgool.setLocation(newLocation);
                });
                glassPane.repaint();
            }
        }
    }

    /**
     * Start executing the events.
     */
    public void run() {
        while (!stop) {
            try {
                synchronized (this) {
                    wait(10);
                }
            }
            catch (InterruptedException e) {
                System.out.println("Interrupted wait.");
            }
            if (currentEvent != events.size() - 1) {
                glassPane.setSize(animateFor.getContentPane().getSize());
                currentEvent++;
                AnimEvent e = events.get(currentEvent);
                if (e.type == EMPTY_HOLE) {
                    emptyEvent(e.id);
                }
                else if (e.type == MOVE_KORGOOLS) {
                    moveEvent(e.id, e.numOfKorgools);
                }
                else {
                    throw new RuntimeException("Invalid AnimEvent type: " + e.type);
                }
            }
        }
    }

    /**
     * Stop animator.
     */
    public void stopAnimator() {
        stop = true;
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

    /**
     * Convenience class for keeping all the relevant information about the event together.
     */
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

    /**
     * Convenience class to keep all relevant information about the movement of the specific korgool together.
     */
    private class AnimKorgool {
        private Korgool korgool;
        private Point start;
        private Point target;

        private AnimKorgool(Korgool k, Point start, Point target) {
            this.korgool = k;
            this.start = start;
            this.target = target;
        }
    }

    /**
     * Set new run time for all animations. The higher the value the slower the animations will be.
     *
     * @param timeInMillis
     */
    public static void setRunTime(int timeInMillis) {
        RUN_TIME = timeInMillis;
    }
}
