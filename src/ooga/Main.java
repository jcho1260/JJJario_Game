package ooga;


import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import ooga.controller.CollisionsParser;
import ooga.controller.LevelParser;
import org.xml.sax.SAXException;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main {
    /**
     * A method to test (and a joke :).
     */
    public double getVersion () {
        return 0.001;
    }

    /**
     * Start of the program.
     */
    public static void main (String[] args)
        throws IOException, SAXException, ParserConfigurationException {
//        LevelParser levelParser = new LevelParser();
//        levelParser.createLevel(new File("data/testgame/level.xml"));
        CollisionsParser collisionsParser = new CollisionsParser();
        collisionsParser.parseCollisions(new File("data/testgame/collisions.xml"));
    }
}
