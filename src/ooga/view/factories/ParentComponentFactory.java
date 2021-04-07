package ooga.view.factories;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParentComponentFactory extends ComponentFactory {
  private final LeafComponentFactory lcf;

  public ParentComponentFactory(ActionFactory af) {
    lcf = new LeafComponentFactory(af);
  }

  @Override
  public Object make(Element e) throws Exception {
    if (e.getAttribute("type").equals("Leaf")) {
      return lcf.make(e);
    } else if (e.getAttribute("type").equals("FilePath")) {
      return makeFile(e);
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

  private Object makeFile(Element e) throws Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new File(e.getTextContent()));
    doc.getDocumentElement().normalize();

    return make(doc.getDocumentElement());
  }
}
