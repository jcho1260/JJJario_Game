# DESIGN_EXERCISE
## Names and NetIDs
Adam Hufstetler - akh47  
Jin Cho - jc695  
Jessica Yang - jqy2  
Juhyoung Lee - jl840  
Noah Citron - nc164

## Fluxx

### High Level Design Ideas
- Player class - contains the players' hand
- abstract Card class
    - abstract Rule class
        - abstract rule group classes
            - concrete rule classes for each of the rule cards
                - defines implementations of each rule
                - checks validity of executing the rule based on immutable game state
    - abstract action class
        - implementations for each action card
            - needs access to GateState as a whole in order to set new values and properties (unlike rules)
    - abstract keeper class
        - implementations for each keeper card
- Play class
    - a collection of the actions that will be iterated through to check for actions and changes
- GameState class
    - contains getters and setters for the state of the game
- ImmutableGameState interface
    - An interface with the same getters as the game state, but with the setters removed. When the rule cards need access to the game state, GameState can be casted to an ImmutableGameState so that the rules can read the the state without modifying it.
- Engine class
    - Handles which player should play next, and begins their play
    - Will ultimately know when any player wins and ends the game

### CRC Card Classes

This class's purpose or value is to manage something:
```java
 public class Rule implements Card{
     public boolean isValidMove(ImmGameState gameState, PlayerMove move);
 }
```

```java
public class Goal implements Card {
    public GoalType getType();
    public boolean isGoalComplete(ImmGameState gameState);
    public GameStatus getGoalResult();
}
```

```java=
public class GameState {
    public boolean isGameOver();
    public Goal getGoal();
    public void setGoal(Goal newGoal);
}
```

```java=
public class Action implements Card{
    public void perform(GameState gamestate);
}
```

```java
 public class Engine {
     public void nextTurn (int data)
 }
```

Player holds cards and plays them.
```java
 public class Player {
     public Play takeTurn ();
     public void drawCards(List<Cards>);
     public void placeKeeper(List<Cards>);
     public void acceptCards(Player player, List<Cards>);
     public List<Cards> giveCards();
 }
```

### Use Cases
- A player plays a Goal card, changing the current goal, and wins the game.
```
In GameState:
Goal card variable set to new Goal card
Run goal.isGoalComplete((ImmGameState) this)

```
- A player plays an Action card, allowing him to choose cards from another player's hand and play them.
```
In GameState:
action.perform(gamestate);

In perform of this specifc action card's class:
cards = gamestate.exchangeCards(player1, player2, num);

In gamestate:
exchangeCards:
cardschosen = player1.choose(player2, player1.cards);
player2.acceptCards(player1, cardschosen);

```
- A player plays a Rule card, adding to the current rules to set a hand-size limit, requiring all players to immediately drop cards from their hands if necessary.
```
action.perform(gamestate);

In action:
gameState.addRule(newRule);
```
- A player plays a Rule card, changing the current rule from Play 1 to Play All, requiring the player to play more cards this turn.
```
In GameState:
Rule PlayCount in GameState.rules is updated to play all
Rule is not met, so player.takeTurn()
Rule PlayCount is checked and if the player has cards remaining, the turn is rejected.
```
- A player plays a card, fulfilling the current Ungoal, and everyone loses the game.
```
In GameState:
player.takeTurn()
if goal.isGoalComplete()
    this.playStatus = goal.getGoalResult()
```
- A new theme for the game is designed, creating a different set of Rule, Keeper, and Creeper cards.
```
update the properties file :)
```