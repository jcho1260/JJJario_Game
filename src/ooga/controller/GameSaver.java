package ooga.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import ooga.model.GameWorld;

public class GameSaver {

  public void saveGame(String game, String level, String name, GameWorld gameWorld) {
    String path = "data/saves/" + game + "/" + level;
    new File(path).mkdirs();

    try {
      FileOutputStream f = new FileOutputStream(path + "/" + name + ".game");
      ObjectOutput s = new ObjectOutputStream(f);
      s.writeObject(gameWorld);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public GameWorld loadGame(String game, String level, String name) {

    String path = "data/saves/" + game + "/" + level;

    try {
      FileInputStream in = new FileInputStream(path + "/" + name + ".game");
      ObjectInputStream s = new ObjectInputStream(in);
      return (GameWorld) s.readObject();
    } catch(IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String[] getSaves(String game, String level) {
    File folder = new File("data/saves/" + game + "/" + level);
    return Arrays.stream(folder.listFiles()).map(File::getName).toArray(String[]::new);
  }
}
