package ooga.model.gameobjects;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import ooga.model.util.Action;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;


/**
 * Checks for collisions and uses reflection to create and execute the methods corresponding to the
 * collisions that have occurred each frame
 *
 * @author jincho juhyounglee
 */
public class DestroyableCollisionHandling implements Serializable {

  public DestroyableCollisionHandling() {
  }

  /**
   * Checks for collision between two objects and returns second object entity tags if they
   * collided. Returns empty list if there is no collision.
   *
   * @param myself reference GameObject
   * @param o      secondary GameObject
   * @return secondary GameObject's entity tags or empty list if no collision
   */
  public List<String> determineCollisionMethods(GameObject myself, GameObject o) {

    List<String> ret = new ArrayList<>();
    Vector[] collisionRect = determineCollisionRectangle(myself, o);
    if (collisionRect == null) {
      return ret;
    }

    return getCollisionMethods(myself, o.getEntityType(), collisionRect);
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

  /**
   * returns the top left and bottom right coord of the rectangle made by the intersection
   *
   * @param myself
   * @param o
   * @return ret[0] is top left, ret[1] is bot right
   */
  public Vector[] determineCollisionRectangle(GameObject myself, GameObject o) {
    double x5 = Math.max(myself.getPredictedPosition().getX(), o.getPredictedPosition().getX());
    double y5 = Math.max(myself.getPredictedPosition().getY(), o.getPredictedPosition().getY());
    Vector myBotRight = myself.getPredictedPosition().add(myself.getSize());
    Vector oBotRight = o.getPredictedPosition().add(o.getSize());
    double x6 = Math.min(myBotRight.getX(), oBotRight.getX());
    double y6 = Math.min(myBotRight.getY(), oBotRight.getY());
    if (x5 > x6 || y5 > y6) {
      return null;
    }

    Vector[] ret = new Vector[2];
    ret[0] = new Vector(x5, y5);
    ret[1] = new Vector(x6, y6);
    return ret;
  }

  private List<String> getCollisionMethods(GameObject myself, List<String> oTags,
      Vector[] collisionBox) {
    Action edgeAction = calculateCollisionDirection(myself, collisionBox).getDirection();

    List<String> collisionMethods = new ArrayList<>(oTags);
    for (String types : oTags) {
      collisionMethods.add(types + "-" + edgeAction);
    }

    return collisionMethods;
  }

  /**
   * determines the direction of the collision based on the rectangle overlap created by the
   * colliding objects
   *
   * @param myself       game object that is checking for its own collisions
   * @param collisionBox game object that it is colliding with and checking for the direction of the
   *                     collision
   * @return the unit vector that defines the direction of the collision and can be translated into
   * an Action
   */
  public Vector calculateCollisionDirection(GameObject myself, Vector[] collisionBox) {
    Vector collisionCenter = (collisionBox[0].add(collisionBox[1]))
        .multiply(new Vector(0.5, 0.5));
    Vector myCenter = (myself.getPredictedPosition()
        .add(myself.getSize().multiply(new Vector(0.5, 0.5))));
    List<Vector> edgeMidpoints = getEdgeMidpoints(myself);
    double minDistance = Double.MAX_VALUE;
    Vector minDistanceEdge = new Vector(-1, -1);
    for (Vector edgeMidpoint : edgeMidpoints) {
      double dist = collisionCenter.subtract(edgeMidpoint).calculateMagnitude();
      if (dist < minDistance) {
        minDistance = dist;
        minDistanceEdge = edgeMidpoint;
      }
    }
    return minDistanceEdge.subtract(myCenter).toUnit();
  }

  private List<Vector> getEdgeMidpoints(GameObject o) {
    List<Vector> edges = new ArrayList<>();

    Vector center = new Vector(o.getPredictedPosition().getX() + 0.5 * o.getSize().getX(),
        o.getPredictedPosition().getY() + 0.5 * o.getSize().getY());
    edges.add(center.add(new Vector(0, -0.5 * o.getSize().getY())));
    edges.add(center.add(new Vector(0, 0.5 * o.getSize().getY())));
    edges.add(center.add(new Vector(-0.5 * o.getSize().getX(), 0)));
    edges.add(center.add(new Vector(0.5 * o.getSize().getX(), 0)));

    return edges;
  }

  /**
   * checks to see if the overlap is a small corner aka a corner collision and ignores if so
   *
   * @param me game object that is detecting the collision
   * @param o  game object that object is colliding with
   * @return true if it is a corner collision
   */
  public boolean smallCorner(GameObject me, GameObject o) {
    Vector[] rect = determineCollisionRectangle(me, o);
    if (rect == null) {
      return false;
    }
    double width = rect[1].getX() - rect[0].getX();
    double height = rect[1].getY() - rect[0].getY();
    return width + height < 10;
  }

}


