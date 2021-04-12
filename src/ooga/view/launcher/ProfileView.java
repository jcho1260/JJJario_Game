package ooga.view.launcher;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import ooga.controller.Controller;
import ooga.controller.Profile;

public class ProfileView {
  private final Controller controller;

  public ProfileView(Controller controller) {
    this.controller = controller;
  }

  public Parent makeMenu(Profile profile) {
    return new HBox();
  }
}
