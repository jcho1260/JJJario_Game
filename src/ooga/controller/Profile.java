package ooga.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;
import ooga.model.util.Action;

public record Profile(String name, String picture, Map<Action, KeyCode> keybinds, Map<String, Map<Integer, Integer>> highScores) implements
    Serializable {

  public Profile(String name) {
    this(
        name,
        "profiles/pictures/default.png",
        Map.of(Action.UP, KeyCode.W, Action.DOWN, KeyCode.S, Action.LEFT, KeyCode.A, Action.RIGHT, KeyCode.D),
        new HashMap<>()
    );
  }
}
