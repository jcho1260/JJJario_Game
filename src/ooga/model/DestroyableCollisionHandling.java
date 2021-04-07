package ooga.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class DestroyableCollisionHandling {

  public DestroyableCollisionHandling() { }

  /**
   * Checks for collision between two objects and returns second object entity tags if they
   * collided. Returns empty list if there is no collision.
   *
   * @param myself reference GameObject
   * @param o secondary GameObject
   * @return secondary GameObject's entity tags or empty list if no collision
   */
  public List<String> determineCollisionMethods(GameObject myself, GameObject o) {
    List<String> ret = new ArrayList<>();
    if (!isCollision(myself, o)) {
      return ret;
    }
    return getCollisionMethods(myself, o);
  }

  /**
   * execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions(Object destroyable, Queue<MethodBundle> collisions)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    while (!collisions.isEmpty()) {
      MethodBundle mb = collisions.remove();
      Method m = mb.makeMethod(destroyable.getClass());
      m.invoke(destroyable, mb.getParameters());
    }
  }

  private boolean isCollision(GameObject myself, GameObject o) {
    double oX = o.getPosition().getX();
    double oY = o.getPosition().getY();
    Vector me = myself.getPosition();
    Vector size = myself.getSize();
    if (oX == me.getX() && oX <= me.getX() + size.getX()) {
      if (oY >= me.getY() && oY <= me.getY() + size.getY()) {
        return true;
      }
    }
    return false;
  }

  private List<String> getCollisionMethods(GameObject myself, GameObject o) {
    Vector myVelocity = myself.getVelocity();
    Vector oVelocity = o.getVelocity();

    String myDirection = myVelocity.getDirection().toString();
    String oDirection = oVelocity.getDirection().toString();
    String collisionDirection = "";

    if (myDirection.equals(oDirection)) {
      if (!myVelocity.equals(new Vector(0,0))) {
        collisionDirection = oDirection;
      } else {
        return new ArrayList<>();
      }
    } else {
      collisionDirection = oVelocity.multiply(new Vector(-1,-1)).getDirection().toString();
    }

    List<String> ret = new ArrayList<>();
    for (String s : o.getEntityType()) {
      ret.add(s + collisionDirection);
    }
    return ret;
  }
}
