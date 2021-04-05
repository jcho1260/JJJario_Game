package ooga.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents GameObject - any component of the game that has position and associate movement
 * properties.
 *
 * @author Jin Cho, Jessica Yang
 */
public class GameObject {
  private List<String> entityTypes;
  private Vector position;
  private Vector velocity;
  private double gravityScale;
  private int id;
  private Vector size;

  /**
   * Default constructor
   */
  public GameObject(List<String> entityTypes, Vector position, Vector velocity, double gravityScale, int id, Vector size) {
    this.entityTypes = entityTypes;
    this.position = position;
    this.velocity = velocity;
    this.gravityScale = gravityScale;
    this.id = id;
    this.size = size;
  }

  private boolean checkActiveStatus() {
    return true;
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

  protected double getGravityScale() {
    return gravityScale;
  }

  public Vector getSize() {
    return size.copy();
  }

  /**
   * @param b
   */
  public List<String> isCollision(GameObject b) {
    List<String> ret = new ArrayList<>();
    double bX = b.getPosition().getX();
    double bY = b.getPosition().getY();
    if (bX== position.getX() && bX<= position.getX() + size.getX()) {
      if (bY >= position.getY() && bY <= position.getY() + size.getY()) {
        List<String> collisionInfo = b.getEntityType();
        Vector bDirection = b.getVelocity().multiply(new Vector(-1,-1));
        String collisionDirection = bDirection.getDirection().toString();
        for (String s : collisionInfo) {
          ret.add(s + collisionDirection);
        }
      }
    }
    return ret;
  }

  /**
   * @param frameCount
   * @param methods
   */
  public void step(int frameCount, List<String> methods) {
    // TODO implement here
  }

  /**
   * @param vel
   */
  public void setVelocity(Vector vel) {
    velocity = vel;
  }

  /**
   * Returns velocity of GameObject for child classes.
   *
   * @return velocity
   */
  protected Vector getVelocity() {
    return velocity.copy();
  }
}
