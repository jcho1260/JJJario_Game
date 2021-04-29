package ooga.view.launcher;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 */
public class ExceptionView {

  /**
   * @param e
   */
  public void displayError(Exception e) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error Dialog");
    alert.setHeaderText("Runtime Error");
    alert.setContentText(e.getMessage());

    alert.showAndWait();
  }

  /**
   * @param heading
   * @param message
   */
  public void displayWarning(String heading, String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Error Dialog");
    alert.setHeaderText(heading);
    alert.setContentText(message);

    alert.showAndWait();
  }
}
