package logic;

import gui.GameWindow;
import gui.Hole;
import gui.Korgool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnimationController extends Thread {
    public static final int MOVE_ALL = 256;
    public static final int EMPTY_HOLE = 512;
    public static final int MOVE_KORGOOLS = 1024;
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    private static AnimationController instance;

    // Time in milliseconds, how long we want the animation to be.
    private static final int RUN_TIME = 500;
    private long startTime;

    private List<AnimEvent> events;
    private GameWindow animateFor;
    private JPanel glassPane;
    private List<Korgool> toDistribute;
    private boolean stop;
    private int currentEvent;

    public static AnimationController instance() {
        return instance;
    }

    public static AnimationController resetController(GameWindow animateFor) {
        instance = new AnimationController(animateFor);
        return instance;
    }

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
        for (int i = 0; i < numOfKorgools; i++) {
            if (toDistribute.isEmpty()) {
                break;
            }
            toMove.add(toDistribute.remove(toDistribute.size() - 1));
        }

        Point paneLoc = animateFor.getContentPane().getLocationOnScreen();

        List<AnimKorgool> animKorgools = toMove.stream().map(k ->
                new AnimKorgool(k, k.getLocation(), new Point(hole.getLocationOnScreen().x - paneLoc.x + hole.getSize().width/2, hole.getLocationOnScreen().y - paneLoc.y + hole.getSize().height/2))
                )
                .collect(Collectors.toList());
        performMove(animKorgools, hole);
    }

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
                boolean wasResizable = animateFor.isResizable();
                animateFor.setResizable(false);
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
                animateFor.setResizable(wasResizable);
            }
        }
    }

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

}
