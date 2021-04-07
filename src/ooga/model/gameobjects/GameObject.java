package ooga.model.gameobjects;

import java.util.List;
import ooga.Observable;
import ooga.model.util.Vector;
import ooga.model.gameobjectcomposites.Size;
import ooga.model.util.MethodBundle;

/**
 * Represents GameObject - any component of the game that has position and associate movement
 * properties.
 *
 * @author Jin Cho, Juhyoung Lee, Jessica Yang
 */
public class GameObject extends Observable {
  private List<String> entityTypes;
  private Vector position;
  private int id;
  private Size size;

  /**
   * Default constructor
   */
  public GameObject(List<String> entityTypes, Vector position, int id, Vector size) {
    this.entityTypes = entityTypes;
    this.position = position;
    this.id = id;
    this.size = new Size(size);
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
    return size.getSize();
  }

  /**
   * @param methods
   */
  public void step(List<MethodBundle> methods) {
    // TODO implement here
  }

  public void step(double elapsedTime, double gameGravity) {

  }

  public Vector getVelocity() {
    return new Vector(0,0);
  }

}
