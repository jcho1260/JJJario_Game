# DESIGN Document for OOGA Team 1
## NAME(s)
- Noah Citron (nc164)
- Jessica Yang (jqy2)
- Jin Cho (jc695)
- Adam Hufstetler (akh47)
- Juhyoung Lee (jl840)

## Roles
- Adam: developed, implemented, and tested the front-end user interface of the application. Helped out with overarching design ideas for the back end of the application.
- Jessica: developed, implemented, and tested the Movement implementations of the model; developed and implemented the collisions implementation of the model; set up listeners for the Observer pattern between Model, Controller, and View.
- Jin: developed, implemented, and tested the collisions detection and handling with reflective calls; implemented and tested collision method banks for different game object types.
- Juhyoung: developed and implemented collision detection and handling; designed and created level data files
- Noah: developed, implemented, and tested the controller package. Built the functionality for parsing game data files, saving and loading games, building games using the game builder UI, and helped with some of the communication code.


## Design Goals
- The goals of this project were to make a game launcher application that could run any reasonable single-player side-scroller platform game. Specifically, we wanted this project to be as data-driven as possible so that adding new games, levels, and UI components could all happen via additional data files rather than editing java code.
- On the front-end specifically, the main design goal was to build JavaFX components from XML data files in a reflective manner so that new components (even those not previously used) can be added by following a standardized format.


## High-Level Design
- Model: GameWorld is called repeatedly from Controller to progress the game loop, which calls other classes in Model to handle game play. Other classes of note in the Model are GameObjects, and the supporting GameObject composite classes - these describe the different types of object that may be present in the game, and the characteristics each have.
- View: LauncherView is the "entrance" to the view package. All other classes are instantiated and used in correspondence to user actions in the UI. For example, the `GameView` class is instantiated in response to the user clicking on a game button in the game library. This means `view` package class interactions occur primarily through the HandlerFactory class but are only called during runtime. Interactions with the `model` package occur through the listener design pattern as sprite, score, health, and other game world updates are informed to the GameView class through listeners. Interactions with the `controller` package occur only through the `Controller` class and are mostly in response to the user requesting to use information that needs to be parsed or deserialized like user profiles or the user is requesting to start a game.
- Controller: The controller centralizes the public facing interfaces in the `Controller` class. To keep much of the public facing interface in `Controller`, we have encapsulated much of the other classes within the controller class. The controller uses three different parsers `LevelParser`, `CollisionParser`, and `LevelNameParser` which can be used to build the model's `GameWorld` entirely from information stored in the data file. Finally, do allow for loading and saving games, we have made the `GameWorld` implement the `Serializable` interface, which allows us to easily save and load the `GameWorld` from a file.

## Assumptions or Simplifications
- Will only support single player games
- In collisions.XML
    - The name of the colliding object will only include a single direction (multi-directional effects need to be declared separately)
    - Directional collisions will take precedence over general collisions
- Destroyable bricks will only have 1 life

## Changes from the Plan
- Removed `Actor` class and `NonPlayable`
- Refactored `Health`, `Movement`, and `UserActions` into separate classes
- Broke `CollisionCheck` down into detecting and executing instead of having a general handeCollisions method
- All methods in the gameobject classes that can be reflectively called have become public to be able to be invoked reflectively
- Removed `GameState` class and stored its information in `GameWorld`
- Removed `Level` class and redistributed its information into `GameWorld` (getGameObjects) and `GameObjects` (getStartingLives)
- Removed Prop class
- Used listeners instead of a getter for scoring in the `Player` class
- View added `.start` methods to builder classes for front end elements

## How to Add New Features
- New collision methods (game play properties that should be triggered by a collision) should be added to the appropriate GameObject class or child class under `model/gameobjects`, depending on how ubiquitous the method should be. The name of the each newly added method should be referenced directly in the `collisions.XML` in the context of GameObjects colliding with each other.
- New color themes can be added by adding a `THEMENAME.css` file to `color_themes/` and adding the appropriate key/file path pair to `color_themes/ColorTheme.properties`. The format of the CSS file can be seen in the included color theme files.

### Adding a new game

#### Front-End Data Files
For a new game to be added on the front-end, the following data files must be added or edited (all file paths are from `resources/view_resources/`):
- A new game library button must be added in `launcher/GameLibrary.XML`. This can be easily done by copying one of the other game buttons and changing the instances of the old game name to the new game name.
- A new profile high score box must be added in `launcher/profile/ProfileMenu.XML`. This can be easily done by copying one of the other game high score boxes and changing the instances of the old game name to the new game name.
- A new resource bundle must be added to `game/SpriteImageKeys/` with the naming scheme `GAMENAMESpriteKeys.properties`. This data file should contain a key for each possible sprite object that exists in the game with the given file path to that sprite's image. See the 4 example files for more clarification.
- A logo for the game should be added to `images/logo/` with the format `GAMENAMELogo`
- Images for the game sprites should be added to `images/sprites/GAMENAME/` and these are the images to be referenced in `GAMENAMESpriteKeys.properties`

#### Back-End Data Files
- need to add level xml files
    - all individual level files must be names in the format of `Level#.XML`
    - all levels must be listed out in a `LevelNames.XML` file for each game added
- collisions must be defined for all potential collision types
    - `collisions.xml` will have all these defined between Actors and GameObjects
    - if collision method is not already defined, you must add it to the game object class (or child class), but this is the only potential java code needed to be written

### Adding a new level
- Add a new `Level#.XML` that describes the properties and locations of each of the GameObjects in the new level of the game
- Add the name of the `Level#.XML` to `LevelNames.XML`
- If the level requires new GameObjects not previously used in other levels, add the GameObject collision information to `collisions.XML`