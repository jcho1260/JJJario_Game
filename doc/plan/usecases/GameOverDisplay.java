class GameOverView {
  private OogaView view;
  private OOGAModel model;
  private ResourceBundle myGameBundle;
  private ResourceBundleParser myBundleParser = new ResourceBundleParser();
  private Integer myScore;

  void main() {
    Boolean levelOutcome = model.getGameResult();
    Map<Integer, Intger> highScoreMap = myBundleParser.extractHighScores(myGameBundle);

    view.endLevelView(levelOutcome, myScore, highScores);
  }
}