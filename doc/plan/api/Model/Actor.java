

/**
 * 
 */
public abstract class Actor extends GameObject{

    /**
     * Default constructor
     */
    public Actor() {
    }

    /**
     * 
     */
    public int getLives() {
        // TODO implement here
        return 0;
    }

    /**
     * 
     */
    public int getHealth() {
        // TODO implement here
        return 0;
    }

    /**
     * @param change
     */
    protected void incrementHealth(int change) {
        // TODO implement here
    }

    /**
     * 
     */
    public abstract void handleCollision();

    /**
     * 
     */
    public boolean isAlive() {
        // TODO implement here
        return true;
    }

}