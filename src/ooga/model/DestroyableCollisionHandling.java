package ooga.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DestroyableCollisionHandling {

  public DestroyableCollisionHandling() { }

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
