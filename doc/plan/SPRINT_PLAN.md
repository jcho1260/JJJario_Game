# Sprint Plan

## Team Member Responsibilities Prioritization
* Adam Hufstetler
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
## Rough Timeline
* Sprint #1
    * Goals
        * Basic game functionalities working, controller able to correctly deliver limited but usable data to both front end and back end
        * Front end able to render simple game layout, reflect updates from controller on screen, and understand/pass basic user input correctly to controller
        * Back end able to update state according to user input, and maintain a state consistent with game rules and display.
    * Adam Hufstetler
        * Implement `MainMenu` and `GameLibrary` in the `Launcher` module
        * Implement `GameMenu`, `LevelLibrary`, and `LevelView` in the `Game` module
        * Write front end data files for the new games that are being implemented
    * Juhyoung Lee
        * Implement `GameObject`, `Actor`, `Player`, and `NonPlayable`
        * Create Mario data files
    * Noah Citron
        * Parse all data files
        * Generate Levels, GameObjects and NonPlayables from those files
        * Generate profiles from files
    * Jessica Yang
        * Set up observables for back end/front end communication, focusing on changes from back end to front end, and then front end key presses to back end
        * Implement `Movement` (accounting for gravity)
    * Jin Cho
        * Implement `GameState`, `GameWorld`, `CollisionCheck`, `Level`
        * Create Levels that will be parsed to create a Mario game level

* Sprint #2
    * Goals
        * Implement data files for 2 pretty different games such that the games are functional
        * Implement changing game settings during runtime through the UI
        * Implement profile updating and setting changes
    * Adam Hufstetler
        * Implement `ProfileMenu` and its various sub-menus in the `Launcher` module
        * Write front end data files for the new games that are being implemented
    * Juhyoung Lee
        * Implement Flappy Bird and Zelda collision interactions
        * Create Flappy Bird and Zelda data files
        * Polish Mario
    * Noah Citron
        * Add ability to update player profiles
        * Add highscores
        * Add keybinding support to profiles
    * Jessica Yang
        * Implement Flappy Bird and Zelda collision interactions
        * Create Flappy Bird and Zelda data files
        * Polish Mario
    * Jin Cho
        * Implement Flappy Bird and Zelda collision interactions
        * Create Flappy Bird and Zelda data files
        * Polish Mario

* Complete
    * Goals
        * Game Level Builder
        * 3 complete games implemented through data files
    * Adam Hufstetler
        * Implement `GameStageBuilder` and its various sub-components as describe din the plan
    * Juhyoung Lee
        * Polish Flappy Bird and Zelda
        * Make sure Model is flexible enough to make any game specified with data files
    * Noah Citron
        * Save game to a file
        * Game Level Builder backend support
    * Jessica Yang
        * Polish Flappy Bird and Zelda
        * Make sure Model is flexible enough to make any game specified with data files necessary
    * Jin Cho
        * Polish Flappy Bird and Zelda
        * Make sure Model is flexible enough to make any game specified with data files necessary

