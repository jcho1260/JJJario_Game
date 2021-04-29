package ooga.view.factories;

import java.beans.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class provides the functionality to generate javafx node objects from properly formatted
 * XML data files to be used to interpret user input for use in the application. This class will
 * fail if the XML files are not properly formatted.
 *
 * @author Adam Hufstetler
 */
public class LeafComponentFactory extends ComponentFactory {

  private final HandlerFactory af;

  /**
   * Constructs a Leaf Component Factory with the given handler factory.
   *
   * @param af the HandlerFactory through which user event handlers are made
   */
  public LeafComponentFactory(HandlerFactory af) {
    this.af = af;
  }

  /**
   * Constructs a javafx node that does not have the ability of having children added to it via the
   * getChildren().add() method, for example a Button or ImageView. It is assumed there is a
   * matching ResourceBundle for the given javafx component in the directory
   * resources/view_resources/factory_bundles. It is also assumed all elements have an id attribute.
   *
   * @param e element which contains the requisite information to construct the expected object
   * @return an object constructed by parsing the attributes and child elements of e
   * @throws ViewFactoryException if the element is not properly formatted or contains requests that
   *                              are unsupported
   */
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
    return new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
  }

  private void addEvent(Node component, Element e) throws ViewFactoryException {
    EventHandler<?> eh;
    if (e.getAttribute("event_type").equals("KeyEvent")) {
      eh = af.makeKeyEventHandler(component, e);
    } else {
      eh = af.makeActionEventHandler(component, e);
    }
    try {
      new Statement(component, e.getAttribute("method"), new Object[]{eh}).execute();
    } catch (Exception exception) {
      throw new ViewFactoryException(exception.getMessage());
    }
  }
}
