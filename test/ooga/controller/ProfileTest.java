package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import javafx.scene.input.KeyCode;
import ooga.model.util.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProfileTest {

  Profile profile;

  @BeforeEach
  public void init() throws IOException {
    profile = new Profile("TestProfile");
  }

  @Test
  public void testGetHighScores() {
    assertEquals(0, profile.getHighScores().size());
  }

  @Test
  public void testGetKeyBinds() {
    assertEquals(Action.DOWN, profile.getKeybinds().get(KeyCode.S));
  }

  @Test
  public void testIllegalName() {
    assertThrows(IOException.class, () -> new Profile(":::::---///.......///////,,,,"));
  }
}
