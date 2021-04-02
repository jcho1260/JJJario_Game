package ooga.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Actor extends GameObject {
  private int lives;
  private int health;
  private List<PropertyChangeListener> myListeners;

  /**
   * Default constructor
   */
  public Actor(int startLife, int startHealth) {
    lives = startLife;
    health = startHealth;
    myListeners = new ArrayList<>();
  }

  /**
   *
   * @return
   */
  public int getLives() {
    // TODO implement here
    return lives;
  }

  /**
   *
   * @return
   */
  public int getHealth() {
    // TODO implement here
    return health;
  }

  /**
   *
   * @param change
   */
  protected void incrementHealth(int change) {
    // TODO implement here
    lives += change;
  }

  /**
   *
   */
  public void handleCollision(List<String> methods){
    List<Method> collisionEffects = new ArrayList<>();
    for(String m : methods) {
      try {
        Method collisionResponse = this.getClass().getDeclaredMethod(m);
        collisionEffects.add(collisionResponse);
      } catch (Exception e) {
      }
    }
    for(Method m : collisionEffects) {
      try {
        m.invoke(this);
      } catch (Exception e) {
      }
    }
  }

  /**
   *
   */
  public boolean isAlive() {
    // TODO implement here
    return true;
  }


  /**
   * Add List of listeners.
   *
   * @param newListeners
   */
  public void addMultipleListeners(List<PropertyChangeListener> newListeners) {
    for (PropertyChangeListener l : newListeners) {
      if (l != null) {
        myListeners.add(l);
      }
    }
  }

  /**
   * Notify added listeners of a change.
   *
   * @param property
   * @param oldValue
   * @param newValue
   */
  public void notifyListeners(String property, Object oldValue,Object newValue) {
    for (PropertyChangeListener l : myListeners) {
      l.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
    }
  }
}
