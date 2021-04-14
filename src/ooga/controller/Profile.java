package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Statement;
import java.io.FileOutputStream;
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

  private String name;
  private String picture;
  private final Map<KeyCode, Action> keybinds;
  private Map<String, Map<Integer, Integer>> highScores;

  public Profile(String name) {
    this.name = name;
    this.keybinds = new HashMap<>();
    keybinds.put(KeyCode.W, Action.UP);
    keybinds.put(KeyCode.A, Action.LEFT);
    keybinds.put(KeyCode.S, Action.DOWN);
    keybinds.put(KeyCode.D, Action.RIGHT);
    save();
  }

  public void setUsername(String name) {
    this.name = name;
  }

  public void setPicture(String f) {
    this.picture = picture;
  }

  public void setKeyBind(Pair<KeyCode, String> bind) {
    Action action = Action.valueOf(bind.getValue());
    for (KeyCode key : keybinds.keySet()) {
      if (keybinds.get(key) == action) {
        keybinds.remove(key);
      }
    }
    keybinds.put(bind.getKey(), action);
    System.out.println(keybinds);
  }

  public Map<KeyCode, Action> getKeybinds() {
    return keybinds;
  }

  public void display(ProfileView pv) {
    pv.makeMenu(name, picture, keybinds, highScores);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String method = evt.getPropertyName();
    Object[] args = new Object[]{evt.getNewValue()};
    try {
      new Statement(this, method, args).execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
    save();
  }

  private void save() {
    try {
      FileOutputStream f = new FileOutputStream("data/profiles/" + name + ".player");
      ObjectOutput s = new ObjectOutputStream(f);
      s.writeObject(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
