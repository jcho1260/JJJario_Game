package ooga.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Represents GameObject - any component of the game that has position and associate movement
 * properties.
 *
 * @author Jin Cho, Juhyoung Lee, Jessica Yang
 */
public class GameObject implements Observable {
  private List<String> entityTypes;
  private Vector position;
  private int id;
  private Vector size;
  private List<PropertyChangeListener> allListeners;

  /**
   * Default constructor
   */
  public GameObject(List<String> entityTypes, Vector position, int id, Vector size) {
    this.entityTypes = entityTypes;
    this.position = position;
    this.id = id;
    this.size = size;
  }

  /**
   * Returns x coordinate of position.  // TODO return the position vector instead?
   *
   * @return x coordinate
   */
  public Vector getPosition() {
    return position.copy();
  }

  protected void setPosition(Vector newPosition) {
    position = newPosition;
  }

  /**
   *
   */
  public List<String> getEntityType() {
    // TODO implement here
    return entityTypes;
  }

  public int getId() {
    return id;
  }

  public Vector getSize() {
    return size.copy();
  }

  /**
   * @param frameCount
   * @param methods
   */
  public void step(int frameCount, List<String> methods) {
    // TODO implement here
  }

  public Vector getVelocity() {
    return new Vector(0,0);
  }

  /**
   * Adds List of PropertyChangeListeners to allListeners.
   *
   * @param newListeners
   */
  @Override
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
  @Override
  public void notifyListeners(String property, Object oldValue, Object newValue) {
    for (PropertyChangeListener l : allListeners) {
      l.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
    }
  }
}
