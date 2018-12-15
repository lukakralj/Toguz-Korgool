package logic;

import gui.GameWindow;
import gui.Hole;
import java.util.Random;
import java.util.Set;
import java.util.*;
import java.io.*;
import java.util.List;

/**
 * Main class for the Team Platypus Agile Project
 * @author Kayla Phillips Sanchez
 * @version 14 December 2018
 *
 */
public class GameManager {

    private GameWindow gameWindow;
    private Board core;
    private AnimationController anim;
    private Random random;

    /**
     * Construct the game manager
     */
    public GameManager() {
        random = new Random();
        gameWindow = new GameWindow(this);
        AnimationController.resetController(gameWindow);
        AnimationController.instance().start();
        populateInitialBoard();
        core = new Board();
    }

    /**
     * Set the default initial state of the board (all 9's)
     */
    private void populateInitialBoard() {
        Set<String> setOfButtonsIDs = gameWindow.getButtonMap().keySet();
        for (String buttonId : setOfButtonsIDs) {
            gameWindow.populateWithKorgools(buttonId, 9);
        }
        gameWindow.resetTuzes();
        gameWindow.displayMessage("");
    }

    /**
     * Return the GUI window
     *
     * @return the game window JFrame
     */
    public GameWindow getWindow() {
        return gameWindow;
    }


    /**
     * Set the initial state of the board with given custom parameters
     *
     * @param wHoles White player's holes
     * @param bHoles Black player's holes
     * @param wTuz White player's tuz
     * @param bTuz Black player's tuz
     * @param wKazan White player's kazan
     * @param bKazan Black player's kazan
     */
    public void populateInitialBoard(int[] wHoles, int[] bHoles, int wTuz, int bTuz, int wKazan, int bKazan) {
        //updates game logic
        AnimationController.resetController(gameWindow);
        AnimationController.instance().start();
        gameWindow.displayMessage("");
        populatePlayerBoard(core.getWhitePlayer(), wHoles, wTuz, wKazan);
        populatePlayerBoard(core.getBlackPlayer(), bHoles, bTuz, bKazan);

        gameWindow.populateWithKorgools("left", bKazan);
        gameWindow.populateWithKorgools("right", wKazan);

        gameWindow.resetTuzes();

        for (int i = 0; i < 9; i++) {
            gameWindow.populateWithKorgools("W" + (i + 1), wHoles[i]);
        }

        for (int i = 0; i < 9; i++) {
            gameWindow.populateWithKorgools("B" + (9 - i), bHoles[i]);
        }

        if (wTuz != -1) {
            gameWindow.setTuz("B" + (10 - wTuz)); // set white tuz
        }
        if (bTuz != -1) {
            gameWindow.setTuz("W" + bTuz); // set black tuz
        }
        core.printBoard();
    }


    /**
     * Set the initial state of the player's board with given custom parameters
     *
     * @param player player whose board is to be initialised
     * @param holes player's holes
     * @param tuz player's tuz
     * @param kazan player's kazan
     */
    private void populatePlayerBoard(Player player, int[] holes, int tuz, int kazan) {
        player.setHoles(holes);
        tuz = (tuz == -1) ? tuz : tuz - 1;
        player.setTuz(tuz);
        player.setKazan(kazan);
    }

    /**
     * Makes a move and updates the board display.
     *  @param buttonID the string representing the buttonID
     *  @param isWhiteTurn boolean indicating whether it is the white player's turn
     */
    public void makeMove(String buttonID, boolean isWhiteTurn) {
        int hole;
        BoardStatus moveStatus;
        if (isWhiteTurn) {
            hole = Integer.parseInt(buttonID) - 1;     //subtract 1 because logic goes from 0-8, while GUI goes from 1-9
            moveStatus = core.makeMove(hole, core.getWhitePlayer(), core.getBlackPlayer());
        }
        else {
            if (core.checkIfMovePossible(core.getBlackPlayer())) {
                hole = machineChooseHole();
                moveStatus = core.makeMove(hole, core.getBlackPlayer(), core.getWhitePlayer());
            }
            else {
                endImpossibleGame(false);
                return;
            }
        }
        BoardStatus endStatus = core.checkResult();
        checkMoveStatus(moveStatus, isWhiteTurn, endStatus);
        checkEndStatus(endStatus);
    }

    /**
     * Returns a randomly chosen hole number. Makes sure it does not equal the white Tuz, and also makes sure that the chosen
     *  whole has at least 1 korgool in it.
     *
     *  @return the hole chosen by the machine
     */
    public int machineChooseHole() {
        boolean foundNumber = false;
        int hole = -1;
        while (!foundNumber) {
            hole = random.nextInt(8);  //generate random number 0-7
            if (hole >= core.getWhitePlayer().getTuz()) {         // if random number >= white Tuz
                hole += 1;                        // we increase by 1 to cover the full range 0-8 (except the tuz)
            }
            System.out.println("Bot chose hole of index " + hole);
            if (core.getBlackPlayer().getHoleAt(hole) != 0) {
                foundNumber = true;
                System.out.println("Bot CHOSE FINAL hole of index " + hole);
            }
        }
        return hole;
    }
	/**
	*	@return A printwriter object for the given filepath
	*/
    private PrintWriter getPrintWriter(String filetoOpen) throws FileNotFoundException{
        File saveFile=new File(filetoOpen);
        FileOutputStream fos=new FileOutputStream(saveFile);
        PrintWriter pw=new PrintWriter(fos);
        return pw;
    }
	
	/**
	*	Closes the given printwriter
	*/
    private void closePrintWriter(PrintWriter pw){
        pw.flush();
        pw.close();
    }

	/**
	*	Saves the current state of the board to four csv files. saveFile1.csv
	*	and saveFile2.csv are responsible for saving the GUI components, while
	*	saveFile3.csv and saveFile4.csv save the back end components
	*/
    public void saveGame(){
        try{
            PrintWriter pw = getPrintWriter("src\\main\\resources\\saveFile.csv");
            for(Map.Entry<String,Hole> entries : gameWindow.getButtonMap().entrySet()){
                pw.println(entries.getKey()+","+entries.getValue().getNumberOfKorgools()+","+entries.getValue().isTuz());
            }
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }
		
		try{
            PrintWriter pw = getPrintWriter("src\\main\\resources\\saveFile2.csv");
            for(Map.Entry<String,Hole> entries : gameWindow.getKazans().entrySet()){
                pw.println(entries.getKey()+","+entries.getValue().getNumberOfKorgools());
            }
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            PrintWriter pw = getPrintWriter("src\\main\\resources\\saveFile3.csv");
            int[] wHoles = core.getWhitePlayer().getHoles();
            int wTuz = core.getWhitePlayer().getTuz();
            int wKazan = core.getWhitePlayer().getKazan();
            insertBackEndData(pw, wHoles, wTuz, wKazan);
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            PrintWriter pw = getPrintWriter("src\\main\\resources\\saveFile4.csv");
            int[] bHoles = core.getBlackPlayer().getHoles();
            int bTuz = core.getBlackPlayer().getTuz();
            int bKazan = core.getBlackPlayer().getKazan();
            insertBackEndData(pw, bHoles, bTuz, bKazan);
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
	
	private void insertBackEndData(PrintWriter pw, int[] holes, int tuz, int kazan){
		for(int i=0;i<holes.length;i++){
            pw.println(holes[i]);
        }
        pw.println(tuz);
        pw.println(kazan);
	}

	/**
	*	The base load game method used for loading a the GUI components according to
	*	the content in saveFile1.csv and saveFile2.csv
	*/
    public void loadGame(String file1, String file2){
        AnimationController.resetController(gameWindow);
        AnimationController.instance().start();
        try{
            FileInputStream fileToOpen=new FileInputStream(new File(file1));
    
            Scanner sc=new Scanner(fileToOpen);
    
            String placeholder = "";
            gameWindow.resetTuzes();
            List<String> tuzes = new ArrayList<>();
            while(sc.hasNextLine()){
                placeholder=sc.nextLine();
                StringTokenizer st = new StringTokenizer(placeholder,",",false);
                String holeId = st.nextToken();
                Hole button = gameWindow.getButtonMap().get(holeId);
                gameWindow.populateWithKorgools(button.getName(), Integer.valueOf(st.nextToken()));
				if(st.nextToken().equals("true")){
					tuzes.add(holeId);
				}
            }
            for (String id : tuzes) {
                gameWindow.setTuz(id);
            }
            fileToOpen.close();
        }catch(Exception e){
            e.printStackTrace();
        }
		
		try{
            FileInputStream fileToOpen=new FileInputStream(new File(file2));
    
            Scanner sc=new Scanner(fileToOpen);
    
            String placeholder = "";
            while(sc.hasNextLine()){
                placeholder=sc.nextLine();
                StringTokenizer st = new StringTokenizer(placeholder,",",false);
                gameWindow.populateWithKorgools(st.nextToken(), Integer.valueOf(st.nextToken()));
            }
            fileToOpen.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

	/**
	*	An overloaded load game method while makes use of the previous method and
	*	also resets the backend according to the contents of saveFile3.csv and
	*	saveFile4.csv
	*/
    public void loadGame(String file1, String file2, String file3, String file4){
        int[] wHoles = new int[9];
        int[] bHoles = new int[9];
        int wKazan = 0;
        int bKazan = 0;
        int wTuz = -1;
        int bTuz = -1;
        loadGame(file1, file2);

        try{
            FileInputStream fileToOpen=new FileInputStream(new File(file3));
    
            Scanner sc=new Scanner(fileToOpen);
    
            for(int i=0;i<9;i++){
                wHoles[i]=Integer.valueOf(sc.nextLine());
            }

            wTuz=Integer.valueOf(sc.nextLine());

            wKazan=Integer.valueOf(sc.nextLine());

            fileToOpen.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            FileInputStream fileToOpen=new FileInputStream(new File(file4));
    
            Scanner sc=new Scanner(fileToOpen);
    
            for(int i=0;i<9;i++){
                bHoles[i]=Integer.valueOf(sc.nextLine());
            }

            bTuz=Integer.valueOf(sc.nextLine());

            bKazan=Integer.valueOf(sc.nextLine());

            fileToOpen.close();
        }catch(Exception e){
            e.printStackTrace();
        }
		populateInitialBoard(wHoles, bHoles, wTuz, bTuz, wKazan, bKazan);
    }


    /**
     * If a move is impossible, the opponent collects all of their own korgools and puts them into
     * their kazan. The kazans of the two players are then compared to determine the winner (more korgools wins)
     *
     * @param isWhiteTurn boolean indicating whether it is the white player's turn
     */
    private void endImpossibleGame(boolean isWhiteTurn) {
        String prefix = isWhiteTurn ? "B" : "W";
        for (int i = 1; i < 10; i++) {
            AnimationController.instance().addEvent(AnimationController.EMPTY_HOLE, prefix + i);
        }
        if (isWhiteTurn) {
            core.takeAllKorgools(core.getBlackPlayer());
            AnimationController.instance().addEvent(AnimationController.MOVE_KORGOOLS, AnimationController.LEFT_KAZAN, AnimationController.MOVE_ALL);
        }
        else {
            core.takeAllKorgools(core.getWhitePlayer());
            AnimationController.instance().addEvent(AnimationController.MOVE_KORGOOLS, AnimationController.RIGHT_KAZAN, AnimationController.MOVE_ALL);
        }
        BoardStatus endStatus = core.checkResultOnImpossible();
        checkEndStatus(endStatus);
    }

    /**
     *  Checks whether the game has ended, either because of a win, loss, or tie.
     *
     *  @param endStatus the endStatus of the board at this point
     */
    private void checkEndStatus(BoardStatus endStatus) {

        if (endStatus == BoardStatus.B_WON) {
            gameWindow.displayMessage("Black player won!");
            AnimationController.instance().stopAnimator();

        } else if (endStatus == BoardStatus.W_WON) {
            gameWindow.displayMessage("White player won!");
            AnimationController.instance().stopAnimator();

        } else if (endStatus == BoardStatus.DRAW) {
            gameWindow.displayMessage("It's a tie!");
            AnimationController.instance().stopAnimator();
        }
    }

    /**
     *  Checks whether the move was succesful, or if a move is impossible.
     *  @param moveStatus the status of the most recent move.
     *  @param isWhiteTurn boolean indicating whether it is the white player's turn.
     *  @param endStatus the endStatus of the board at this point
     */
    private void checkMoveStatus(BoardStatus moveStatus, boolean isWhiteTurn, BoardStatus endStatus) {
        if (moveStatus == BoardStatus.SUCCESSFUL) {  // move went well but the game is not finished, next player should make a move
            if (isWhiteTurn) {
                makeMove("", false);   // calls makeMove with hole chosen by the machine.
            }
            checkEndStatus(endStatus);
        }
        if (moveStatus == BoardStatus.MOVE_IMPOSSIBLE) {
            endImpossibleGame(isWhiteTurn);
        }
    }

    public Hole getKazanLeft() {
        return gameWindow.getKazanLeft();
    }

    public Hole getKazanRight() {
        return gameWindow.getKazanRight();
    }


    /**
     * Sets random seed to make bot's behaviour predictable.
     * Used for testing
     */
    public void setRandomSeed() {
        random.setSeed(3);
    }

    /**
     * Getter only used for testing
     * @return board of the game
     */
    public Board getCore() {
        return core;
    }
}

