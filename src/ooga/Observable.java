package ooga;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles listeners for observable pattern. Based off of Duvall's Observable lab example.
 */
public abstract class Observable {

  private List<PropertyChangeListener> allListeners = new ArrayList<>();

  /**
   * Adds single PropertyChangeListener to allListeners.
   *
   * @param newListener
   */
  public void addListener(PropertyChangeListener newListener) {
    allListeners.add(newListener);
  }

  /**
   * Adds List of PropertyChangeListeners to allListeners.
   *
   * @param newListeners
   */
  public void addMultipleListeners(List<PropertyChangeListener> newListeners) {
    allListeners.addAll(newListeners);
  }

  /**
   * Notify added listeners of a change.
   *
   * @param property
   * @param oldValue
   * @param newValue
   * @return
   */
  public void notifyListeners(String property, Object oldValue, Object newValue) {
    for (int i = 0; i < allListeners.size(); i++) {
      notifyListenerIndex(i, property, oldValue, newValue);
    }
  }

  /**
   * Notify added listener at index i of a change.
   * @param index
   * @param property
   * @param oldValue
   * @param newValue
   */
  public void notifyListenerIndex(int index, String property, Object oldValue, Object newValue) {
    allListeners.get(index).propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
  }
}