# OOGA Sprint 2 Presentation

## Names

- Adam Hufstetler
- Noah Citron
- Jessica Yang
- Juhyoung Lee
- Jin Cho

## Application Demo
- JJJario
    - Refactoring of collision handling (Juhyoung)
        - implemented hitbox checking
            - Adam: hit a side wall
                - show it can't move anymore
            - Adam: hit a goomba from the top
                - show that it changes health of the goomba (aka kills it) and also increments the score of the player (top left corner)
            - Adam: just walk around
                - shows that the ground collisions happen properly with gravity contantly being applied (doesn't fall through the floor)
            - Adam: jump up and let it fall down
                - shows that it can jump up from the ground and hit the ground again (ground collision handling and detection works)
        - corner collision
            - Adam: stand over a corner and show that nothing changes
    - View port adjustments (Jin)
        - viewport follows player with bounds
            - Adam: move side to side (long strides)
                - when you move side to side, the viewport follows the player
        - die when out of screen (falls)
            - Adam: fall into the hole
                - screen limit is set in data file and the player, once it hits the y limits will die.
        - game world win when player reaches end of screen
            - Adam: restart the game, and make your way to the end of the game level (nothing happens but it will be implemented. currently it can just walk straght off)
- FlappyBird (Jessica)
    - Multijump
        - Adam: Make multiple jumps that clearly show the bird falling and re-jumping on a key press
    - Forward driving velocity
        - Adam: don't die
- Frontend (Adam)
    - Profile - image, remapped keybindings
    - Can open new games

### Model
- JJJario
    - Collisions redone and improved
    - No vertical jitter
    - Jumping changed - no longer needs to hold UP key to jump
    - View port follows player and bounds are defined by data files
    - Proper response when hitting bounds of the view port (death or next level)
- FlappyBird
    - Multiple jumps
    - Driving velocity

### Data Files
- Profile data file
    - Uses the Serializable interface so that we can save profiles using Java's ObjectOutputSteam.writeObject method
    - Stores information such as name, profile picture, and highscores

### Testing

- JUnit 1 - Jumping test for new jumping implementation (Jessica)

```java
  /**
   * Simulates a user holding the up key, then releasing, and re-pressing while in air. Should have
   * no change in behavior.
   */
  @Test
  void moveUpRelease() {
    try {
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -2)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 0)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 1)));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
```
- JUnit 2 - Test if the collisions config is properly parsed
```java
public class CollisionsParserTest {

  CollisionsParser collisionsParser;

  @BeforeEach
  public void init() {
    collisionsParser = new CollisionsParser();
  }

  @Test
  public void testCollisions() throws IOException, SAXException, ParserConfigurationException {
    Map<String, Map<String, List<MethodBundle>>> c = collisionsParser.parseCollisions(new File("data/testgame/collisions.xml"));
    double paramOne = c.get("Player").get("Enemy").get(1).getParameters()[0];
    double paramTwo = c.get("Player").get("Enemy").get(1).getParameters()[1];

    assertEquals(8, paramOne);
    assertEquals(7, paramTwo);
  }
}
```

- JUnit 3 - Testing corner collisions - Jin
```java
 @Test
  void testCornerCollision() {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(10, 20);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(0, -1);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(14, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 15);
    Vector aVel = new Vector(0, 1);
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    assertEquals(true, collisionHandling.smallCorner(a,b));
  }

```

- JUnit 4 -
```java
  /**
   * Simulates a user playing Flappy Bird. Should allow 2 jumps.
   */
  @Test
  void continuousJumping() {
    try {
      user = new Player(new ArrayList<>(), initPosition, 0, new Vector(1, 1),
          1, 1, 1, velocityMagnitude, 1, new Vector(0, 0), 2);
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }
    assertNotNull(user);
    try {
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -2)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -3)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -4)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -3)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -2)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
```

- JUnit 5 - Testing automated movement (Jessica)
```java
  /**
   * Reversed start/end NonPlayable can turn around once it passes the end position.
   */
  @Test
  void movementEndTurnReverse() {
    npcReversed.step(5, 5);
    assertTrue(npcReversed.getPredictedPosition().getX() == 0 && npcReversed.getPredictedPosition().getY() == 0);
    npcReversed.step(1, 5);
    assertFalse(npcReversed.getPredictedPosition().getX() == 0 && npcReversed.getPredictedPosition().getY() == 0);
    npcReversed.step(1, 5);
    assertFalse(npcReversed.getPredictedPosition().getX() == 1 && npcReversed.getPredictedPosition().getY() == -1);
  }
```
- TestFX (Adam)
```java=
  @Test
  void ProfileLoginTest () {
    try {
      controller.setActiveProfile("");
    } catch (IOException e) {
      e.printStackTrace();
    }
    Button pfButton = lookup("#ProfileButton").query();
    assertNotNull(pfButton);
    clickOn(pfButton);
    TextField tf = lookup("#UsernameInputBox").query();
    assertNotNull(tf);
    clickOn(tf);
    type(KeyCode.A, KeyCode.D, KeyCode.A, KeyCode.M, KeyCode.ENTER);
    assertNotNull(lookup("#ProfileMenuVBox1").query());
  }

  @Test
  void ProfileEditTest () {
    ProfileLoginTest();
    TextField upMenu = lookup("#UPMenuInput").query();
    assertNotNull(upMenu);
    clickOn(upMenu);
    type(KeyCode.Q);
    assertEquals("Q", upMenu.getPromptText());
  }
```

## Sprint 1 Recap
- Accomplished:
    - profiles and high scores
    - color themes
    - collisions and gravity fixed
    - flappy bird added
- Difficulties:
    - rehaul collisions
    - profile view
- Significant Event
    - teammates killed a midterm
    - vaccine wiped out a teammate for a bit
- Communication
    - doing great - using slack and zoom
- Teamwork Improvement
    - are better at communicating what we need from others
- Next Sprint
    - Outstanding tasks
        - Model: implement scoring for flappy bird
        - View: add powerups and lives to the HUD
        - Controller: get highscores working properly
    - Future tasks
        - Model: more levels, third game, powerups
        - View: Game Stage Builder, load and save games
        - Controller: Mario Maker, Save/Load games, Handling multiple levels per game. Disabling key actions
    - Concerns
        - Model: refactoring
        - View: file path and resource bundle handling