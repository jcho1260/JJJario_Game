package ooga.model;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
  private List<String> entityTypes;
  protected int frameCount;
  private PhysicsEngine movement;
  protected Vector position;
  protected Vector velocity;
  protected Vector acceleration;
  protected double gravityScale;
  protected int id;
  protected boolean isActive;
  protected Vector size;

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
    isActive = checkActiveStatus();
  }

  private boolean checkActiveStatus() {
    return true;
  }


  /**
   *
   */
  public int getX() {
    // TODO implement here
    return 0;
  }

  /**
   *
   */
  public int getY() {
    // TODO implement here
    return 0;
  }

  public Vector getVelocity() {
    // todo: return new vector
    return velocity;
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

  /**
   * @param b
   */
  public List<String> isCollision(GameObject b) {
    List<String> ret = new ArrayList<>();
    if (b.getX() == position.getX() && b.getX() <= position.getX() + size.getX()) {
      if (b.getY() >= position.getY() && b.getY() <= position.getY() + size.getY()) {
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
   *
   */
  public boolean isActive() {
    return isActive;
  }

  private void updateActive() {
    isActive = true;
    //todo: implement
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
    // TODO implement here
  }

}
