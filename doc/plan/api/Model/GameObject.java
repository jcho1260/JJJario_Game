
import java.util.*;

/**
 * 
 */
public class GameObject {


    private String entityType;
    protected int frameCount;
    protected int xPosition;
    protected int yPosition;
    protected Vector velocity;


    /**
     * Default constructor
     */
    public GameObject() {
    }


    /**
     * 
     */
    public int getX() {
        // TODO implement here
        return 0;
    }

    /**
     * 
     */
    public int getY() {
        // TODO implement here
        return 0;
    }

    /**
     * 
     */
    public void getEntityType() {
        // TODO implement here
    }

    /**
     * 
     */
    public String getEntitiyState() {
        // TODO implement here
        return null;
    }

    /**
     * @param b
     */
    public boolean isCollision(GameObject b) {
        // TODO implement here
        return true;
    }

    /**
     * 
     */
    public boolean isActive() {
        // TODO implement here
        return true;
    }

    /**
     * @param frameCount
     * @param methods
     */
    public void step(int frameCount, List<String> methods) {
        // TODO implement here
    }

    /**
     * @param vel
     */
    public void setVelocity(Vector vel) {
        // TODO implement here
    }

}