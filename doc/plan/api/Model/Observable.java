
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * 
 */
public interface Observable {

    /**
     * 
     */
    public void addMultipleListeners(List<PropertyChangeListener> listeners);

    /**
     * @param listener
     */
    public void removeListener(PropertyChangeListener listener);

    /**
     * @param property
     * @param oldValue
     * @param newValue
     */
    public Object notifyListeners(String property, Object oldValue,Object newValue);

}