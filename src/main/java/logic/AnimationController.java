package logic;

import gui.GameWindow;

import javax.swing.*;
import java.util.List;

public class AnimationController {
    public static final int MOVE_ALL = 256;
    public static final int EMPTY_HOLE = 512;
    public static final int MOVE_KORGOOLS = 1024;
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    private List<AnimEvent> events;
    private GameWindow animateFor;

    public AnimationController(GameWindow animateFor) {
        if (animateFor == null) {
            throw new NullPointerException("Animation controller cannot work without a GameWindow.");
        }
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
    }

    /**
     * Add animation event to the queue. The event will be executed when run() is called.
     * This event will involve all the korgools available.
     *
     * @param eventType Use one of the constants EMPTY_HOLE or MOVE_KORGOOLS.
     * @param holeId Use hole id or one of the constants LEFT or RIGHT for kazans.
     */
    public void addEvent(int eventType, String holeId) {
        addEvent(eventType, holeId, MOVE_ALL);
    }

    public void run() {
        // make frame temporarily not resizeable
        // create top transparent layer to block all actions
        // iterate and execute all event
        // remove top layer to allow the game to continue
        // make window resizeable again
        events.clear(); // only remove events once for performance
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
