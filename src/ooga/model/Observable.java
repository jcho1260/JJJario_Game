package ooga.model;

import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Handles listeners for observable pattern. Based off of Duvall's Observable lab example.
 */
public interface Observable {

    /**
     * Add List of listeners.
     *
     * @param newListeners
     */
    void addMultipleListeners(List<PropertyChangeListener> newListeners);

    /**
     * Notify added listeners of a change.
     *
     * @param property
     * @param oldValue
     * @param newValue
     */
    Object notifyListeners(String property, Object oldValue,Object newValue);

}