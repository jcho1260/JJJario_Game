package ooga;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles listeners for observable pattern. Based off of Duvall's Observable lab example. Extended
 * by classes notify listeners that implement the Observer pattern.
 *
 * @author Jessica Yang
 */
public abstract class Observable {

  private final Map<String, PropertyChangeListener> allListeners = new HashMap<>();

  /**
   * Adds single PropertyChangeListener to allListeners.
   *
   * @param newListener
   */
  public void addListener(String sourceKey, PropertyChangeListener newListener) {
    allListeners.put(sourceKey, newListener);
  }

  /**
   * Notify added listener with a given String key.
   *
   * @param sourceKey
   * @param property
   * @param oldValue
   * @param newValue
   */
  public void notifyListenerKey(String sourceKey, String property, Object oldValue,
      Object newValue) {
    if (allListeners.containsKey(sourceKey)) {
      allListeners.get(sourceKey)
          .propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
    }
  }
}