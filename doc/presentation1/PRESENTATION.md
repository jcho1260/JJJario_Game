# OOGA Plan Presentation
## Team 01
- Noah Citron (nc164)
- Jessica Yang (jqy2)
- Jin Cho (jc695)
- Adam Hufstetler (akh47)
- Juhyoung Lee (jl840)

## Implementation Plan
- Genre
    - Side scrolling platformer
    - Similarities:
        - Playable character that can move on the screen based on user input
        - Objects on the screen that can collide with the playable character
        - Enemy characters that can impede the playable character or otherwise harm
        - Power ups that can impact game play either negatively or positively
    - Differences
        - Direction of movement: mario can move up, down, left, and right while flappy bird can only be moved up and down
        - Perspective: mario is side view with a gravity component to movement, while zelda is top down without a gravity compoent
        - Customization: mario game play is standard from the start of the game, while jetpack joyride involves an initial jetpack selection stage that alters game play
        - Enemy capabilities: mario has a variety of enemies with a variety of ways of harming the playable character; flappy bird is limited to auto fail upon hitting a tube
- Features
    - Ability to add new levels with XML files
    - Ability to implement new players/GameObjects with data files
    - Specific Games
        - Mario, Flappy Bird, Zelda Games
    - Extra Implementations
        - Profiles
            - high score
            - key mapping personalization
        - Loading and saving games
        - Game Area Editor
- Project roles
    -  Adam Hufstetler
        * User Interface Development and Testing
    * Juhyoung Lee
        * Model
            * Game Engine
    * Noah Citron
        * Controller and data file parsers
    * Jin Cho
        * Model
            * Game Engine
    * Jessica Yang
        * Communication (Observables, Controller)
        * Model
            * Game engine
- Sprint completion
    - Sprint #1
        - Basic game functionalities working, controller able to correctly deliver limited but usable data to both front end and back end
        * Front end able to render simple game layout, reflect updates from controller on screen, and understand/pass basic user input correctly to controller
        * Back end able to update state according to user input, and maintain a state consistent with game rules and display.
    - Sprint #2
        - Implement data files for 2 pretty different games such that the games are functional
        * Implement changing game settings during runtime through the UI
        * Implement profile updating and setting changes
    - Sprint #3
        -  Game Level Builder
        * 3 complete games implemented through data files

## Design Plan
- Design and framework goals:
    - Flexible/open: Our design is open since it makes heavy use of data files. Any type of sidescroller can be created just by modifying the data files.
    - Fixed/closed: The view is designed to be as closed as possible. This is because it is very general, taking in entities to display, and rendering them to the screen. Normal extensions will not require modifying or extending it.
- Modules
    - Front end
        - main menu (profile, builder, and game library navigation)
        - game menu (level selection, level playing, and game settings)
    - Control:
        - Parser: reads data files and converts into usable format for front and back end
    - Back end:
        - game engine: runs the game and holds collection of GameObjects and updates them
            - Relies on information from Control to build a given level once the user selects it from the front end
        - GameObjects: back end representation of any components (eg platforms, playable character, enemies, etc) of a level
    - Notes:
        - back end communicates GameObject updates to front end through observers
        - front end communicates key input presses and click events to back end through observers
- Controller
    - Service: Reads all data files, and instantiates the game
    - Extensibility: Extensibility is provided by being able to use data files to completely change the behavior of the game.
    - Ease of well designed development: The Controller class will make use of reflection in order to easily handle creating all objects needed for the model.
- GameObject (Backend Internal)
    - Service: class that deines all objects in the backend and their characteristics. also has lower heirarchy of collision type objects that respond accordingly to collisions
    - Extensibility: Able to define new `GameObject` types with a new datafile
    - Ease of well designed development: All backend objects will be `GameObject` objects and the specifications of each `GameObject` will be extracted from properties files and executed with reflection, but this will not be defined in code and be consistent for all `GameObject` types (`Actor` and `NonPlayble`)
- Alternative design?
    - Instead of using data files to add new games, games would be added through implementing the game development framework
        - Pros: Convenient for games that would be similar in style and type
        - Cons: Less extensible in terms of adding completely new games