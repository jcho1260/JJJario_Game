# OOGA Backlog

### Team Number 1
### Names -
Noah Citron (nc164)  
Jessica Yang (jqy2)  
Jin Cho (jc695)  
Adam Hufstetler (akh47)  

## Noah use cases:
1. User should be able to launch the game and view the select games screen.
2. User should be able to select a game from a list, and play it.
3. User should be able to view their profile
4. User should be able to create their profile
5. User should be able to save edit and save their profile
6. User should be able to save the game state to a properties file
7. User should be able to load a saved game state from a properties file
8. User should be able to view high scores from their profile.
9. User should be able to view other peoples profiles
10. User should be able to launch a custom game from a properties file

## Jin use cases:
1. User should be able to lose/gain health and reflect this change on front end
2. User should be able to stop movement when hitting an obstacle/NonInteractable
3. Non-player actors should be able to respond to collisions with NonInteractables
4. Collisions should be differentiated by direction of collision with other GameObjects and be known to the Objects in the collision
6. User should see a difference in player stats in response to different direction collisions if appropriate change player stats accordingly
7. User should be able to differentiate direction of collision with other GameObjects and execute proper movement
8. User should be able to "gain" a powerup through a collision
10. Enemies' stats should change as defined when interacting with GameObjects
11. User should see enemies' movements change based on collisions if necessary
12. should we queue deletions and have them run after all collisions are checked? la comida para pensar


## Juhyoung use cases:
1. Player can harm enemies
2. Enemies will disappear when killed
3. Enemies can harm player
4. Player will die when 0 health remains
5. Player death ends level
6. Player can be controlled via keyboard
7. Falling out of display bounds kills player
8. Reaching the end of a level ends the level
9. Completing a level gives a score
10. Powerups will be lost upon level completion

## Adam Use Cases:
1. The user runs the application and a main window for OOGA opens
2. The user navigates from the main menu to a game library screen
3. The user chooses a game from the game library screen and the game main menu is displayed in a separate window
4. The user selects a level from the game main menu and the window displays the level
5. The user presses the appropriate move key and the sprite moves forward
6. The user changes the main OOGA window from light mode to dark mode
7. The user defeats an enemy on screen and that enemy disappears
8. The user navigates from the level screen back to the game menu screen
9. The user can never move the sprite beyond the screen, toleranced-drag model of rendering
10. The user can change the settings of the specific game through the game settings menu

## Jessica use cases:
1. Backend location changes can be reflected in the frontend
2. Backend high score changes can be reflected in the frontend high score representation
3. Front-end key press events can be reflected in the backend location variables
4. Backend player health can be reflected in the front end
5. Backend player score can be reflected in the front end
6. Collision movement follows physics
7. Non actors should not be affected by collisions
8. Actor-actor collisions will impacts each correctly based on the other actor
9. Animation clocking in the backend creates changes that are reflected in the frontend
10. Collisions can use reflection to select correct effect
