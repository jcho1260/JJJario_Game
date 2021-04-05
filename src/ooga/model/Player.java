package ooga.model;

import java.util.*;

/**
 * 
 */
public class Player extends Actor{

    private int score;
    private List<GameObject> activePowerUps;
    /**
     * Default constructor
     */
    public Player(List<String> entityTypes, Vector position, Vector velocity, double gravity, int id, Vector size) {
        super(entityTypes, position, velocity, gravity, id, size);
    }

    /**
     * 
     */
    public int getScore() {
        // TODO implement here
        return 0;
    }

    /**
     * 
     */
    public List<GameObject> getActivePowerUps() {
        // TODO implement here
        return null;
    }


    @Override
    void stepMovement(double elapsedTime) {
    }
}