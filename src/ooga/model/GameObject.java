package ooga.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents GameObject - any component of the game that has position and associate movement
 * properties.
 *
 * @author Jin Cho, Jessica Yang
 */
public abstract class GameObject {
  private List<String> entityTypes;
  private Vector position;
  private int id;
  private Vector size;

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

  public abstract Vector getVelocty();
}
