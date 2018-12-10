package logic;

import gui.GameWindow;
import gui.Hole;
import gui.OvalButton;
import java.util.Random;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.util.List;

import logic.AnimationController;
import logic.GameManager;

/**
 * Main class for the Team Platypus Agile Project
 *
 */
public class GameManager {

    private GameWindow gameWindow;
    private Board core;
    private HashMap<String, Hole> buttonMap;
    private HashMap<String, Hole> kazans;

    /**
     * Construct the game manager
     */
    public GameManager() {
        gameWindow = new GameWindow(this);
        AnimationController.resetController(gameWindow);
        AnimationController.instance().start();
        populateInitialBoard();
        core = new Board();
        buttonMap = gameWindow.getButtonMap();
        kazans = gameWindow.getKazans();
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
            gameWindow.setTuz("B" + (9 - wTuz)); // set white tuz
        }
        if (bTuz != -1) {
            gameWindow.setTuz("W" + bTuz); // set black tuz
        }
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
        player.setTuz(tuz - 1);
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
    private int machineChooseHole() {
        Random random = new Random();
        boolean foundNumber = false;
        int hole = -1;
        while (!foundNumber) {
            hole = random.nextInt(8);  //generate random number 0-7
            if (hole >= core.getWhitePlayer().getTuz()) {         // if random number >= white Tuz
                hole += 1;                        // we increase by 1 to cover the full range 0-8 (except the tuz)
            }
            if (core.getBlackPlayer().getHoleAt(hole) != 0) {
                foundNumber = true;
            }
        }
        return hole;
    }

    private PrintWriter getPrintWriter(String filetoOpen) throws FileNotFoundException{
        File saveFile=new File(filetoOpen);
        FileOutputStream fos=new FileOutputStream(saveFile);
        PrintWriter pw=new PrintWriter(fos);
        return pw;
    }

    private void closePrintWriter(PrintWriter pw){
        pw.flush();
        pw.close();
    }

    public void saveGame(){
        try{
            PrintWriter pw = getPrintWriter("src\\main\\resources\\saveFile.csv");
            for(Map.Entry<String,Hole> entries :buttonMap.entrySet()){
                pw.println(entries.getKey()+","+entries.getValue().getNumberOfKorgools()+","+entries.getValue().isTuz());
            }
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }
		
		try{
            PrintWriter pw = getPrintWriter("src\\main\\resources\\saveFile2.csv");
            for(Map.Entry<String,Hole> entries :kazans.entrySet()){
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
            for(int i=0;i<wHoles.length;i++){
                pw.println(wHoles[i]);
            }
            pw.println(wTuz);
            pw.println(wKazan);
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            PrintWriter pw = getPrintWriter("src\\main\\resources\\saveFile4.csv");
            int[] bHoles = core.getBlackPlayer().getHoles();
            int bTuz = core.getBlackPlayer().getTuz();
            int bKazan = core.getBlackPlayer().getKazan();
            for(int i=0;i<bHoles.length;i++){
                pw.println(bHoles[i]);
            }
            pw.println(bTuz);
            pw.println(bKazan);
            closePrintWriter(pw);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadGame(String file1, String file2){
        AnimationController.resetController(gameWindow);
        AnimationController.instance().start();
        try{
            File toRead=new File(file1);
            FileInputStream fis=new FileInputStream(toRead);
    
            Scanner sc=new Scanner(fis);
    
            String placeholder = "";
            gameWindow.resetTuzes();
            List<String> tuzes = new ArrayList<>();
            while(sc.hasNextLine()){
                placeholder=sc.nextLine();
                StringTokenizer st = new StringTokenizer(placeholder,",",false);
                String holeId = st.nextToken();
                Hole button = buttonMap.get(holeId);
                gameWindow.populateWithKorgools(button.getName(), Integer.valueOf(st.nextToken()));
				if(st.nextToken().equals("true")){
					tuzes.add(holeId);
				}
            }
            for (String id : tuzes) {
                gameWindow.setTuz(id);
            }
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
		
		try{
            File toRead=new File(file2);
            FileInputStream fis=new FileInputStream(toRead);
    
            Scanner sc=new Scanner(fis);
    
            String placeholder = "";
            while(sc.hasNextLine()){
                placeholder=sc.nextLine();
                StringTokenizer st = new StringTokenizer(placeholder,",",false);
                gameWindow.populateWithKorgools(st.nextToken(), Integer.valueOf(st.nextToken()));
            }
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadGame(String file1, String file2, String file3, String file4){
        int[] wHoles = new int[9];
        int[] bHoles = new int[9];
        int wKazan = 0;
        int bKazan = 0;
        int wTuz = -1;
        int bTuz = -1;
        loadGame(file1, file2);

        try{
            File toRead=new File(file3);
            FileInputStream fis=new FileInputStream(toRead);
    
            Scanner sc=new Scanner(fis);
    
            for(int i=0;i<9;i++){
                wHoles[i]=Integer.valueOf(sc.nextLine());
            }

            wTuz=Integer.valueOf(sc.nextLine());

            wKazan=Integer.valueOf(sc.nextLine());

            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            File toRead=new File(file4);
            FileInputStream fis=new FileInputStream(toRead);
    
            Scanner sc=new Scanner(fis);
    
            for(int i=0;i<9;i++){
                bHoles[i]=Integer.valueOf(sc.nextLine());
            }

            bTuz=Integer.valueOf(sc.nextLine());

            bKazan=Integer.valueOf(sc.nextLine());

            fis.close();
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
        if (isWhiteTurn) {
            core.getAllKorgools(core.getBlackPlayer());
        }
        else {
            core.getAllKorgools(core.getWhitePlayer());
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
}

