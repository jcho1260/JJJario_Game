package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Statement;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;
import ooga.model.util.Action;
import ooga.view.launcher.ProfileView;

/**
 * Profiles represent user profiles. They contain functionality for modifying and saving themselves. It is dependent on
 * ooga.model.util.Action and ooga.view.launcher.ProfileView.
 *
 * @author Noah Ctiron
 */
public class Profile implements Serializable, PropertyChangeListener {

  private final Map<KeyCode, Action> keybinds;
  private final Map<String, Map<String, Integer>> highScores;
  private String name;
  private String picture;

  /**
   * Creates a new profile
   *
   * @param name  the name of the profile
   * @throws IOException
   */
  public Profile(String name) throws IOException {
    this.name = name;
    this.picture = "view_resources/images/button_icons/User.png";
    this.keybinds = new HashMap<>();
    keybinds.put(KeyCode.W, Action.UP);
    keybinds.put(KeyCode.A, Action.LEFT);
    keybinds.put(KeyCode.S, Action.DOWN);
    keybinds.put(KeyCode.D, Action.RIGHT);
    keybinds.put(KeyCode.SPACE, Action.SHOOT);
    this.highScores = new HashMap<>();
    save();
  }

  /**
   * Gets the highscore map
   *
   * @return  a map representing the highscores. The keys of the outer map is the game name, while the key of the
   *          inner map is the level name. The values of the inner map is the highscore
   */
  public Map<String, Map<String, Integer>> getHighScores() {
    return highScores;
  }

  /**
   * Sets the username
   *
   * @param name  username
   */
  public void setUsername(String name) {
    this.name = name;
  }

  /**
   * Sets the profile picture
   *
   * @param f profile picture path
   */
  public void setPicture(String f) {
    this.picture = f;
  }

  /**
   * Sets a keybind
   *
   * @param bind  a pair from keycode to action representing the keybind
   */
  public void setKeyBind(Pair<KeyCode, String> bind) {
    Action action = Action.valueOf(bind.getValue());

    KeyCode[] keys = keybinds.keySet().toArray(KeyCode[]::new);
    for (KeyCode key : keys) {
      if (keybinds.get(key) == action) {
        keybinds.remove(key);
      }
    }
    keybinds.put(bind.getKey(), action);
  }

  /**
   * Gets the keybind map
   *
   * @return  keybind map
   */
  public Map<KeyCode, Action> getKeybinds() {
    return keybinds;
  }

  /**
   * Displays the profile on the screen
   *
   * @param pv  the ProfileView frontend component
   */
  public void display(ProfileView pv) {
    pv.makeMenu(name, picture, keybinds, highScores);
  }

  /**
   * Handles incoming events
   *
   * @param evt the incoming PropertyChangeEvent
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("mapUpdated")) {
      try {
        save();
      } catch (IOException ignored) {
      }
      return;
    }
    String method = evt.getPropertyName();
    Object[] args = new Object[]{evt.getNewValue()};
    try {
      new Statement(this, method, args).execute();
      save();
    } catch (Exception ignored) {
    }
  }

  private void save() throws IOException {
    FileOutputStream f = new FileOutputStream("data/profiles/" + name + ".player");
    ObjectOutput s = new ObjectOutputStream(f);
    s.writeObject(this);
  }
}
