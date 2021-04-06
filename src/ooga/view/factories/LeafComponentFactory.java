package ooga.view.factories;

import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.Node;
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
          ((Button) component).setOnAction(makeAction(childElem, component));
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

  private EventHandler<ActionEvent> makeAction(Element e, Node n) {
    String actionType = e.getElementsByTagName("Type").item(0).getTextContent();
    if (actionType.equals("NewScreen")) {
      return makeNewScreenAction(e);
    }
    return null;
  }

  private EventHandler<ActionEvent> makeNewScreenAction(Element e) {
    return event -> {
      SceneFactory sf = new SceneFactory();
      String filePath = e.getElementsByTagName("Path").item(0).getTextContent();
      try {
        Scene scene = sf.make(filePath);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    };
  }

  /*private EventHandler<ActionEvent> makeChangeNodeAction(Element e, Node n) {
    return event -> {
      String parentName = e.getElementsByTagName("ParentNode").item(0).getTextContent();
      String oldName = e.getElementsByTagName("OldNode").item(0).getTextContent();
      Pane parentPane = getParentPane(n, parentName);
      ParentComponentFactory pcf = new ParentComponentFactory();
      String filePath = e.getElementsByTagName("NewNode").item(0).getTextContent();
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      Document doc = null;
      try {
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(new File(filePath));
        doc.getDocumentElement().normalize();
      } catch (ParserConfigurationException | SAXException | IOException parserConfigurationException) {
        parserConfigurationException.printStackTrace();
      }
      assert doc != null;
      try {
        Node newNode = (Node) pcf.make(doc.getDocumentElement());
        parentPane.getChildren().remove(parentPane.lookup(oldName));
        assert parentPane.getScene().lookup(oldName) == null;
        System.out.println("Adding "+newNode.getId()+" to "+parentPane.getId());
        parentPane.getChildren().add(newNode);
        parentPane.lookup(oldName);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    };
  }

  private Pane getParentPane(Node n, String pName) {
    Node tempNode = n;
    while (tempNode != null && !tempNode.getId().equals(pName)) {
      tempNode = tempNode.getParent();
    }
    return (Pane) tempNode;
  }*/
}
