package ooga.model;

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
  protected double size;

  /**
   * Default constructor
   */
  public GameObject(List<String> entityTypes, Vector position, Vector velocity, double gravityScale, int id, double size) {
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

  /**
   *
   */
  public void getEntityType() {
    // TODO implement here
  }

  /**
   *
   */
  public String getEntitiyState() {
    // TODO implement here
    return null;
  }

  /**
   * @param b
   */
  public boolean isCollision(GameObject b) {
    // TODO implement here
    return true;
  }

  /**
   *
   */
  public boolean isActive() {
    // TODO implement here
    return true;
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
