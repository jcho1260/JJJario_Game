package ooga.view.launcher;

import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import ooga.controller.Controller;
import ooga.model.util.Vector;
import ooga.view.factories.ParentComponentFactory;
import ooga.view.factories.SceneFactory;
import ooga.view.factories.ViewFactoryException;
import ooga.view.game.Sprite;
import org.w3c.dom.Element;

public class BuilderView {

  private final ParentComponentFactory pcf;
  private final Controller controller;
  private String game;
  private String levelName;
  private Vector frameSize;
  private Vector levelSize;
  private Stage builderStage;
  private final String colors;
  private Group builderGroup;

  public BuilderView(Controller controller, ParentComponentFactory pcf, String colors) {
    this.controller = controller;
    this.pcf = pcf;
    this.colors = colors;
  }

  public void startBuilder(Element e, String game, String levelName, Vector frameSize,
      Vector levelSize)
      throws ViewFactoryException {
    this.game = game;
    this.frameSize = frameSize;
    this.levelSize = levelSize;
    this.levelName = levelName;

    controller.startGameMaker(game, this);

    builderGroup = new Group();
    builderGroup.setId("BuilderGroup");

    ScrollPane sp = (ScrollPane) pcf.make(e);
    Pane p = (Pane) sp.getContent();
    p.setPrefHeight(levelSize.getY());
    p.setPrefWidth(levelSize.getX());
    sp.setPrefViewportHeight(frameSize.getY());
    sp.setPrefViewportWidth(frameSize.getX());
    sp.setContextMenu(makeObjectTypeMenu());
    p.getChildren().add(builderGroup);

    builderStage = new Stage();
    builderStage.setTitle("Stage Builder");
    Scene temp = new Scene(sp);
    builderStage.setScene(temp);
    builderStage.show();
  }

  public void displayBuilderSprite(String imageName, Vector pos, Vector size) {
    ImageView newSprite = new Sprite(game, imageName, size.getX(), size.getY(), pos.getX(),
        pos.getY()).getImageView();
    newSprite.setVisible(true);
    builderGroup.getChildren().add(newSprite);
  }

  private void queryObjectInfo(String name, String type) throws ViewFactoryException {
    Scene queryScene = new SceneFactory(controller)
        .make("resources/view_resources/launcher/builder/" + type + "Maker.XML");
    queryScene.getStylesheets().add(colors);
    ResourceBundle imgKeys = ResourceBundle
        .getBundle("view_resources/game/SpriteImageKeys/" + game + "SpriteKeys");
    String imagePath = imgKeys.getString(name.toUpperCase());
    ((ImageView) queryScene.lookup("#GameObjectImage")).setImage(new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
    Button b = (Button) queryScene.lookup("#MakeObjectButton");
    b.setText(b.getText().replace(type, name));
    Text t = (Text) queryScene.lookup("#StageBuilderTitle");
    t.setText(t.getText().replace(type, name));
    Stage queryStage = new Stage();
    queryStage.setTitle(name + " Maker");
    queryStage.setScene(queryScene);
    queryStage.show();
  }

  private ContextMenu makeObjectTypeMenu() {
    List<Pair<String, String>> objectTypes = controller.getAllGameObjectsForMaker();
    ContextMenu contextMenu = new ContextMenu();
    MenuItem buildItem = new MenuItem("Build");
    buildItem.setOnAction(event -> buildCustomStage());
    contextMenu.getItems().add(buildItem);
    for (Pair<String, String> type : objectTypes) {
      MenuItem mi = new MenuItem(type.getKey());
      mi.setOnAction(event -> {
        try {
          if (type.getValue().equals("Player")) {
            setPlayer();
          } else {
            queryObjectInfo(type.getKey(), type.getValue());
          }
        } catch (ViewFactoryException e) {
          new ExceptionView().displayError(e);
        }
      });
      contextMenu.getItems().add(mi);
    }
    return contextMenu;
  }

  private void setPlayer() throws ViewFactoryException {
    Scene queryScene = new SceneFactory(controller)
        .make("resources/view_resources/launcher/builder/PlayerMaker.XML");
    queryScene.getStylesheets().add(colors);
    Stage queryStage = new Stage();
    queryStage.setTitle("Player Maker");
    queryStage.setScene(queryScene);
    queryStage.show();
  }

  private void buildCustomStage() {
    controller.saveGameMaker(game, levelName, frameSize, 60, new Vector(0, 0), levelSize);
  }
}
