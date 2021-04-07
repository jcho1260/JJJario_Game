# Methods to Implement

### Destroyable
- [ ] incrementScore - will any non player be able to increment score?
- [x] isAlive
- [x] incrementHealth
- [ ] onDeath -  feel like this should be reflectively called and defined in a datafile b/c 
it can vary for each destroyable (gumba falls or smthg but block releases a powerup)
- [x] kill

### MovingDestroyable
- [x] scaleVelocity

### Player
- [x] scaleSize - priv var redeclared in player bc composition but is this duplication?
- [x] incrementScore - protected variable to priv variable, should score be composite?
- [x] incrementLife - refactor for life to be composite? or no bc only player has this i think
- [ ] onKeyPress - need to get key map definitions from controller in parsing in datastructure and the
methods associated with each and respond with reflection?
