ooga
====

This project implements a game launcher for multiple related games.

Developers:
Noah Citron (nc164)
Jessica Yang (jqy2)
Jin Cho (jc695)
Adam Hufstetler (akh47)
Juhyoung Lee (jl840)


### Timeline

Start Date: 3/28/2020

Finish Date: 4/25/2020

Hours Spent: 5 * 70 = 350 hours

### Primary Roles
- Noah: Controller
- Jessica: Model
- Adam: View
- Jin: Model
- Juhyoung: Model

### Resources Used


### Running the Program

Main class: Main

Data files needed: XMLs configuration files for each game to run (Level#.xml, collisions.xml, LevelNames.xml); color themes; .properties for view factories; sprite property files; .css files for view; front-end XML configuration files; images for backgrounds, button icons, logos, profile, and sprites.

Features implemented:
- Essential
    - Model
        - Can create an entirely new single player side scroller platform game using XMLs that define the properties of GameObjects in the game, collisions between GameObjects, and the images required to display them
            - Can create different levels for a single game that operates under the same set of rules and definitions
        - Player can move in any of the cardinal directions, with or without any level of gravity, and can shoot projectiles in a single direction
        - GameObjects can be defined to move automatically between locations, with or without any level of gravity, and perform collisions between specific GameObjects at the game designer's choosing
        - Collision events can perform a variety of game state changes, including changing GameObject health, GameObject lives, and player score. Any additional methods not currently defined can be easily defined in GameObject classes and child classes to be referenced in XML files.
        - Collisions are detected using rectangular hitboxes, with leeway built in for corner collisions
    - View
        - Can select different games to play, and specific levels to play under a certain title
        - Can change the color theme of the game launcher
- Basic Extensions
    - High scores
        - show on profile
    - Load games
    - Save games
- Mild Extensions
    - Player Profiles
        - can personalize key bindings, see high score, and add a profile picture
- Challenging Extensions
    - Game Area Editor
        - allowed to choose the game objects and their locations
        - includes an image that reflects position of added GameObjects

### Notes/Assumptions
Assumptions or Simplifications:
- Will only support single player games
- In collisions.XML
    - The name of the colliding object will only include a single direciton - multi-directional effects need to be declared separately
    - Directional collisions will take precedence over general collisions
- Destroyable bricks will only have 1 life

Notes:
- When creating a new game:
    - need to add as a button in `GameLibrary.XML`
        - need to have appropriate name of Game and image for game cover
    - need to add into `ProfileMenu.XML` so that it shows up in profiles with high score for the game
    - need to add all images and image file paths
        - in `images` directory of view_resources, sprites for entities needs to be added in `sprites` directory
        - paths to these sprites need to be specified in a ResourceBundle in `SpriteImageKeys` directory under `game`
    - need to add level xml files
        - all individual level files must be names in the format of `Level#.XML`
        - all levels must be listed out in a `LevelNames.XML` file for each game added
    - collisions must be defined for all potential collision types
        - `collisions.xml` will have all these defined between Actors and GameObjects
        - if collision method is not already defined, you must add it to the game object class (or child class), but this is the only potential java code needed to be written

Interesting data files:
- In 3 XML files in the resources of model, we can make a whole new, fully defined game! (Sprite images will still need to be added into view_resources and paths must be defined in resource bundle)
    - `collisions.XML`
    - `Level#.XML` --> however many levels that are needed but minimum 1
    - `LevelNames.XML`

Known Bugs:
- Collision handling: corner collisions cause automated movement in MovingDestroyables in JJJario to stutter, and can cause FlappyBird to be hit when it shouldn't be

Extra credit: N/A


### Impressions

Overall, this project provided a lot of freedom for creativity in implementation of different games. The data driven aspects required us to think about flexibiltiy of design throughout the process. 
