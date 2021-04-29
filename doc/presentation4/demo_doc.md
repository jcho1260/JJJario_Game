# Demo Outline

## Functionality

    1. Settings button
        - changing color theme and adding new themes
    2. Profile creation
        - Logging in
        - High scores
        - Changing keybinds
        - Change image
        - Logout button
        - Log back in
    3. Play game and talk about model stuff
        a. collisions and movement
    5. Die in level 1
        a. game over screen
    7. restart level 1
    8. finish level 1
        a. winning screen
    10. save level 2 and reload and finish
    11. StageBuilder
        - Make a simple level by adding a player and block
        - Load the level
    12. JJJalaga to show shooting

- run the program from the master branch through a planned series of steps/interactions that shows at least the following features:
    - choosing a game to play, playing a game, winning or losing a game, choosing a different game to play and starting that one
    - choosing different color themes
    - saving a game and restarting from that point
    - any optional features implemented
    - anything else that makes your project unique and interesting
- show an example of each kind of data file used by the program and describe which are essential (e.g., internal resources) and which can be user created (e.g., external or example data)
    - External
        - Level1.XML of JJJario
        - LevelNames.XML of JJJario
        - collisions.XML of JJario
        - JJJarioSpriteKeys.Properties
    - Internal
        - Game Template data files
        - Builder data files
    - show three examples of making a change in a data file and then seeing that change reflected when the program is run
        - Gravity scale
        - New enemy creation
        - Change collision method with an enemy gives health
- show a variety of tests (including both happy and sad paths for both the backend and frontend) and verify they all pass
    - Profile edit test & failed edit test
    - method reflection success and fail with parameters (DestroyableCollisionHandling)
    - JJJarioMoveGuyOnKey
    - movement test - jump simulation

## Design

Revisit the design from the original plan and compare it to your current version (as usual, focus on the behavior and communication between modules, not implementation details):
- revisit the design's goals: is it as flexible/open as you expected it to be and how have you closed the core parts of the code in a data driven way?
    - Java code would need to be added for game specific methods that don't exist, but the current Java code supports the most common game functionalities (health, score)
        - All games and game objects have been categorized so that it is easy to create new game objects and categorize them into types
            - Player, Destroyable (doesn't move but has health), MovingDestroyable (usually enemies; can move and have health), GameObject (doesn't move or have health)
    - Functioning games can be built from existing elements through ~ creativity ~
        - Utilizing creativity, players can implement new variations on most existing platformers. Even the definition of a platformer can be expanded.
- describe two APIs in detail (one from the first presentation and a new one):
    - Use of listeners:
        - Listener between each gameobject and sprite, listener between gameworld and controller, listener between player gameobject and gameworld -> sent to listener between gameworld and controller
    - Communication between frontend and controller
        - show skeleton code :snail:
    - show the public methods for the API
        - show skeleton code :)
    - how does it provide a service that is open for extension to support easily adding new features?
        - easily add property change methods that pass info through listeners between MVC
    - how does it support users (your team mates) to write readable, well design code?
        - listeners ensure that team mates can get information from other parts of the project in a consistent and expected manner
    - how has it changed during the Sprints (if at all)?
        - hashmap of listeners was required when we needed to differentiate which listener the model wanted to communicate with since the property changes that were being called differed between teh controller and view in the viewpoint of the model
- show two Use Cases implemented in Java code in detail that show off how to use each of the APIs described above
    - killing the player:
        - everytime a collision that results in a decrementing of health, a change in health is notified to the front end to be displayed in the heads-up screen.
        - once the player dies, the controller knows
        - GaveView.gameover is called when the player is found to have died which is checked in the step method of the controller at every step
    - Showing only active objects
        - view window follows the player, so all objects outside the view must be marked as inactive and visibility must be adjusted
            - every step the active game objects are found and all that are inactive turn their visibility off
- describe two designs
    - one that has remained stable during the project
        - Factories used in view has been the same the entire project
    - one that has changed significantly based on your deeper understanding of the project: how were those changes discussed and what trade-offs ultimately led to the changes
        - NonPlayable class has undergone significant changes since the start. As we gained a deeper understanding, we realized that we were trying to shove too many characteristics into one class. In order to separate responsibility and write more readable code, we split the class into a hierarchy. This way, we could still refer to all game elements under the same class GameObject, but we could also specify to get unique features.

## Team
Present what your team and, you personally, learned from managing this project:

- contrast the completed project with where you planned it to be in your initial Wireframe and the initial planned priorities and Sprints with the reality of when things were implemented
    - Jin: Followed initial plan relatively closely
    - Jin: Model planned to have the game engine implemented by sprint 1, but the reality was we were still implementing core features up until the final deadline. On the flip side, model successfully implemented a game a week.
    - Adam: The save/load functionality was pushed back from the second sprint week to the third sprint week for two reasons:
        1. there was a good amount of work required to get profiles working causing us not have a lot of time to do other aspects
        2. the save/load game functionality was very closely related to the game stage builder and thus these functionalities made more sense to be worked on in conjunction with one another

Individually, share one thing each person learned from using the Agile/Scrum process to manage the project.
- Noah: It's important to keep the entire team updated on significant events that occur so everyone is on the same page
- Jin: Creating a plan for the week helps break down a large project into smaller, more reasonable deadlines
- Jessica: Having flexible timelines help when inevitable problems arise
- Adam: Regular scrum meetings keep everyone in the loop on significant events
- Juhyoung: Reflecting helps reorient the team to keep progress on track week to week

show a timeline of at least four significant events (not including the Sprint deadlines) and how communication was handled for each (i.e., how each person was involved or learned about it later)
- Displaying a controllable character
  - Team effort: Jessica, Jin, Juhyoung implemented backend, Noah and Adam implemented controller and front end
- Changing GameObject structure from Actors to composites
    - Jessica, Jin, Juhyoung implemented; Noah adapted to new constructors
- Revamping collisions implementation
    - Jessica, Jin, Juhyoung wrote/implemented; Noah and Adam informed later
- Adam: Adding user profile functionality to the application
    - Noah wrote the backend profile framework and tests
    - Adam used Noah's backend and wrote the user interface for logging in and editing profiles
    - Jessica, Jin, Juhyoung were informed of these changes both via slack updates and a demonstration during a meeting
- Individually, share one thing each person learned from trying to manage a large project yourselves.
    - Juhyoung: establishing how data can be accessed was crucial
    - Noah: managing schedules and timelines
    - Jessica: Consistent communication between different members and parts of the project is important to keep everyone up to date on the many changes
    - Jin: pair coding or just coding while on a call together regularly was very helpful in being able to share ideas and communicate integration as we went so that refactoring and code changes would be prevented in the future
    - Adam: While managing this size of a project, I found it to be very useful to try and code with a team member working on an entirely separate functionality. Because of the size of the project, pair coding was not always feasible. However, having people work on different things allowed for both teammates to improve their designs in the following ways:
        1. Discussing ideas with an outside perspective causes the implementation to need to be explained clearly during the code, making the coder think critically about their design
        2. Having an outside perspective allows someone to play devil's advocate with mostly no assumptions, making inflexible design more visible
- describe specific things the team actively worked to improve on during the project and one thing that could still be improved
    - whenever someone would be busy, we tried to accomodate by working earlier or later
    - coding in groups was done to aid communication and also to bounce ideas to better the design
    - Noah: our git practices were not the most healthy and could have been improved with more frequent merge requests and better handling of issues
- Individually, share one thing each person learned about creating a positive team culture.
    - Juhyoung: ensuring everyone had the same goals helped keep everyone on the same page
    - Jessica: being understanding of non-cs308 conflicts and obligations helps members do their best work
    - Adam: being respectful of others time by being prompt to meetings and giving proper notice when we could not make meetings
    - Noah: being open to constructive criticism about design ideas.
    - Jin: being prepared to be productive during meetings to respect each others' time and other responsibilities
- revisit your Team Contract to assess what parts of the contract are still useful and what parts need to be updated (or if something new needs to be added)
    - confirming remains useful. it set expectations for each other and ourselves
    - the meeting times should be updated because they ended up varying week by week as different people had different external commitments
- Individually, share one thing each person learned about how to communicate and solve problems collectively, especially ways to handle negative team situations.
    - Noah: answering messages on slack quickly to ensure that nobody has any blockers.
    - Jessica: being open to multiple ideas from different team members so potential solutions aren't being ignored, and so everyones contribution is valued
    - Adam: being sure to stay patient and calm in communication when things are not working and the deadline is approaching
    - Juhyoung: trusting everyone's judgements allowed us to work more confidently
    - Jin: debugging together especially in integration and near deadlines