# OOGA Sprint 1 Presentation

## Names

- Adam Hufstetler
- Noah Citron
- Jessica Yang
- Juhyoung Lee
- Jin Cho

## Application Demo

- Run `ViewDevMain`
    - Explanation of Launcher Menu (Adam)
- Click on `JJJario` Button
    - Explanation of Game Menu (Adam)
- Click on `Level1` Button
    - Explanation of Level Screen (Adam)
    - Explanation of Noah's beautiful parsing (Noah)

### Model
- Move Player around (L and R) (Jin)
    - A/D affects player velocity
    - gravity pushes down but ground collision pushes up
- Move Player jump and Automated Movement (Jessica)
    - automated movement checks position and flips velocity if at either end point
    - jumping
        - internal counter for air time
        - resets upon ground collision
- Move Player into the Enemy (Jin)
    - collision detected
    - entity tag checks return player:enemy collision
    - calls incrementHealth on player
    - (top/down collision hurts enemy)
    - checks if player is alive

### Data Files

- Data File Type 1
    - Example Format: level.xml
    - Explanation
    - Presenter:  Noah
    ```xml
    <Level>
  <GlobalGravity>10</GlobalGravity>
  <GameObjects>
    <GameObject>
      <Name>Mario</Name>
      <Tags>
        <Tag>GameObject</Tag>
        <Tag>MovingDestroyable</Tag>
        <Tag>Player</Tag>
      </Tags>
      <Type>Player</Type>
      <Gravity>1</Gravity>
      <Size>
        <SizeX>1</SizeX>
        <SizeY>1</SizeY>
      </Size>
    </GameObject>
    <GameObject>
      <Name>Goomba</Name>
      <Tags>
        <Tag>GameObject</Tag>
        <Tag>MovingDestroyable</Tag>
        <Tag>Enemy</Tag>
        <Tag>Goomba</Tag>
      </Tags>
      <Type>MovingDestroyable</Type>
      <Gravity>1</Gravity>
      <Size>
        <SizeX>1</SizeX>
        <SizeY>1</SizeY>
      </Size>
    </GameObject>
    <GameObject>
      <Name>Block</Name>
      <Tags>
        <Tag>GameObject</Tag>
        <Tag>Block</Tag>
      </Tags>
      <Type>GameObject</Type>
      <Gravity>1</Gravity>
      <Size>
        <SizeX>2</SizeX>
        <SizeY>2</SizeY>
      </Size>
    </GameObject>
  </GameObjects>

  <Layout>
    <Entity>
      <Name>Mario</Name>
      <Location>
        <x>5</x>
        <y>13</y>
      </Location>
      <Velocity>
        <x>1</x>
        <y>1</y>
      </Velocity>
      <StartLife>3</StartLife>
      <JumpTime>120</JumpTime>
      <StartHealth>1</StartHealth>
    </Entity>
    <Entity>
      <Name>Goomba</Name>
      <Location>
        <x>15</x>
        <y>13</y>
      </Location>
      <Velocity>
        <x>-1</x>
        <y>0</y>
      </Velocity>
      <StartHealth>1</StartHealth>
      <StartLife>1</StartLife>
      <FinalLocation>
        <x>12</x>
        <y>13</y>
      </FinalLocation>
    </Entity>
    <Entity>
      <Name>Block</Name>
      <Location>
        <x>0</x>
        <y>14</y>
      </Location>
    </Entity>
  </Layout>
</Level>
```

- Data File Type 2
    - Example Format: collisions.xml
    - Explanation
    - Presenter: Noah
    ```xml
    <Collisions>
  <Actor>
    <Name>Player</Name>
    <GameObject>
      <Name>Enemy</Name>
      <Method>
        <Name>incrementHealth</Name>
        <Args>
          <Arg>-1</Arg>
        </Args>
      </Method>
    </GameObject>
    <GameObject>
      <Name>Enemy-B</Name>
      <Method>
        <Name>scaleVelocity</Name>
        <Args>
          <Arg>1</Arg>
          <Arg>-1</Arg>
        </Args>
      </Method>
    </GameObject>
    <GameObject>
      <Name>Coin</Name>
      <Method>
        <Name>incrementScore</Name>
        <Args>
          <Arg>1</Arg>
        </Args>
      </Method>
    </GameObject>
    <GameObject>
      <Name>Block</Name>
      <Method>
        <Name>generalBottomCollision</Name>
        <Args>
        </Args>
      </Method>
    </GameObject>
  </Actor>
  <Actor>
    <Name>MovingDestroyable</Name>
    <GameObject>
      <Name>MovingDestroyable</Name>
      <Method>
        <Name>scaleVelocity</Name>
        <Args>
          <Arg>-1</Arg>
          <Arg>1</Arg>
        </Args>
      </Method>
    </GameObject>
  </Actor>
</Collisions>
    ```

- User Interface File (Adam)

```xml
<?xml version = "1.0" ?>
<ScrollPane id="JJJarioLevelLibraryScrollable" type="Control" style="view_resources/game/css/JJJarioLevelLibraryScroll.css">
  <VBox id="GameLibraryVBox" type="Pane" style="view_resources/game/css/JJJarioLevelLibrary.css">
    <Height>1080</Height>
    <Button id="Level1Button" type="Leaf">
      <Text>Level 1</Text>
      <Action>
        <Type>StartLevel</Type>
        <Level>Level1</Level>
      </Action>
    </Button>
  </VBox>
</ScrollPane>
```

```xml
<?xml version = "1.0" ?>
<Scene>
  <Root>
    <HBox id="LauncherScreen" type="Parent" style="view_resources/launcher/css/LauncherScreen.css">
      <Height>810</Height>
      <Width>1440</Width>
    </HBox>
  </Root>
  <Parent>resources/view_resources/launcher/SideBar.XML</Parent>
  <Parent>resources/view_resources/launcher/GameLibrary.XML</Parent>
</Scene>
```

### Testing

- JUnit 1 - Testing same axis collisions (Jin)
    -  cover all the collision checking methods in collision handling class
        -  does not invoke any collion methods of players
    -  controlled velocity to control direction of collision
    -  position to also control direction of collision
    -  gameobjcet type specified for specific type of movement
```java=
@Test
  void testTopSameAxisCollision() throws ClassNotFoundException {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(10, 20);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(0, -1);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(10, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 15);
    Vector aVel = new Vector(0, 1);
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    List<String> expected = new ArrayList<>();
    expected.add("EnemyUP");
    assertEquals(expected, collisionHandling.determineCollisionMethods(b,a));
  }

```

- JUnit 2 - Testing different axes collisions - Jin
    - same coverage as last test, since this is also a directional collision check
    - different variables for direction and location set based on the axis i wanted to specify
```java=
void testTopDiffAxisCollision() throws ClassNotFoundException {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(10, 20);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(1, 0);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(10, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 15);
    Vector aVel = new Vector(0, 1);
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    List<String> expected = new ArrayList<>();
    expected.add("EnemyUP");
    assertEquals(expected, collisionHandling.determineCollisionMethods(b,a));
  }
```

- JUnit 3 - Testing Collisions Parser
    - Parses a test collisions file
    - Checks to ensure that collisions file creates correct collisions map object
    ```java
    @Test
  public void testCollisions() {
    Map<String, Map<String, List<MethodBundle>>> c = collisionsParser.parseCollisions(new File("data/testgame/collisions.xml"));
    double paramOne = c.get("Player").get("Enemy").get(1).getParameters()[0];
    double paramTwo = c.get("Player").get("Enemy").get(1).getParameters()[1];

    assertEquals(8, paramOne);
    assertEquals(7, paramTwo);
  }
    ```
    - Presenter: Noah
- JUnit 4 - Testing Automated Movement (Jessica)
    - Simulates clock cycles where moving GameObjects have automated movement
    - Checks to make sure that it can successfully turn around once reaching the end of its path
```java
/**
* NonPlayable can turn around once it passes the end position.
*/
@Test
void movementEndTurn() {
npc.step(5, 5);
assertTrue(npc.getPosition().getX() == 5 && npc.getPosition().getY() == -5);
npc.step(1, 5);
npc.step(1, 5);
assertFalse(npc.getPosition().getX() == 5 && npc.getPosition().getY() == -5);
npc.step(1, 5);
assertFalse(npc.getPosition().getX() == 4 && npc.getPosition().getY() == -4);
}
```
- JUnit 5 - Testing Player movement for jumping (Jessica)
    - Simulates user keypress that holds the UP key
    - Checks to make sure that a user can't indefinitely jump, and jumping ability is restore upon hitting the ground
    ``` java
      /**
       * Simulates a user holding the up key, then falling and hitting a surface, and jumping again.
       */
  @Test
  void moveUpFallUpAgain() {
    try {
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPosition().equals(new Vector(0, -2)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPosition().equals(new Vector(0, 0)));
      user.generalBottomCollision();
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPosition().equals(new Vector(0, -2)));
    } catch (Exception e) {
      assert false;
    }
  }
    ```
- JJJarioGameButton (Adam)
    - covers if the user can click on the JJJario button in the game library and open up the JJJario Game Menu Window
```java
@Test
void JJJarioGameButton () {
  assertNotNull(lookup("#Game1Button").query());
  Button game1Button = lookup("#Game1Button").query();
  clickOn(game1Button);
  assertNotNull(lookup("#SuperMarioMenu").query());
}
```
- JJJarioLevel1Button (Adam)
    - covers if the user can click on the Level 1 button in the JJJario level library and change the window to a level 1 window
```java
@Test
void JJJarioLevel1Button () {
  JJJarioGameButton();
  assertNotNull(lookup("#Level1Button").query());
  Button level1Button = lookup("#Level1Button").query();
  clickOn(level1Button);
  assertNotNull(lookup("#JJJarioLevelView").query());
}
```
- JJJarioMoveGuyOnKey (Adam)
    - covers if the user can press the `D` key and the player image's `X` position changes
```java
@Test
void JJJarioMoveGuyOnKey () {
  JJJarioLevel1Button();
  assertNotNull(lookup("#Player").query());
  ImageView playerImg = lookup("#Player").query();
  double prevX = playerImg.getX();
  press(KeyCode.D);
  double currX = playerImg.getX();
  assertNotEquals(prevX, currX);
}
```

## Sprint 1 Recap


- Accomplished:
    - game library window
    - level selection
    - basic game over window
    - data file parsing
    - sprite rendering
    - basic collision detection and handling (sort of)
    - view model observer pattern
- Difficulties:
    - data file format miscommunications
    - post collision displacement
    - might have taken on too much

- Significant Event
    - Model view communication setup went very smoothly! Pair programming is excellent for these.
    - Collision handling kept producing unexpected issue cases (like post collision displacement). We hadn't walked through enough use cases before starting.
- Communication
    - Using slack and pair programming a good amount has reduced communication issues. We decided to comment our code more as we write to help teammates understand model/view methods more easily.

- Next Sprint
    - Outstanding tasks
        - refactor collision handling to be more robust (Jin, Juhyoung)
            - post collision adjustment
            - directional collision
        - rethink gravity and movement (Jessica)
        - rework view window during level runtime (Adam)
        - allow user customization of language and other UI features (Adam)
    - Future tasks
        - user profile parser and controller (Noah)
        - user profile view windows and accepting profile changes (Adam)
        - user-controllable game settings (Adam)
        - ability to update game settings during runtime (Adam, Jessica)
        - flappy bird collision handling (Jin, Juhyoung)
        - flappy bird data files (Juhyoung, Noah)
    - Concerns
        - relies on collision handling to be robustly refactored (WIP)