package ooga;


import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.view.factories.LeafComponentFactory;
import ooga.view.factories.ParentComponentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Feel free to completely change this code or delete it entirely.
 */
public class ViewDevMain extends Application {

  /**
   * Start of the program.
   */
  public static void main (String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new File("resources/view/launcher/SideBar.XML"));
      doc.getDocumentElement().normalize();

      ParentComponentFactory pcf = new ParentComponentFactory();
      Element rootE = (Element) doc.getElementsByTagName("VBox").item(0);
      VBox vbox = (VBox) pcf.make(rootE);
      Scene scene = new Scene(vbox, 300, 750);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
