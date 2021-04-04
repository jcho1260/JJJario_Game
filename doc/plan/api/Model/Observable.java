import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Handles listeners for observable pattern. Based off of Duvall's Observable lab example.
 */
public interface Observable {

    /**
     * Add List of listeners.
     *
     * @param listeners
     */
    public void addMultipleListeners(List<PropertyChangeListener> listeners);

    /**
     * Notify added listeners of a change.
     *
     * @param property
     * @param oldValue
     * @param newValue
     */
    public void notifyListeners(String property, Object oldValue,Object newValue);
}