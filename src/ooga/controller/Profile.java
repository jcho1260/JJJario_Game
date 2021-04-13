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
import ooga.model.util.Action;

public class Profile implements Serializable, PropertyChangeListener {

  private String name;
  private String picture;
  private Map<KeyCode, Action> keybinds;
  private Map<String, Map<Integer, Integer>> highScores;

  public Profile(String name) {
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPicture(String f) {
    this.picture = picture;
  }

  public void setKeybinds(Map<KeyCode, Action> keybinds) {
    this.keybinds = keybinds;
  }

  public Map<KeyCode, Action> getKeybinds() {
    return keybinds;
  }

  public void display(ProfileView pv) {

  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String method = evt.getPropertyName();
    Object[] args = new Object[]{evt.getNewValue()};
    try {
      new Statement(this, method, args).execute();
      FileOutputStream f = new FileOutputStream("data/profiles/" + name + ".player");
      ObjectOutput s = new ObjectOutputStream(f);
      s.writeObject(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
