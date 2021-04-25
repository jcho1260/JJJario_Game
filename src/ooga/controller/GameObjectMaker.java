package ooga.controller;

import java.util.Arrays;
import ooga.model.gameobjects.GameObject;

public class GameObjectMaker {

  String type;
  Object[] attributes;

  public GameObjectMaker(String type, Object[] attributes) {
    this.type = type;
    this.attributes = attributes;
  }

  public GameObject createGameObject() throws GameObjectMakerException {

    try {
      Class clazz = Class.forName(type);
      return (GameObject) clazz.getConstructors()[0].newInstance(attributes);
    } catch (Exception e) {
      throw new GameObjectMakerException("cannot create GameObject");
    }
  }
}
