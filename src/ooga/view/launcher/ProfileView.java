package ooga.view.launcher;

import javafx.scene.Parent;
import ooga.controller.Controller;
import ooga.controller.Profile;
import ooga.view.factories.SceneFactory;

public class ProfileView {
  private final Controller controller;
  private final SceneFactory sf;

  public ProfileView(Controller controller) {
    this.controller = controller;
    this.sf = new SceneFactory(controller);
  }

  public Parent makeMenu(Profile profile) {
    return null;
  }
}
