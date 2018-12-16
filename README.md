# Team Platypus

## Members:
- Kayla Phillips Sanchez
- Noyan Raquib
- Mohammed Yasir Soleja
- Karolina Szafranek
- Luka Kralj (k1764125)

## Credits:

- All background images were found online.
- No external libraries or APIs were used.



## Overall project Structure
The Main method of our project is defined in src\main\java\Main.java and this creates an instance of the GameManager class. The GameManager class if responsible for merging the functionality in front and back end. All front end components are placed in the GUI class and all back end compnents, as well as the GameManager class itself is placed in the logic folder. The GameManager only has two field, 'gameWindow' which is responsible for managing the GUI components and 'core' which is reponsible for managing the backend functions. In addition to this, the GameManager class is also responsible for saving/loading the game as this requires storing and updating both front and back end components.

## GameWindow class
The GameWindow class is the most high level class in the front-end. It is responsible for calling all subclasses to create and manage all: menus, components, animations and ActionListeners. The actual buttons and kazans are maintained in a HashMap in this class to allow for easy access and modifications by the GameManager class. The actuall Korgools are managed by a separate class which maintains thier relative positions and actions. This class also uses multithreading, the animations are handled in a separate thread to prevent the actual game board freezing mid animation. The Animation class handles all dynamic animations and movement of the korgools. We also use a wooden texture to give out game a more realistic look and feel. Our game board opens in full screen, but is fully resizable to allow the greater flexibility for the user.

### High level component overview
Our main window consists of a BorderLayout, where the centre of the Border layout holds the Kazans and the player scores. The North and south panel of the outer BorderLayout consist of a GridLayout with 9 elements, each of which contains a Hole which acts as a Container for the Korgools. The individual korgools are placed randomly within the hole, but enough space is left between the korgools as to avoid any overlap. The Menu contains the option to create a custom input window, which allows the user to load up a custom game board scenario as long as it does not exceed the maximum possible value. The menu also contains the ability to start a new game, which is loaded from newGameFile1.csv and newGameFile2.csv. The files are in csv format to allow the user to modify these in excel and perhaps choose a different starting scenario on every new game, adding another layer of customization options for the user. Finally, the menu also gives the user the option to save the game or load the most recent save for the game.

## Extension tasks implemented:
* Animations for the Korgools
* Oval shaped button with hover animations
* Resizable window
* New game functionality
* Save/Load game functionality

## Game-Manager
The game-manager class is responsible for initating all the other objects and contains the Main method. It is also responsible for merging the front end and back end functionlity.

## Game-Window
This class is responsible for managing all the GUI elements including the menu bar

## Board.java and Player.java
These classes are responsible for delivering the back-end functionality. Currently, the player chooses moves at random, but the game is fully functional and it is possible to complete a full game.

### TODO:
- Implement save game functionality
- Animate the korgools
- Use a seed for taking random values in the integration tests
- Refactor the GUI classes
