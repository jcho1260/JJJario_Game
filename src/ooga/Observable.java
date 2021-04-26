package ooga;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles listeners for observable pattern. Based off of Duvall's Observable lab example.
 */
public abstract class Observable {

  private Map<String, PropertyChangeListener> allListeners = new HashMap<>();

  /**
   * Adds single PropertyChangeListener to allListeners.
   *
   * @param newListener
   */
  public void addListener(String sourceKey, PropertyChangeListener newListener) {
    allListeners.put(sourceKey, newListener);
  }

  /**
   * Notify added listener at index i of a change.
   * @param sourceKey
   * @param property
   * @param oldValue
   * @param newValue
   */
  public void notifyListenerKey(String sourceKey, String property, Object oldValue, Object newValue) {
    if (allListeners.containsKey(sourceKey)) {
      allListeners.get(sourceKey).propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
    }
  }
}