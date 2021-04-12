package ooga.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;
import ooga.model.util.Action;

public record Profile(String name, String picture, Map<KeyCode, Action> keybinds, Map<String, Map<Integer, Integer>> highScores) implements
    Serializable {

  public Profile(String name) {
    this(
        name,
        "profiles/pictures/default.png",
        Map.of(KeyCode.W, Action.UP, KeyCode.S, Action.DOWN, KeyCode.A, Action.LEFT, KeyCode.D, Action.RIGHT),
        new HashMap<>()
    );
  }
}
