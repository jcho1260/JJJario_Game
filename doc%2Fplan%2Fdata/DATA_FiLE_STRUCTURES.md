# Game Object File

Root
- Entity
    - Name: player triangle
    - Image: jpeg path
- Entity
    - Name: block
    - Image: jpeg path
- Entity
    - Name: enemy triangle
    - Image: jpeg path




# Level File

Root
- Classifications
    - “player triangle”: Player
    - “enemy triangle”: NonPlayable
    - “block”: GameObject

- objects
    - player triangle
        - Location: (5, 10)
        - Velocity: 0
    - enemy triangle
        - Location (10, 10)
        - Velocity: -1
    - enemy triangle
        - Location: (15, 10)
        - Velocity: -1
    - block
        - Location: (0:20, 10)

# Collision File (for TrianglePlayer) - ResourceBundle

TriangleEnemy = loseHealth, bounceMovement
RectangleObstacle = stopMovement

# Non-Playable File (for TrianglePlayer) - XML
Root
- ConditionalType
    - MethodName
    - Parameter1…




# GameMenu
Game Menu
- Background
    - Image: data/resources/triangle_player/TrianglePattern.png
- Title
    - Image: data/resources/triangle_player/TrianglePlayerTitle.png
- Levels
    - Level Names: tutorial
    - Level Parameters
        - Param1
            - Parameter Name
            - Parameter Type
            - Parameter Values
        - Param2
        - Param3...
- Settings
    * Setting1
        * Setting Name
        * Setting Type
        * Setting Values
    * Setting2
    * Setting3...


# Profile
Root
- ProfilePicture
    - File
- Name
- Age
- Highscores
    - Game
        - Score
    - Game
        - Score
- KeybindMapping
    - Mapping
        - SrcKey
        - DstKey
    - Mapping
        - SrcKey
        - DstKey

# Sprites
Sprites
- Sprite1
    - Sprite Type: 
    - Sprite Image: 
- Sprite2
- Sprite3...

