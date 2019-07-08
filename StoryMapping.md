# Story Mapping for Ms. Pac-Man

## BHAG: (Siqi Chai)
The pacman is trapped in an underground prison, trying to avoid monsters, eating cherry for energy supply, and seeking for an opportunity fight back.

## Detailed Game Description: (Siqi Chai)
This is an implementatio of the classic arcade game Ms. Pacman on the Android plateform. This newer version holds the same spirit as the old one.
The pacman will be trapped in a underground prison with highly complicated paths. There are monsters that runs after the pacman, and once the pacman
bumps into a monster, it is died. Of course, the pacman will be able to fight back. The pacman can eat cherries or coins to collect energy or slow down
the monsters. Once the monsters are slowed down, the pacman will be able to attack back. Instead of controling the pacman with a gamepad, in this Android
game, the player will direct the pacman with the virtual pointer. 


## User Stories: (Pacman, Enemy)
### Pacman: (Alessandro)
1) As a Pacman, I want to earn points by eating pellets and avoiding enemies. 
2) As a Pacman, I will die if I touch an enemy. 
3) As a Pacman, if I am in danger, I can eat a power pellets to counterattack my enemies.
4) As a Pacman, if I want to be the best fugitive, I will have to earn the maximum points possible.
5) As a Pacman, I have 3 life.

### Enemy:(Xinwei Wu)
6) As an enemy, I want to kill the Pacman through a single touch.
7) As an enemy, if I see Pacman getting the cherry, I will try to run faster to kill the Pacman.
8) As an enemy, I can be killed through contact and slow down within 10 seconds after Pacman eats the power pellets.
9) As an enemy, if I bump to any of my co-workers, I will move to the opposite direction.

### Cherry:(Adila)
10) As a cherry, I may or may not show up after the pacman eats the power pellets.
11) As a cherry, I will show up more frequently as the game goes on.
12) As a cherry, I will move around.
13) As a cherry, I will disappear immediately after pac man dies.

## Sprints (Jian)
### Sprints Chart
Objects | Pacman | Ghost (Enemy) | Grid (Map) | Small Coin | Big Coin |  Cherry 
--- | --- | --- | --- |--- | --- | ---
Sprint 1 | Able to move | 4 Ghosts; <br> Able to move; <br> Can kill Pacman by touching | Simple horizonal and vertical grid | One small coin in one grid | N/A | N/A 
Sprint 2 | Will be able to kill ghosts after eating big coin | Enemy can move randomly; <br> Generate new ghost when there is ghost being killed | Letter grid |  | One big coin in the corner of the grid | N/A 
Sprint 3 | When all pellets are eaten, PacMan is taken to the next level. |Generate the vonerable ghost affects and also sounds.  | More than one grid system (more than one map) |  |  | One cherry will be generated randomly for extra points 

### Sprint 1: 
Build Grid (with obstacles). 
Implement the figures, such as Pacman, enemies, pellets, power pellets, cherry, etc.
Pacman can move by user input (input management).
Enemy can move randomly.  
Possibly accomplish: the user stories 1, 2, 5, 6.

### Sprint 2: 
Possibly accomplish: the user stories 1, 2, 5, 6. 3 8 9 

### Sprint 3: 
Sound effect  
Possibly accomplish: the user stories 4, 7, 10, 11, 12

## CRC 
### Classes: 
MainActivity: 	overall game lifecycle

Pakman:  		how the pacman moves, interacts, and looks like

Enemy:			how the monsters move, interact, and look like

Blocks(grid):	Part of the arcade, marks the paths of the characters

Gold:			controls the reward

Levels:			controls the hardness of the game: how many monsters, how fast do they move, etc.

UserInput:		transfrom tapping event into up/down left/right

Painter:		contrals the display
