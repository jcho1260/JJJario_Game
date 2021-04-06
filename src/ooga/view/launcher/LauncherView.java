package ooga.view.launcher;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.view.factories.SceneFactory;

public class LauncherView {

  public void start(Stage primaryStage) {
    try {
      SceneFactory sf = new SceneFactory();
      Scene scene = sf.make("resources/view_resources/launcher/Launcher.XML");
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
