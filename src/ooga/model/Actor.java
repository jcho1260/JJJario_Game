package ooga.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Actor extends GameObject {
  private int lives;
  private int health;
  private boolean isDead;
  private Queue<Method> collisions;
  private List<PropertyChangeListener> myListeners;

  /**
   * Default constructor with default lives, health values
   */
  public Actor(List<String> entityTypes, Vector position, Vector velocity, double gravity) {
    super(entityTypes, position, velocity, gravity);
    lives = 5;
    health = 10;
    gravity = 1;
    myListeners = new ArrayList<>();
    collisions = new PriorityQueue();
  }

  /**
   * Constructor to specify initial number of lives and amount of health
   */
  public Actor(List<String> entityTypes, Vector position, Vector velocity, double gravity, int startLife, int startHealth) {
    super(entityTypes, position, velocity, gravity);
    lives = startLife;
    health = startHealth;
    myListeners = new ArrayList<>();
    collisions = new PriorityQueue();
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
   */
  public boolean isAlive() {
    // TODO implement here
    return !isDead;
  }

  /**
   *
   * @param change
   */
  protected void incrementHealth(int change) {
    // TODO implement here
    lives += change;
    if (lives == 0) { isDead = true; }
  }

  /**
   * create a Queue of all methods to invoke on self for collisions with other GameObjects
   */
  public void addCollision(List<String> method){
    for (String m : method) {
      try {
        Method collisionResponse = this.getClass().getDeclaredMethod(m);
        collisions.add(collisionResponse);
      } catch (Exception e) { }
    }
  }

  /**
   * execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions() {
    while (!collisions.isEmpty()) {
      Method curr = collisions.remove();
      try {
        curr.invoke(this);
      } catch(Exception e) { }
    }
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
