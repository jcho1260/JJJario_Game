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
import ooga.view.factories.ComponentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * A method to test (and a joke :).
     */
    public double getVersion () {
        return 0.001;
    }

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
            ComponentFactory bFactory = new ComponentFactory();
            NodeList nl = doc.getElementsByTagName("Scene").item(0).getChildNodes();
            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10,10,10,10));
            vbox.setSpacing(10);
            vbox.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            vbox.getStylesheets().add("view/launcher/css/SideBarButton.css");
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    vbox.getChildren().add((Node) bFactory.makeComponent(el));
                }
            }
            Scene scene = new Scene(vbox, 300, 750);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
