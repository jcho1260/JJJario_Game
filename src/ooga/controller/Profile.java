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

public class Profile implements Serializable, PropertyChangeListener {

  private final Map<KeyCode, Action> keybinds;
  private String name;
  private String picture;
  private final Map<String, Map<String, Integer>> highScores;

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

  public Map<String, Map<String, Integer>> getHighScores() {
    return highScores;
  }

  public void setUsername(String name) {
    this.name = name;
  }

  public void setPicture(String f) {
    this.picture = f;
  }

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

  public Map<KeyCode, Action> getKeybinds() {
    return keybinds;
  }

  public void display(ProfileView pv) {
    System.out.println("name: " + name);
    System.out.println("hs: " + highScores);
    pv.makeMenu(name, picture, keybinds, highScores);
  }

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
