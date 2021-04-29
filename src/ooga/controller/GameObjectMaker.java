package ooga.controller;

import ooga.model.gameobjects.GameObject;

/**
 * GameObjectMaker is a class that can be reflectively converted into any of the GameObjects. It is dependent on
 * ooga.model.gameobjects.GameObject
 *
 * @author Noah Citron
 */
public class GameObjectMaker {

  String type;
  Object[] attributes;

  /**
   * Creates a new GameObjectMaker. Assumes that the type string is an actual GameObject subclass, and that the attribute
   * are the correctly ordered list of parameters for that particular tpe.
   *
   * @param type        a string representing the type of GameObject
   * @param attributes  the parameters required to construct the subclass of GameObject
   */
  public GameObjectMaker(String type, Object[] attributes) {
    this.type = type;
    this.attributes = attributes;
  }

  /**
   * Instantiates a GameObject from the information provided by the constructor
   *
   * @return  a GameObject
   * @throws GameObjectMakerException
   */
  public GameObject createGameObject() throws GameObjectMakerException {

    try {
      Class clazz = Class.forName(type);
      return (GameObject) clazz.getConstructors()[0].newInstance(attributes);
    } catch (Exception e) {
      throw new GameObjectMakerException("cannot create GameObject");
    }
  }
}
