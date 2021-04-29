package ooga.view.factories;

import java.io.File;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class provides the functionality to generate javafx node objects from properly formatted
 * XML data files to be used to interpret user input for use in the application. This class will
 * fail if the XML files are not properly formatted.
 *
 * @author Adam Hufstetler
 */
public class ParentComponentFactory extends ComponentFactory {

  private final LeafComponentFactory lcf;

  /**
   * Constructs a ParentComponentFactory with the given handler factory to make a Leaf Factory.
   *
   * @param af the HandlerFactory through which user event handlers are made in the Leaf Factory
   */
  public ParentComponentFactory(HandlerFactory af) {
    lcf = new LeafComponentFactory(af);
  }

  /**
   * Constructs a javafx node from a given element. It is assumed there is a matching ResourceBundle
   * for the given javafx component in the directory resources/view_resources/factory_bundles. It
   * is also assumed all elements have an id attribute and style attribute with a valid css file.
   *
   * @param e element which contains the requisite information to construct the expected object
   * @return an object constructed by parsing the attributes and child elements of e
   * @throws ViewFactoryException if the element is not properly formatted or contains requests that
   *                              are unsupported
   */
  @Override
  public Object make(Element e) throws ViewFactoryException {
    if (e.getAttribute("type").equals("Leaf")) {
      return lcf.make(e);
    } else if (e.getNodeName().equals("FilePath")) {
      return makeFile(e);
    }

    String compName = e.getNodeName();
    ResourceBundle currRB = ResourceBundle
        .getBundle("view_resources/factory_bundles/" + compName + "Keys");
    Parent parent = (Parent) makeComponentBase(currRB, compName);
    parent.setId(e.getAttribute("id"));
    parent.getStylesheets().add(e.getAttribute("style"));
    NodeList nl = e.getChildNodes();

    for (int i = 0; i < nl.getLength(); i++) {
      org.w3c.dom.Node tempNode = nl.item(i);
      if (tempNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        Element childElem = (Element) tempNode;
        if (childElem.getNodeName().equals("Placeholder")) {
          continue;
        }
        if (hasChildElements(childElem)) {
          if (e.getAttribute("type").equals("Pane")) {
            Node child = (Node) make(childElem);
            ((Pane) parent).getChildren().add(child);
          } else {
            addChild(currRB, parent, childElem);
          }
        } else {
          editProperty(currRB, parent, childElem);
        }
      }
    }

    return parent;
  }

  private Object makeFile(Element e) throws ViewFactoryException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      doc = dBuilder.parse(new File(e.getElementsByTagName("Path").item(0).getTextContent()));
    } catch (Exception exception) {
      throw new ViewFactoryException(exception.getMessage());
    }
    doc.getDocumentElement().normalize();
    return make(doc.getDocumentElement());
  }
}
