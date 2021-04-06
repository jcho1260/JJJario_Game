package ooga.view.launcher;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.view.factories.RootFactory;

public class LauncherView {

  public void start(Stage primaryStage) {
    try {
      RootFactory rf = new RootFactory();
      Pane root = rf.make("resources/view_resources/launcher/Launcher.XML");
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
