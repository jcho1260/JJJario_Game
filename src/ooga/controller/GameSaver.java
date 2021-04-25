package ooga.controller;

import java.io.*;
import java.util.Arrays;
import ooga.model.GameWorld;

public class GameSaver {

  public void saveGame(String game, String level, String name, GameWorld gameWorld) throws IOException {
    String path = "data/saves/" + game + "/" + level;
    new File(path).mkdirs();

    FileOutputStream f = new FileOutputStream(path + "/" + name + ".game");
    ObjectOutput s = new ObjectOutputStream(f);
    s.writeObject(gameWorld);
  }

  public GameWorld loadGame(String game, String level, String name) throws IOException, ClassNotFoundException {

    String path = "data/saves/" + game + "/" + level;

    FileInputStream in = new FileInputStream(path + "/" + name);
    ObjectInputStream s = new ObjectInputStream(in);
    return (GameWorld) s.readObject();
  }

  public String[] getSaves(String game, String level) {
    File folder = new File("data/saves/" + game + "/" + level);
    return Arrays.stream(folder.listFiles()).map(File::getName).toArray(String[]::new);
  }
}
