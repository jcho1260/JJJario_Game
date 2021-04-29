package ooga.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import ooga.JjjanException;
import ooga.model.GameWorld;

/**
 * GameSaver is responsible for saving and loading games. It is dependent on ooga.JjjanException
 * and ooga.model.GameWorld;
 *
 * @author Noah Citron
 */
public class GameSaver {

  /**
   * Saves a GameWold to a file
   *
   * @param game      the name of the game type
   * @param level     the level name to save
   * @param name      the name of the save file
   * @param gameWorld the GameWold to save
   * @throws IOException
   * @throws JjjanException
   */
  public void saveGame(String game, String level, String name, GameWorld gameWorld)
      throws IOException, JjjanException {

    if (gameWorld == null) {
      throw new JjjanException("Invalid Game World");
    }

    String path = "data/saves/" + game + "/" + level;
    new File(path).mkdirs();

    FileOutputStream f = new FileOutputStream(path + "/" + name + ".game");
    ObjectOutput s = new ObjectOutputStream(f);
    s.writeObject(gameWorld);
  }

  /**
   * Loads a game from a save
   *
   * @param game  the name of the game
   * @param level the name of the level
   * @param name  the name of the save file
   * @return      the GameWold recreated from the save file
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public GameWorld loadGame(String game, String level, String name)
      throws IOException, ClassNotFoundException {

    String path = "data/saves/" + game + "/" + level;

    FileInputStream in = new FileInputStream(path + "/" + name);
    ObjectInputStream s = new ObjectInputStream(in);
    return (GameWorld) s.readObject();
  }

  /**
   * Gets a list of saves
   *
   * @param game  the game name
   * @param level the level name
   * @return      an array of save names
   */
  public String[] getSaves(String game, String level) {
    File folder = new File("data/saves/" + game + "/" + level);
    return Arrays.stream(folder.listFiles()).map(File::getName).toArray(String[]::new);
  }
}
