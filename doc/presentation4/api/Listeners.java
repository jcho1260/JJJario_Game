import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Handles listeners for observable pattern
 */
public interface Observable {

    /**
     * Adds single PropertyChangeListener to allListeners.
     *
     * @param newListener is the listener that is being connected in teh observable pattern
     */
    public void addListener(String sourceKey, PropertyChangeListener newListener);

    /**
     * Notify added listener of sourceKey of a change.
     *
     * @param sourceKey indicates what the purpose of the listener is/the listener it's connected to
     * @param property indicates the property that has changed and is being passed up
     * @param oldValue indicates previous value of the property
     * @param newValue indicates the new value of the property
     */
    public void notifyListenerKey(String sourceKey, String property, Object oldValue, Object newValue);

}


public class Sprite implements PropertyChangeListener {

    public ImageView getImageView();

    public void changeHeight(Double h);

    public void changeWidth(Double w);

    public void changeVisibility(Boolean b);

    public void changeX(Double x);

    public void changeY(Double y);

}

public class GameView implements PropertyChangeListener {

    public void addSprite(Sprite s);

    public void addScore(int score);

    public void addLife(int life);

    public void addHealth(int health);

    public void changeScore(int score);

    public void changeLife(int newLife);

    public void changeHealth(int newHealth);
}
