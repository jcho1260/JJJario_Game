package ooga.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ooga.JjjanException;
import ooga.Observable;
import ooga.model.gameobjectcomposites.WorldCollisionHandling;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.Player;
import ooga.model.util.Action;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;


/**
 *
 */
public class GameWorld extends Observable {

  private List<GameObject> allGameObjects;
  private List<GameObject> allActiveGameObjects;
  private List<GameObject> allDestroyables;
  private List<GameObject> allActiveDestroyables;
  private WorldCollisionHandling worldCollisionHandling;
  private int score;
  private Player player;
  private Vector windowSize;

  private final double gravity;
  private final double stepTime;

  /**
   * Default constructor
   */
  public GameWorld(Player gamePlayer, Map<String, Map<String, List<MethodBundle>>> collisionMethods,
      List<GameObject> gameObjects, List<GameObject> actors, Vector frameSize, double levelGravity, double frameRate) {
    player = gamePlayer;
    windowSize = frameSize;
    allGameObjects = gameObjects;
    gravity = levelGravity;
    stepTime = frameRate;
    allActiveGameObjects = findActiveObjects(allGameObjects);
    allDestroyables = actors;
    allActiveDestroyables = findActiveObjects(allDestroyables);
    worldCollisionHandling = new WorldCollisionHandling(collisionMethods, gameObjects, actors);
  }

  public void stepFrame(Action pressEffect)
      throws NoSuchMethodException, JjjanException, InvocationTargetException, IllegalAccessException {

    worldCollisionHandling.detectAllCollisions();
    List<Integer> forDeletion = worldCollisionHandling.executeAllCollisions();
    removeDeadActors(forDeletion);
    allActiveGameObjects = findActiveObjects(allGameObjects);
    allActiveDestroyables = findActiveObjects(allDestroyables);
    worldCollisionHandling.updateActiveGameObjects(allActiveGameObjects, allActiveDestroyables);
  }

  // TODO: refactor out isActive from GameObject and calculate active status here DO THIS !!!!!
  private List<GameObject> findActiveObjects(List<GameObject> allObjects) {
    Vector topL = getWindowFrame()[0];
    Vector botR = getWindowFrame()[1];
    List<GameObject> ret = new ArrayList<>();
    for (GameObject o : allObjects) {
      Vector oTopL = o.getPosition();
      Vector oTopR = o.getPosition().add(new Vector(o.getSize().getX(), 0));
      Vector oBotL = o.getPosition().add(new Vector(0,o.getSize().getY()));
      Vector oBotR = o.getPosition().add(new Vector(o.getSize().getX(),o.getSize().getY()));

      if (oTopL.insideBox(topL,botR) || oTopR.insideBox(topL,botR) || oBotL.insideBox(topL, botR) || oBotR.insideBox(topL,botR)) {
        ret.add(o);

        //setvisibility
      }
    }
    return ret;
  }

  // TODO LATER
  private Vector[] getWindowFrame() {
    return new Vector[]{new Vector(0,0), new Vector(50,15)};
  }

  private void removeDeadActors(List<Integer> deadActors) {
    allGameObjects = removeIndicesFromList(allGameObjects, deadActors);
    allDestroyables = removeIndicesFromList(allDestroyables, deadActors);
  }

  private List<GameObject> removeIndicesFromList(List<GameObject> objects, List<Integer> deadActors) {
    for(int j = objects.size() - 1; j >= 0; j--) {
      if (deadActors.contains(objects.get(j).getId())) {
        objects.remove(j);
      }
    }
    return objects;
  }

  private List<Integer> extractActiveId() {
    List<Integer> id = new ArrayList<>();
    for (GameObject o : allActiveGameObjects) {
      id.add(o.getId());
    }
    return id;
  }

  /**
   *
   */
  public List<GameObject> getAllDestroyables() {
    // TODO implement here
    return allDestroyables;
  }

  /**
   *
   */
  public List<GameObject> getActiveActors() {
    // TODO implement here
    return allActiveDestroyables;
  }

  /**
   *
   */
  public List<GameObject> getAllGameObjects() {
    // TODO implement here
    return allGameObjects;
  }

  /**
   *
   */
  public List<GameObject> getActiveGameObjects() {
    // TODO implement here
    return null;
  }

  public double getGravity() {
    return gravity;
  }

  public double getStepTime() {
    return stepTime;
  }


  /**
   * Returns score of the Player.
   *
   * @return score
   */
  public int getScore() {
    return score;
  }
}