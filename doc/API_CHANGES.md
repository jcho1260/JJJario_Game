# API Changes
Noah Citron (nc164)
Jessica Yang (jqy2)
Jin Cho (jc695)
Adam Hufstetler (akh47)
Juhyoung Lee (jl840)
## Comprehensive Changes
- Removed `Actor` class and `NonPlayable`
    - Recategorized into combinations of two characteristics: destroyable and moving
    - `NonPlayable` was too broad of a class, so we split into more specialized classes. This allowed us to create composition classes that could be inherited from. `Destroyable` is a composition of `Health` and `DestroyableCollisionHandling` inheriting from `GameObject`, and `MovingDestroyable` is a composition with `AutomatedMovement` inheriting from `Destroyable`.
    - This results in much cleaner classes that better observe the Single Responsibility Principle.
- Refactored `Health`, `Movement`, and `UserActions` into separate classes
    - Refactored `Movement` into `AutomatedMovement` and `UserInputMovement` classes. Refactored `UserActions` into `UserInputActions` and `UserInputMovement`.
    - We split movement because automated movement and player movement take in different parameters that need to be resolved differently. Making them their own classes makes our code more SHY and helps uphold single responsibility. They are integrated as parts of composite `GameObject` classes.
    - `UserActions` became `UserInputActions` (to help clarify itself) and inputs were split into two types: moving and shooting. Moving was refactored out into `UserInputMovement`. This helps encapsulate things further as `UserInputActions` only has to receive inputs. Things like interpreting the received inputs are handled by its composite, `UserInputMovement`.
- Broke `CollisionCheck` down into detecting and executing instead of having a general handeCollisions method
    - We now have a `WorldCollisionHandling` class that checks all collisions in the `GameWorld` and `DestroyableCollisionHandling` class that handles collisions at a game object to game object level.
    - By doing this, we split responsibility into the lowest level, making our classes more SHY. `DestroyableCollisionHandling` is responsible at the `Destroyable` level because each object should keep track of who it has collided with. It's also contains methods that help process collisions.
- All methods in the gameobject classes that can be reflectively called have become public to be able to be invoked reflectively
    - This allows games to reuse methods that affect `GameObject` instances. For instance, `incrementHealth` is used in all the games anytime anything needs to be destroyed.
    - In order to streamline this process, we created the `MethodBundle` class. We saw that Java had one, but we were looking for a slightly different implementation. We also didn't need all the features that came with the Java version.
    - Using reflections allows us to avoid hardcoding methods for each class, making our project extremely data driven. We avoid hard coded strings too.
- Removed `GameState` class and stored its information in `GameWorld`
    - We removed `GameState` because it was a passive class. We end up with a massive constructor for GameWorld, but that is part of having a very data driven design. Everything comes together in GameWorld, and we couldn't reduce the parameter list without creating passive data storage classes.
- Removed `Level` class and redistributed its information into `GameWorld` (getGameObjects) and `GameObjects` (getStartingLives)
    - Level was a passive class, so we removed it. This also distributed data down to where it needed to be. `GameObjects` needed to know how many lives it had in case it needed to respawn.
- Removed Prop class
    - We realized it was a redundant class as `Prop` instances were just `GameObject` instances with no added features.
- Used listeners instead of a getter for scoring in the `Player` class
    - Using listeners allows us to keep our code SHY. The front end needs the score, but we didn't want to give it access to the `Player`. By using listeners, we avoid having direct dependencies.
- View added `.start` methods to builder classes for front end elements
    - Controller initializes the game as this is where the parsing of game files occurs
    - Controller must be able to trigger the creation of front end elements through the public API, therefore these methods were added to Builders