package ooga.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Actor extends GameObject {
  public Map<String, Class[]> methodBank;
  private int lives;
  private int health;
  private boolean isDead;
  public List<Method> collisions;
  private List<PropertyChangeListener> myListeners;

  /**
   * Default constructor with default lives, health values
   */
  public Actor(List<String> entityTypes, Vector position, Vector velocity, double gravity, int id, double size) {
    super(entityTypes, position, velocity, gravity, id, size);
    lives = 5;
    health = 10;
    gravity = 1;
    myListeners = new ArrayList<>();
    collisions = new ArrayList<>();
    methodBank = makeMethodBank();
  }

  /**
   * Constructor to specify initial number of lives and amount of health
   */
  public Actor(List<String> entityTypes, Vector position, Vector velocity, double gravity, int id, double size, int startLife, int startHealth) {
    super(entityTypes, position, velocity, gravity, id, size);
    lives = startLife;
    health = startHealth;
    myListeners = new ArrayList<>();
    collisions = new ArrayList<>();
    methodBank = makeMethodBank();
  }

  abstract void stepMovement(double elapsedTime);

  private Map<String, Class[]> makeMethodBank() {
    Map<String, Class[]> ret = new HashMap<>();
    Method[] methods = this.getClass().getDeclaredMethods();
    for(Method m : methods) {
      Class[] parameterTypes = m.getParameterTypes();
      ret.put(m.getName(), parameterTypes);
    }
    return ret;
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
  public boolean isDead() {
    // TODO implement here
    return isDead;
  }

  /**
   * create a Queue of all methods to invoke on self for collisions with other GameObjects
   */
  public void addCollision(List<String> method){
    for (String m : method) {
      if(!methodBank.containsKey(m)) {
        //throw error that method doesnt exist
        return;
      }
      try {
        Class[] paramTypes = methodBank.get(m);
        Method collisionResponse = this.getClass().getDeclaredMethod(m, paramTypes);
        collisions.add(collisionResponse);
      } catch (Exception e) {
      }
    }
  }

  /**
   * execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions() {
    while (!collisions.isEmpty()) {
      Method curr = collisions.remove(0);
      try {
        curr.invoke(this);
      } catch(Exception e) { }
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
