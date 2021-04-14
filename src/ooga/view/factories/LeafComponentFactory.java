package ooga.view.factories;

import java.beans.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LeafComponentFactory extends ComponentFactory {

  private final ActionFactory af;

  public LeafComponentFactory(ActionFactory af) {
    this.af = af;
  }

  @Override
  public Object make(Element e) throws ViewFactoryException {
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
        if (childElem.getNodeName().equals("Event")) {
          addEvent(component, childElem);
        } else if (hasChildElements(childElem)) {
          addChild(currRB, component, childElem);
        } else {
          editProperty(currRB, component, childElem);
        }
      }
    }

    return component;
  }

  private Image makeImage(Element e) {
    String imagePath = e.getElementsByTagName("Path").item(0).getTextContent();
    System.out.println(imagePath);
    return new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
  }

  private void addEvent(Node component, Element e) throws ViewFactoryException {
    EventHandler<?> eh = null;
    if (e.getAttribute("event_type").equals("KeyEvent")) {
      eh = af.makeKeyEvent(component, e);
    } else {
      eh = af.makeActionEvent(component, e);
    }
    try {
      new Statement(component, e.getAttribute("method"), new Object[]{eh}).execute();
    } catch (Exception exception) {
      throw new ViewFactoryException(exception.getMessage());
    }
  }
}
