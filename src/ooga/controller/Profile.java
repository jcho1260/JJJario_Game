package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Statement;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;
import ooga.model.util.Action;
import ooga.view.launcher.ProfileView;

public class Profile implements Serializable, PropertyChangeListener {

  private String name;
  private String picture;
  private Map<KeyCode, Action> keybinds;
  private Map<String, Map<Integer, Integer>> highScores;

  public Profile(String name) {
    this.name = name;
    this.keybinds = Map.of(KeyCode.W, Action.UP, KeyCode.A, Action.LEFT, KeyCode.S, Action.DOWN, KeyCode.D, Action.RIGHT);
    save();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPicture(String f) {
    this.picture = picture;
  }

  public void setKeyBind(Pair<KeyCode, String> bind) {
    Action action = Action.valueOf(bind.getValue());
    keybinds.entrySet().removeIf(entry -> action.equals(entry.getValue()));
    keybinds.put(bind.getKey(), action);
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
