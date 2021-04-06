package ooga.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Actor extends GameObject {
  private int lives;
  private int health;
  private boolean isDead;
  private Map<Method, List<double[]>> collisions; //TODO: refactor so its not a list of arrays?
  private List<PropertyChangeListener> myListeners;

  /**
   * Default constructor with default lives, health values
   */
  public Actor(List<String> entityTypes, Vector position, Vector velocity, double gravity, int id, Vector size) {
    super(entityTypes, position, velocity, gravity, id, size);
    lives = 5;
    health = 10;
    gravity = 1;
    myListeners = new ArrayList<>();
    collisions = new HashMap<>();
  }

  /**
   * Constructor to specify initial number of lives and amount of health
   */
  public Actor(List<String> entityTypes, Vector position, Vector velocity, double gravity, int id, Vector size, int startLife, int startHealth) {
    super(entityTypes, position, velocity, gravity, id, size);
    lives = startLife;
    health = startHealth;
    myListeners = new ArrayList<>();
    collisions = new HashMap<>();
  }

  abstract void stepMovement(double elapsedTime, double gameGravity);

  /**
   *
   * @return
   */
  public int getLives() {
    return lives;
  }

  /**
   *
   * @return
   */
  public int getHealth() {
    return health;
  }

  /**
   *
   */
  public boolean isDead() {
    return isDead;
  }

  /**
   * create a Queue of all methods to invoke on self for collisions with other GameObjects
   */
  public void addCollision(List<MethodBundle> methods) throws NoSuchMethodException {
    for (MethodBundle m : methods) {
      Method method = m.makeMethod(this);
      collisions.putIfAbsent(method, new ArrayList<>());
      collisions.get(method).add(m.getParameters());
    }
  }

  /**
   * execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions() {
    for (Method m : collisions.keySet()) {
      for (double[] params : collisions.get(m)) {
        try {
          m.invoke(this, params);
        } catch(Exception e) { }
      }

    }
  }

  protected void kill() {
    isDead = true;
  }

  /**
   * changes health of an actor
   * @param change the amount the actor's health should change
   */
  protected void incrementHealth(int change) {
    lives += change;
    if (lives == 0) { isDead = true; }
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
