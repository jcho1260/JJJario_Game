package ooga.view.factories;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import ooga.controller.Controller;
import ooga.controller.GameObjectMaker;
import ooga.controller.KeyListener;
import ooga.controller.Profile;
import ooga.model.util.Vector;
import ooga.view.game.GameView;
import ooga.view.launcher.BuilderView;
import ooga.view.launcher.ExceptionView;
import ooga.view.launcher.ProfileView;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class HandlerFactory {

  private final Controller controller;
  private final ParentComponentFactory pcf = new ParentComponentFactory(this);

  public HandlerFactory(Controller controller) {
    this.controller = controller;
  }

  public EventHandler<ActionEvent> makeActionEventHandler(Node component, Element e) {
    String eventType = e.getElementsByTagName("Type").item(0).getTextContent();
    return event -> {
      try {
        Method m = HandlerFactory.class.getDeclaredMethod(eventType, Node.class, Element.class);
        m.invoke(this, component, e);
      } catch (Exception exception) {
        exception.printStackTrace();
        new ExceptionView().displayError(exception);
      }
    };
  }

  public EventHandler<KeyEvent> makeKeyEventHandler(Node component, Element e) {
    String eventType = e.getElementsByTagName("Type").item(0).getTextContent();
    return event -> {
      try {
        Method m = HandlerFactory.class
            .getDeclaredMethod(eventType, Node.class, Element.class, KeyEvent.class);
        m.invoke(this, component, e, event);
      } catch (Exception exception) {
        new ExceptionView().displayError(exception);
      }
    };
  }

  private void launchGame(Node component, Element e) {
    Stage newStage = new Stage();
    String filePath = e.getElementsByTagName("Path").item(0).getTextContent();
    String gameName = e.getElementsByTagName("Game").item(0).getTextContent();
    KeyListener kl = controller.getKeyListener();
    controller.setCurrGame(gameName);
    GameView gv = new GameView(gameName, newStage, kl, controller,
        component.getScene().getStylesheets().get(0));
    controller.startGame(gv);
    gv.start(filePath);
  }

  private void goToMenu(Node component, Element e) {
    controller.displayMenu();
  }

  private void restartLevel(Node component, Element e) {
    controller.restartLevel();
  }

  private void nextLevel(Node component, Element e) {
    controller.nextLevel();
  }

  private void changeStack(Node component, Element e) {
    try {
      changeStackPane(component, e,
          (Node) pcf.make((Element) e.getElementsByTagName("FilePath").item(0)));
    } catch (Exception exception) {
      new ExceptionView().displayError(exception);
    }
  }

  private void profileView(Node component, Element e) {
    String pfName = controller.getActiveProfile();
    try {
      if (pfName.equals("")) {
        changeStackPane(component, e,
            (Node) pcf.make((Element) e.getElementsByTagName("FilePath").item(0)));
      } else {
        makeAndDisplayProfile(component, e, pfName);
      }
    } catch (ViewFactoryException vfe) {
      new ExceptionView().displayError(vfe);
    }
  }

  private void makeAndDisplayProfile(Node component, Element e, String pfName) {
    Profile profile = controller.getProfile(pfName);
    ProfileView pv = new ProfileView(controller, pcf, profile);
    profile.display(pv);
    changeStackPane(component, e, pv.getParent());
  }

  private void profileLogin(Node component, Element e, KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      try {
        String pfName = ((TextField) component).getText();
        if (pfName.length() == 0) {
          throw new ViewFactoryException("Please provide a non-empty username!");
        }
        controller.setActiveProfile(pfName);
        makeAndDisplayProfile(component, e, pfName);
      } catch (ViewFactoryException | IOException exception) {
        new ExceptionView().displayError(exception);
      }
    }
  }

  private void logout(Node component, Element e) {
    try {
      controller.setActiveProfile("");
      changeStack(component, e);
    } catch (IOException ioException) {
      new ExceptionView().displayError(ioException);
    }
  }

  private void changeStackPane(Node component, Element e, Node newNode) {
    StackPane sp = (StackPane) component.getScene()
        .lookup("#" + e.getElementsByTagName("PaneID").item(0).getTextContent());
    sp.getChildren().clear();
    newNode.toFront();
    sp.getChildren().add(newNode);
  }

  private void changeTheme(Node component, Element e) {
    String value = (String) ((ChoiceBox<?>) component).getValue();
    ResourceBundle rb = ResourceBundle.getBundle("view_resources/color_themes/ColorThemes");
    component.getScene().getStylesheets().clear();
    component.getScene().getStylesheets().add(rb.getString(value.toUpperCase()));
  }

  private void saveGame(Node component, Element e) {
    controller.saveGame();
  }

  private void levelLibrary(Node component, Element e) {
    try {
      int numLevels = controller.getNumLevels();
      ScrollPane sp = (ScrollPane) pcf.make((Element) e.getElementsByTagName("FilePath").item(0));
      for (int i = 0; i < numLevels; i++) {
        String levelName = controller.getLevelName(i);
        Button b = (Button) pcf.make((Element) e.getElementsByTagName("Button").item(0));
        b.setText(levelName.replaceAll("([A-Za-z])(\\d)", "$1 $2"));
        b.setId(levelName + "Button");
        int finalI = i;
        b.setOnAction(event -> controller.startLevel(finalI));
        ((Pane) sp.getContent()).getChildren().add(b);
      }
      String[] userDefined = controller.getUserDefinedLevels();
      for (String levelName : userDefined) {
        Button b = (Button) pcf.make((Element) e.getElementsByTagName("Button").item(0));
        b.setText(levelName);
        b.setId(levelName + "Button");
        b.setOnAction(event -> controller.loadUserDefinedName(levelName));
        ((Pane) sp.getContent()).getChildren().add(b);
      }
      changeStackPane(component, e, sp);
    } catch (ViewFactoryException vfe) {
      new ExceptionView().displayError(vfe);
    }
  }

  private void loadLibrary(Node component, Element e) {
    try {
      Pair<String, String>[] saves = controller.getSaves();
      ScrollPane sp = (ScrollPane) pcf.make((Element) e.getElementsByTagName("FilePath").item(0));
      for (Pair<String, String> save : saves) {
        Button b = (Button) pcf.make((Element) e.getElementsByTagName("Button").item(0));
        String timestamp = new SimpleDateFormat("HH:mm:ss MM-dd-yyyy")
            .format(new SimpleDateFormat("MM-dd-yyyy_HH_mm_ss").parse(save.getValue()));
        b.setText(save.getKey().replaceAll("([A-Za-z])(\\d)", "$1 $2") + "\t" + timestamp);
        b.setOnAction(event -> controller.loadGame(save.getKey(), save.getValue()));
        ((Pane) sp.getContent()).getChildren().add(b);
      }
      changeStackPane(component, e, sp);
    } catch (ViewFactoryException | ParseException exception) {
      new ExceptionView().displayError(exception);
    }
  }

  private void loadGame(Node component, Element e) {
    String[] decoded = component.getId().split("_");
    controller.loadGame(decoded[1], decoded[2]);
  }

  private void startBuilder(Node component, Element e) {
    try {
      Node p = component.getParent();
      Vector frameSize = new Vector(
          doubleFromTextField(p, "#ViewWidthInput"),
          doubleFromTextField(p, "#ViewHeightInput"));
      Vector levelSize = new Vector(
          doubleFromTextField(p, "#LevelWidthInput"),
          doubleFromTextField(p, "#LevelHeightInput"));
      new BuilderView(controller, pcf, component.getScene().getStylesheets().get(0)).startBuilder(
          new Stage(),
          (Element) e.getElementsByTagName("FilePath").item(0),
          stringFromTextField(p, "#GameNameInput"),
          stringFromTextField(p, "#LevelNameInput"),
          frameSize,
          levelSize);
    } catch (ViewFactoryException vfe) {
      new ExceptionView().displayError(vfe);
    }
  }

  private String stringFromTextField(Node n, String id) throws ViewFactoryException {
    if (((TextField) n.lookup(id)).getText() == null) {
      throw new ViewFactoryException("Please provide proper input for " + id);
    }
    return ((TextField) n.lookup(id)).getText();
  }

  private int intFromTextField(Node n, String id) throws ViewFactoryException {
    try {
      return Integer.parseInt(((TextField) n.lookup(id)).getText());
    } catch (Exception e) {
      throw new ViewFactoryException("Please provide proper input for " + id);
    }
  }

  private double doubleFromTextField(Node n, String id) throws ViewFactoryException {
    try {
      return Double.parseDouble(((TextField) n.lookup(id)).getText());
    } catch (Exception e) {
      throw new ViewFactoryException("Please provide proper input for " + id);
    }
  }

  private Vector vectorFromTextField(Node n, String id) throws ViewFactoryException {
    String[] arr = ((TextField) n.lookup(id)).getText().split(",");
    try {
      return new Vector(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]));
    } catch (Exception e) {
      throw new ViewFactoryException("Please provide proper input for " + id);
    }
  }

  private void makeObject(Node component, Element e) {
    String objName = ((Button) component).getText()
        .substring(5, ((Button) component).getText().length() - 1);
    ArrayList<Object> objList = new ArrayList<>();
    objList.add(controller.getEntityTypes(objName));
    NodeList argNodes = e.getElementsByTagName("Arg");
    Vector pos = null;
    Vector size = null;
    for (int i = 0; i < argNodes.getLength(); i++) {
      if (argNodes.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        Element argElem = (Element) argNodes.item(i);
        String name = argElem.getElementsByTagName("Name").item(0).getTextContent();
        if (name.equals("ID")) {
          objList.add(controller.getNumGameMakers());
          continue;
        }
        String argClass = argElem.getElementsByTagName("Class").item(0).getTextContent();
        try {
          Method m = HandlerFactory.class
              .getDeclaredMethod(argClass.toLowerCase() + "FromTextField", Node.class,
                  String.class);
          Object o = m.invoke(this, component.getScene().lookup("#ObjectMakerPane"),
              "#" + name + "Input");
          if (name.equals("Position")) {
            pos = (Vector) o;
          }
          if (name.equals("Size")) {
            size = (Vector) o;
          }
          objList.add(o);
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }
    }
    objList.add(true);
    controller.addObjectToGameMaker(
        new GameObjectMaker(
            "ooga.model.gameobjects." + e.getElementsByTagName("ObjectType").item(0)
                .getTextContent(),
            objList.toArray()));
    controller.displayBuilderSprite(objName, pos, size);
    ((Stage) component.getScene().getWindow()).close();
  }

  private void makePlayer(Node component, Element e) {
    try {
      Vector pos = vectorFromTextField(
          component.getScene().lookup("#ObjectMakerPane"),
          "#PositionInput");
      Vector size = vectorFromTextField(
          component.getScene().lookup("#ObjectMakerPane"),
          "#SizeInput");
      controller.setGameMakerPlayer(pos, size);
      controller.displayBuilderSprite("Player", pos, size);
      ((Stage) component.getScene().getWindow()).close();
    } catch (ViewFactoryException vfe) {
      new ExceptionView().displayError(vfe);
    }
  }
}
