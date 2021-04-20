package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ScoreListener implements PropertyChangeListener {

    double score;

    public double getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("score")) {
            score = (double) evt.getNewValue();
        }
    }
}
