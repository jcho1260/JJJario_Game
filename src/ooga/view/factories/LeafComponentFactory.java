package ooga.view.factories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LeafComponentFactory extends ComponentFactory {

  @Override
  public Object make(Element e) throws Exception {
    if (e.getNodeName().equals("Image")) {
      return makeImage(e);
    }

    String compName = e.getNodeName();
    ResourceBundle currRB = ResourceBundle.getBundle(
        "view_resources/factory_bundles/" + compName + "Keys");
    Node component = (Node) makeComponentBase(currRB, compName);
    component.setId(e.getAttribute("id"));
    NodeList nl = e.getChildNodes();

    for (int i = 0; i < nl.getLength(); i++) {
      org.w3c.dom.Node tempNode = nl.item(i);
      if (tempNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        Element childElem = (Element) tempNode;
        if (childElem.getNodeName().equals("Action")) {
          ((Button) component).setOnAction((EventHandler<ActionEvent>) makeAction(childElem));
        } else if (hasChildElements(childElem)) {
          addChild(currRB, component, childElem);
        } else {
          editProperty(currRB, component, childElem);
        }
      }
    }

    return component;
  }

  private Image makeImage(Element elem) {
    String imagePath = elem.getElementsByTagName("Path").item(0).getTextContent();
    return new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
  }

  private EventHandler<ActionEvent> makeAction(Element e) {
    String actionType = e.getElementsByTagName("Type").item(0).getTextContent();
    if (actionType.equals("NewScreen")) {
      return makeNewScreenAction(e);
    }
    return null;
  }

  private EventHandler<ActionEvent> makeNewScreenAction(Element e) {
    return event -> {
      RootFactory rf = new RootFactory();
      String filePath = e.getElementsByTagName("Path").item(0).getTextContent();
      try {
        Pane root = rf.make(filePath);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    };
  }
}
