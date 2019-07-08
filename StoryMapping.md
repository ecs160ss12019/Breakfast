# Story Mapping for Ms. Pac-Man

## BHAG: 
You are a Pacman, and you are trapped in an underground prison where your mission is to avoid all ghosts, while trying to clear the level by eating all small coins, and seeking for an opportunity fight back through consuming a big coin.

## Detailed Game Description: 
This is an implementati of the classic arcade game "Ms. Pac-man" on the Android platform. This newer version holds the same spirit as the old one, but we will add new flavors on top of it.
The pacman will be trapped in a underground prison with highly complicated paths. There are a total of 4 ghosts who are after the poor Pac-man. If the Pac-man bumps into any of the 4 ghosts, the poor Pac-man dies. The Pac-man can eat any of the several big coins to be able to eat ghosts, while they are being slowed down. Finally, the players will direct the Pac-man with the virtual pointer instead of a virtual gamepad. 


## User Stories: (Pacman, Enemy)
### Pacman: 
1) As a Pacman, I want to earn points by eating pellets and avoiding enemies. 
2) As a Pacman, I will die if I touch an enemy. 
3) As a Pacman, if I am in danger, I can eat a power pellets to counterattack my enemies.
4) As a Pacman, if I want to be the best fugitive, I will have to earn the maximum points possible.
5) As a Pacman, I have 3 life.

### Ghost:
6) As an ghost, I want to kill the Pac-man through a single touch.
7) As an ghost, if I see the Pac-man getting the cherry, I will try to run faster to kill the Pac-man.
8) As an ghost, I can be killed through contact and slowed down within 10 seconds after the Pac-man eats a big coin.
9) As an ghost, if I bump to any other ghosts, I will bounce to the opposite direction.

### Cherry:
10) As a cherry, I may or may not show up after the pacman eats the big coin.
11) As a cherry, I will show up more frequently as the game goes on.
12) As a cherry, I will move around.
13) As a cherry, I will disappear immediately, if the Pac-man dies.

## Sprints 
### Sprints Chart
Epic Story | As a Pacman, I will run around and eat all the coins on my path, while trying to avoid being killed by ghosts.  | As a Pacman, I will be able to kill the ghosts, if I eat a big coin. | As a Pacman, I can eat a cherry to earn extra points.  | As a ghost, I can kill the Pac-man by contact, and I can reset if the Pac-man eats me.  | Grid (Map or Maze) 
--- | --- | --- | --- | --- | ---
Objects | Pacman, Pellets, Ghosts | Pacman, Power Pellets, Ghosts | Pacman, Cherry | Ghost, Pacman | Grid (Map) 
Sprint 1 | Pacman is able to move around and change directions with user inputs | Can generate big coins  | N/A | 4 Ghosts; Able to move around; Can kill the Pac-man | Simple horizonal and vertical grid 
Sprint 2 | Able to kill the ghosts with a big coin | Able to eat big coins | Can generate cherry | Enemy can move randomly; Generate new ghost when there is ghost being killed | Letter grid 
Sprint 3 | When all the coins are eaten,the player will go next level | Able to kill ghosts after eating big coins | Able to eat cherry to get extra points | Generate sound effects | More than one grid system (more than one map) 

### Sprint 1: 
Build Grid (with obstacles). 
Implement the figures, such as the Pac-man, ghosts, coins, big coins, cherry, etc.
The Pac-man can move by user input (input management).
The 4 ghosts can move automatically.  
Possibly accomplish: the user stories 1, 2, 5, 6.

### Sprint 2: 
Possibly accomplish: the user stories 1, 2, 5, 6, 3, 8, 9 

### Sprint 3: 
Sound effect  
Possibly accomplish: the user stories 4, 7, 10, 11, 12

## CRC 
### Classes: 
MainActivity: 	overall game lifecycle

Pakman:  		how The pac-man moves, interacts, and looks like

Ghost:			how the ghosts move, interact, and look like

Blocks(grid):	Part of the arcade, marks the paths of the characters

Gold:			controls the reward

Levels:			controls the hardness of the game: how many monsters, how fast do they move, etc.

UserInput:		transfrom tapping event into up/down left/right

Painter:		contrals the display
