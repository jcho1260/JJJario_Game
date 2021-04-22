package ooga.controller;

import java.lang.reflect.InvocationTargetException;

import ooga.model.gameobjects.GameObject;

public class GameObjectMaker {

  String name;
  Object[] attributes;

  public GameObjectMaker(String name, Object[] attributes) {
    this.name = name;
    this.attributes = attributes;
  }

  public GameObject createGameObject() throws GameObjectMakerException {

    try {
      Class clazz = Class.forName(name);
      return (GameObject) clazz.getConstructors()[0].newInstance(attributes);
    } catch (Exception e) {
      throw new GameObjectMakerException("cannot create GameObject");
    }
  }
}
