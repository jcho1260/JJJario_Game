package ooga.view.factories;

import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParentComponentFactory extends ComponentFactory {
  private final LeafComponentFactory lcf = new LeafComponentFactory();

  @Override
  public Object make(Element e) throws Exception {
    if (e.getAttribute("type").equals("Leaf")) {
      return lcf.make(e);
    }

    String compName = e.getNodeName();
    ResourceBundle currRB = ResourceBundle.getBundle("view_resources/factory_bundles/" + compName + "Keys");
    Parent parent = (Parent) makeComponentBase(currRB, compName);
    parent.setId(e.getAttribute("id"));
    parent.getStylesheets().add(e.getAttribute("style"));
    NodeList nl = e.getChildNodes();

    for (int i = 0; i < nl.getLength(); i++) {
      org.w3c.dom.Node tempNode = nl.item(i);
      if (tempNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        Element childElem = (Element) tempNode;
        if (hasChildElements(childElem)) {
          if (e.getAttribute("type").equals("Pane")){
            ((Pane) parent).getChildren().add((Node) make(childElem));
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
}
