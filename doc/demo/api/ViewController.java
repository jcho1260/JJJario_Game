public class Controller {

  /*
  Display Initialization
   */
  public int getNumLevels();

  public String getLevelName(int n);

  public String[] getUserDefinedLevels();

  /*
  Starting the Game
   */
  public void startGame(GameView gameView);

  public void startLevel(int n);

  public void setCurrGame(String game);

  /*
  GameMaker
   */
  public void startGameMaker(String game, BuilderView builderView);

  public void undoGameMaker();

  public void addObjectToGameMaker(GameObjectMaker gom);

  public void setGameMakerPlayer(Vector pos, Vector size);

  public List<String> getEntityTypes(String name);

  public void saveGameMaker(String gameName, String levelName, Vector frameSize, double frameRate,
      Vector minScreen, Vector maxScreen);

  public void loadUserDefinedName(String name);

  public int getNumGameMakers();

  public List<Pair<String, String>> getAllGameObjectsForMaker();

  public void displayBuilderSprite(String imageName, Vector pos, Vector size);

  /**
   Saving and Loading Games
   */

  public void saveGame();

  public void loadGame(String level, String dateString);

  public Pair<String, String>[] getSaves();

  /**
   Game Management
   */
  public void nextLevel();

  public void restartLevel();

  public void togglePaused();

  public void endGame();

  public void addCreatable(Vector pos);

  public void displayMenu();

  public KeyListener getKeyListener();

  /**
   Profiles
   */

  public Profile getProfile(String name);

  public String getActiveProfile();

  public void setActiveProfile(String name);

}

public class GameView implements PropertyChangeListener {

  public void start(String filePath);

  public void displayMenu();

  public void initializeLevel(double w, double h, String imagePath);

  public void startLevel();

  public void gameOver();

  public void gameWin();

  public String getGameName();

}

public class BuilderView {

  public void startBuilder(Stage stage, Element e, String game, String levelName, Vector frameSize,
      Vector levelSize);

  public void displayBuilderSprite(String imageName, Vector pos, Vector size);

}

public class ProfileView {

  public void makeMenu(String name, String imagePath, Map<KeyCode, Action> keyCodeActionMap,
      Map<String, Map<String, Integer>> highScoresMap);

}


