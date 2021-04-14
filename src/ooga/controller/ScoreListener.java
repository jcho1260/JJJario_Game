package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ScoreListener implements PropertyChangeListener {

    int score;

    public int getScore() {
        return score;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("score")) {
            score = (int) evt.getNewValue();
        }
    }
}
