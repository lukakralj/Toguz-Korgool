# Main Repository for Agile Project

## Overall project Structure
The Main method of our project is defined in src\main\java\Main.java and this creates an instance of the GameManager class. The GameManager class if responsible for merging the functionality in front and back end. All front end components are placed in the GUI class and all back end compnents, as well as the GameManager class itself is placed in the logic folder. The GameManager only has two field, 'gameWindow' which is responsible for managing the GUI components and 'core' which is reponsible for managing the backend functions. In addition to this, the GameManager class is also responsible for saving/loading the game as this requires storing and updating both front and back end components.

## GameWindow class
The GameWindow class is the most high level class in the front-end. It is responsible for calling all subclasses to create and manage all: menus, components, animations and ActionListeners. The actual buttons and kazans are maintained in a HashMap in this class to allow for easy access and modifications by the GameManager class. The actuall Korgools are managed by a separate class which maintains thier relative positions and actions. This class also uses multithreading, the animations are handled in a separate thread to prevent the actual game board freezing mid animation. The Animation class handles all dynamic animations and movement of the korgools. We also use a wooden texture to give out game a more realistic look and feel. Our game board opens in full screen, but is fully resizable to allow the greater flexibility for the user.

## Extension tasks implemented:
* Animations for the Korgools
* Oval shaped button with hover animations
* Resizable window
* New game functionality
* Save/Load game functionality
