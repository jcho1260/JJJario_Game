public interface OOGAView {
    void endLevelView(Boolean outcome, Integer score, Map<Integer, Integer> highScores);

    void startLevelView(String levelType, Collection<GameObjObserver> observers);

    void setStyle(Style style);

    void reportError(String eType, Exception e);
}