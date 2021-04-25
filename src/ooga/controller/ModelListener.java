package ooga.controller;

import ooga.model.util.Vector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ModelListener implements PropertyChangeListener {

    double score;
    double health;
    double lives;
    Vector creatableLocation;
    Controller controller;

    public double getScore() {
        return score;
    }

    public double getHealth() { return health; }

    public double getLives() { return  lives; }

    public Vector getCreatableLocation() {
        return  creatableLocation;
    }

    public void addController(Controller controller) {
        this.controller = controller;
    }

    public void reset() {
        score = 0;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("score")) {
            score = (double) evt.getNewValue();
        }
        if (evt.getPropertyName().equals("newMovingDestroyable")) {
            if (controller != null) {
                creatableLocation = (Vector) evt.getNewValue();
                controller.addCreatable(creatableLocation);
            }
        }
        if (evt.getPropertyName().equals("changeHealth")) {
            health = (double) evt.getNewValue();
        }
        if (evt.getPropertyName().equals("changeLife")) {
            lives = (double) evt.getNewValue();
        }
    }
}
