package ooga.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  /**
   * create a Queue of all methods to invoke on self for collisions with other GameObjects
   */
  public Map<Method, List<double[]>> addCollision(Class destroyableClass, List<MethodBundle> methods, Map<Method, List<double[]>> collisions) throws NoSuchMethodException {
    for (MethodBundle m : methods) {
      Method method = m.makeMethod(destroyableClass);
      collisions.putIfAbsent(method, new ArrayList<>());
      collisions.get(method).add(m.getParameters());
    }
    return collisions;
  }

  /**
   * execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions(Object destroyable, Map<Method, List<double[]>> collisions) {
    for (Method m : collisions.keySet()) {
      for (double[] params : collisions.get(m)) {
        try {
          m.invoke(destroyable, params);
        } catch(Exception e) { }
      }
    }
  }

}
