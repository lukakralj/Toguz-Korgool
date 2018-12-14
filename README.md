# Main Repository for Agile Project

## GUI prototype
Currently, the GUI has 9 oval shaped button on the top and bottom to represents the 9 circles for the player and the AI. It also has an nearly complete menu with some basic options. The Text Area in the center represents the main play area and if you click on a button, it will append text to the Text Area in a new line. The front and back end have recently been merged and some basic integration tests have been implemented. The GameWindow class is also responsible for saving/loading the game.

## Overall project Structure
The Main method of our project is defined in src\main\java\Main.java and this creates an instance of the GameManager class. The GameManager class if responsible for merging the functionality in front and back end. All front end components are placed in the GUI class and all back end compnents, as well as the GameManager class itself is placed in the logic folder. The GameManager only has two field, 'gameWindow' which is responsible for managing the GUI components and 'core' which is reponsible for managing the backend functions
