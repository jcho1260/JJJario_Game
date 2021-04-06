# OOGA Design Plan

### Team Number - 01

### Names -
Noah Citron (nc164)  
Jessica Yang (jqy2)  
Jin Cho (jc695)  
Adam Hufstetler (akh47)  
Juhyoung Lee (jl840)  

## Introduction
The goal of this project is to construct a flexible Game Engine that allows for implementation of a variety of scrolling screen games. In order to show this flexibility, we will be implementing 2-3 different games of this scrolling screen genre.


## Overview
- Goal: extensible side scroller platformer that can support multiple games through adding data files that represent game types and specific levels
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
- Refer to Design Details for further details of specific classes
### UML
![](https://i.imgur.com/8vnL8Ar.png)


## Design Details
### Back End
- Controller - should not change with the implementation of different games so deals with things consistent across all games. with this information, it will construct the proper game world type for the selected game
    - LevelParser - parses through data file describing entities of a level and other parameters necessary to construct a level
    - ProfileParser - parses through data file specifying details for each player profile
    - NonPlayableMethodsParser - parses through ResourceBundle that defines the methods associated with a non-playable game object
- GameWorld - created for each new game that is started and keeps track of all objects in a running game. cycling through game animations will happen here. collisions will be detected here.
- CollisionsCheck - will check for collisions at each step between all active actors and active GameObjects
- GameState - keep track of the timer of the game to see if the game should end if determined by time
- GameObject - all entities in the backend will be represented as a GameObject object
    - Actor Abstract Class - objects that are able to have their health damaged through a collision
        - Player Class - object that the user controls through key presses. is the "main" object in the class that has its unique set of abilities as the main player in a game
        - NonPlayable - all objects that a player can interact with with which collisions will affect the player or other interactable objects. This will have a "bank" of all methods that a nonplayable can have. this is where new game implementations will define methods that aren't defined
- Movement - calculates resulting coordinates resulting from a specified movement of a GameObject
- Level - holds all the information for a level and its settings
- Profile - holds information needed in the backend about the profile (high score)
- Vector - structure to define an x and y value that will be used to define coordinates and velocity vectors


### Front End
- `MainMenu` - the first thing the user sees; has the following tabs
    - `GameLibrary` - where the user views games and chooses the game to play
        - selecting a game from the game library opens a game menu
    - `ProfileMenu` - where the user views their profile and changes a profile, has the following view types
        - `ProfileViewer` - where the user can view their own profile, default view
        - `ProfileEditor` - where the user make a new profile or edit an existing one, activated by an edit profile button and changes are saved on a save profile that switches back to the `ProfileViewer` with the new changes
        - `ProfileLogin` - where the user can login into a profile by providing the username and password
    - `GameStageBuilder` - where the user can create their own levels, most of the information for this view is taken from the data files used to parse level files
        - `GameStageViewer` - scrollable and zoomable window that shows the current game stage the user has designed
        - `GameStageLibrary` - library of all possible game objects for a given game type
        - `GameObjectEditor` - view for the editing of the initial parameters of a given game object
- `GameMenu` - the menu for each game, should be constructed from data files defined by each game implemented, has the following sub menus
    - `LevelLibrary` - where the users chooses which level they want to play, choosing a level would then require the user to provide any `neccesary` parameters and then open a `LevelView`
        - `LevelView` - the screen displaying the actual game, functionally is just displaying game objects and handling game input
    - `SettingsScreen` - where the user changes any settings for the given game

## Design Considerations
(include pros and cons from all sides of the discussion) as well as any ambiguities, assumptions, or dependencies
- Collision Checking
    - We first considered dividing actors into categories and having general collision methods within each class. This assumed actors could be categorized by types of collision interactions.
        - Pros: Easy to implement game variations that utilize the same set of actors.
        - Cons: Hard coded actors make adapting to new game types difficult. Actor classes are heavily dependent on each other for collision checks.
    - We decided to create `NonPlayable` that contains all the possible collision methods. Data files specify which methods are called for which collisions.
        - Pros: Easy to implement multiple game types. Doesn't force destroyable categorization.
        - Cons: Necessitates multiple data files that need to be checked for consistency. The Collision file specifically needs to ensure the same collisions aren't handled in multiple ways.
- Lambda Collision Handling
    - We considered using lambda expressions to handle collisions instead of using method calls.
        - Pros: Very flexible with little hard coding.
        - Cons: At that point, you're coding in the data files.
    - We opted to use method calls specified in `NonPlayable`
        - Pros: Easily understood code. Creates reusable building blocks for future games.
        - Cons: `NonPlayable` can get messy if too many collision handling methods are added.
- Movement Class
    - We first had movement within the `Actor` class.
        - Pros: Natural inheritence structure. Actors are GameObjects that can move.
        - Cons: Movement is strictly defined and difficult to vary from.
    - We then decided to have movement extracted to `Movement`.
        - Pros: Very flexible movement. Games will decide via a text file how actors move. Utilizes the composition design.
        - Cons: Movement will need to be designed and accounted for.


## Example Games
- Super Mario
    - Level Goal: reach the castle
    - User input: left, right, jump, general interact key
    - Enemies: Goomba
- Super Mario Back End Support
    - Mario is `Player`
        - Movement is standard and handled by `Movement`
    - Goomba is `NonPlayable`
        - Collisions with `Player` will call `incrementHealth` by -1
    - Blocks are `GameObject`
        - Moving platforms will be given a velocity and a frame interval after which to switch direction
        - Block collisions with `NonPlayable` will negate `NonPlayable` velocity
        - Block collisions with `Player` will set velocity to zero
- Flappy Bird
    - Level Goal: reach the end of the level
    - User input: bird jumps
- Flappy Bird Back End Support
    - Bird is `Player`
        - Velocity is set to a constant rightward speed
        - gravity is on
    - Pipes are `GameObject`
        - Velocity is 0
        - Gravity is off
        - Collisions with player will call `kill`
- Zelda
    - Level Goal: get to the princess
    - User input: left, right, up, down, sword, shield, bow
    - Enemies
- Zelda Back End Support
    - Zelda is `Player`
        - gravity is off
    - Ganon is `NonPlayable`
        - Collisions with player will call `decrementHealth`
## Test Plan
### Testing Strategies
- `GameWorld` and `GameObject` variable examination
- Front end ID tagging

### Testing Scenarios
#### Movement Detection and Execution
- Scenario #1: User uses default key bindings to move the playable character in free space.
    - simulated key press of correct key and check that the x-location of the playable character has changed between before and after the key press
- Scenario #2: User uses default key bindings to move the playable character until it collides with something, which stops it.
    - simulated key press of correct key and check that x-location of the playable character does not intersect with the x-location of the collided object
- Scenario #3: User attempts to use incorrect keys to move the playable character, and a reminder window will pop up.
    - simulated key press of incorrect key and tagged reminder window appears

#### Data File Parsing
- Scenario #1: Collection of GameObjects is correctly parsed from the XML and constructed with reflection to initialize the level
    - List of GameObjects is constructed with the correct GameObject types with correct flags
- Scenario #2: For each `NonPlayable`, the proper `GameObject` parsing will map the correct repective method calls/abilities
    - Tested by seeing if a NonPlayable executes specialized methods through observing the GameObject characteristics in the backend
- Scenario #3: Incorrect formatting of XML file defining a level
    - tested by creating an incorrectly formatted XML defining Level and seeing if an error is thrown

#### User Profiles
- Scenario #1: User edits their profile to their correct name and the data file reflects that change
    - tested through profile data file examination
- Scenario #2: User sets their default movement key bindings to WASD and plays a game with these key bindings as the controls
    - simulated key press of a non-default movement key changes the position of the player game object in the back end as expected
- Scenario #3: User leaves a required profile field blank, this causes an error message to appear requesting the user
    - add a tag to the error window and assert that the tag is true after the error is thrown

#### Game Stage Builder
- Scenario #1: User adds a few entities to the game, and successfully saves it to a file
    - tested through testFX and reading the produced file
- Scenario #2: User can change the location of where the file is saved
    - tested through testFX and reading the produced file
- Scenario #3: User places an `Actor` inside of another `GameObject` which causes an error to be displayed stating this action is invalid
    - Tested through testFX and checking for a exception

