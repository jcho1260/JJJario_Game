package ooga.view.launcher;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExceptionView {

  public void displayError(Exception e) {
    e.printStackTrace();
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error Dialog");
    alert.setHeaderText("Runtime Error");
    alert.setContentText(e.getMessage());

    alert.showAndWait();
  }

  public void displayWarning(String heading, String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Error Dialog");
    alert.setHeaderText(heading);
    alert.setContentText(message);

    alert.showAndWait();
  }
}
