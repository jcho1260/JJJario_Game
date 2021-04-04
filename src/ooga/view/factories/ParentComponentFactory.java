package ooga.view.factories;

import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParentComponentFactory extends ComponentFactory {

  @Override
  public Object make(Element e) throws Exception {
    LeafComponentFactory lcf = new LeafComponentFactory();

    String compName = e.getNodeName();
    ResourceBundle currRB = ResourceBundle.getBundle("view/factory_bundles/" + compName + "Keys");
    Pane parent = (Pane) makeComponentBase(currRB, compName);
    parent.setId(e.getAttribute("id"));
    parent.getStylesheets().add(e.getAttribute("style"));
    NodeList nl = e.getChildNodes();

    for (int i = 0; i < nl.getLength(); i++) {
      org.w3c.dom.Node tempNode = nl.item(i);
      if (tempNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        Element childElem = (Element) tempNode;
        if (hasChildElements(childElem)) {
          parent.getChildren().add((Node) lcf.make(childElem));
        } else {
          editProperty(currRB, parent, childElem);
        }
      }
    }

    return parent;
  }
}
