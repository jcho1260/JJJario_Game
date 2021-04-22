package ooga.view.launcher;

import java.util.Objects;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.model.util.Vector;
import ooga.view.factories.ParentComponentFactory;
import ooga.view.factories.SceneFactory;
import ooga.view.factories.ViewFactoryException;
import org.w3c.dom.Element;

public class BuilderView {

  private final ParentComponentFactory pcf;
  private final Controller controller;
  private String game;
  private Vector frameSize;
  private Vector levelSize;
  private Stage builderStage;

  public BuilderView(Controller controller, ParentComponentFactory pcf) {
    this.controller = controller;
    this.pcf = pcf;
  }

  public void startBuilder(Element e, String game, Vector frameSize, Vector levelSize)
      throws ViewFactoryException {
    this.game = game;
    this.frameSize = frameSize;
    this.levelSize = levelSize;

    ScrollPane sp = (ScrollPane) pcf.make(e);
    Pane p = (Pane) sp.getContent();
    p.setPrefHeight(levelSize.getY());
    p.setPrefWidth(levelSize.getX());
    sp.setPrefViewportHeight(frameSize.getY());
    sp.setPrefViewportWidth(frameSize.getX());

    builderStage = new Stage();
    Group builderGroup = new Group();
    builderGroup.setId("BuilderGroup");
    p.getChildren().add(builderGroup);

    builderStage.setTitle("Stage Builder");
    builderStage.setScene(new Scene(sp));
    builderStage.show();
  }
}
