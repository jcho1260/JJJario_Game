package ooga;


import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.view.factories.ButtonFactory;
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
            ButtonFactory bFactory = new ButtonFactory();
            NodeList nl = doc.getElementsByTagName("Button");
            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10,10,10,10));
            vbox.setSpacing(10);
            vbox.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                vbox.getChildren().add(bFactory.makeButton(el));
            }
            double sHeight = Double.parseDouble(doc.getElementsByTagName("Height").item(0).getTextContent());
            double sWidth = Double.parseDouble(doc.getElementsByTagName("Width").item(0).getTextContent());
            Scene scene = new Scene(vbox, sWidth, sHeight);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
