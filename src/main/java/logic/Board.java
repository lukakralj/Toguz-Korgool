package logic;

/**
 * Class representing a board of the game.
 *
 * @author Luka Kralj, Karolina Szafranek
 * @version 26 November 2018
 */
public class Board {
    private Player whitePlayer;
    private Player blackPlayer;

    private Player currentBoard; // side of the board we're currently on
    private Player otherBoard;  // other side of the board

    public Board() {
        whitePlayer = new Player();
        blackPlayer = new Player();
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * Helper function, prints state of the board
     */
    public void printBoard() {
        System.out.print("\nBlack: ");
        blackPlayer.printHolesReversed();
        System.out.print("\nWhite: ");
        whitePlayer.printHoles();
        System.out.println("\nkazanB: " + blackPlayer.getKazan());
        System.out.println("kazanW: " + whitePlayer.getKazan());
        System.out.println("tuzB: " + blackPlayer.getTuz());
        System.out.println("tuzW: " + whitePlayer.getTuz());
    }


    /**
     * Represents making a move on the board.
     *
     * @param hole the hole the move starts with
     * @param player player whose turn it is this move
     * @param opponent other player
     * @return status of the board
     */
    public BoardStatus makeMove(int hole, Player player, Player opponent) {
        if (!checkIfMovePossible(player)) {
            return BoardStatus.MOVE_IMPOSSIBLE;
        }

        currentBoard = player;
        otherBoard = opponent;

        int korgools = player.getHoleAt(hole);
        player.setHole(hole, 0);

        if (korgools == 0) {
            return BoardStatus.MOVE_UNSUCCESSFUL;
        }

        if (korgools == 1) {
            if (hole == 8) {
                switchBoards();
                hole = 0;
            } else {
                hole++;
            }
            currentBoard.incrementHole(hole);
        } else {
            for (int i = korgools; i > 0; --i) {
                currentBoard.incrementHole(hole);
                if (i == 1) {
                    break;
                }
                if (hole == 8) {
                    switchBoards();
                    hole = 0;
                }
                else {
                    hole++;
                }
            }
        }

        return endMove(hole, player, opponent, currentBoard);
    }

    /**
     * Helper function implementing ending conditions of a move.
     *
     * @param lastHoleFilled the index of hole that was filled last
     * @param player player whose turn it is this move
     * @param opponent other player
     * @param currentBoard player whose board we are on
     * @return status of the board
     */
    private BoardStatus endMove(int lastHoleFilled, Player player, Player opponent, Player currentBoard) {
        if(currentBoard == opponent && lastHoleFilled != player.getTuz()) {
            if (opponent.getHoleAt(lastHoleFilled) == 3 && player.getTuz() == -1 && opponent.getTuz() != lastHoleFilled && lastHoleFilled != 8) {
                player.setTuz(lastHoleFilled);
            } else if (opponent.getHoleAt(lastHoleFilled) % 2 == 0){
                player.setKazan(player.getKazan() + opponent.getHoleAt(lastHoleFilled));
                opponent.setHole(lastHoleFilled, 0);
            }

        }

        if (player.getTuz() > -1) {
            player.setKazan(player.getKazan() + opponent.getHoleAt(player.getTuz()));
            opponent.setHole(player.getTuz(), 0);
        }

        if (opponent.getTuz() > -1) {
            opponent.setKazan(opponent.getKazan() + player.getHoleAt(opponent.getTuz()));
            player.setHole(opponent.getTuz(), 0);
        }

        return checkResult();
    }

    /**
     * Helper function checking status of the board after a move.
     *
     * @return status of the board
     */
    public BoardStatus checkResult() {
        if (whitePlayer.getKazan() >= 82) {
            return BoardStatus.W_WON;
        } else if (blackPlayer.getKazan() >= 82) {
            return BoardStatus.B_WON;
        } else if (whitePlayer.getKazan() == 81 && blackPlayer.getKazan() == 81) {
            return BoardStatus.DRAW;
        } else {
            return BoardStatus.SUCCESSFUL;
        }
    }

    /**
     * Helper function checking status of the board after a move has been declared impossible
     * @return Status of the board
     */
    public BoardStatus checkResultOnImpossible() {
        if (whitePlayer.getKazan() > blackPlayer.getKazan()) {
            return BoardStatus.W_WON;
        }
        else if (blackPlayer.getKazan() > whitePlayer.getKazan()) {
            return BoardStatus.B_WON;
        }
        else {
            return BoardStatus.DRAW;
        }
    }

    /**
     * Helper function determines whether given player can make a move,
     * ie if there are any non-empty holes on player's board.
     *
     * @param currentPlayer player whose turn it is this move
     * @return true if move is possible, false otherwise
     */
    public boolean checkIfMovePossible(Player currentPlayer) {
        for (int hole : currentPlayer.getHoles()) {
            if (hole != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function that toggles current board between player's and opponent's board.
     */
    private void switchBoards() {
        Player temp = currentBoard;
        currentBoard = otherBoard;
        otherBoard = temp;
    }
    

     /**
     * Add all the korgools in the player's holes into the player's kazaan.
     * Sets player's holes to 0.
     */
     public void getAllKorgools(Player player) {
        for (int valueInHole : player.getHoles()) {
            player.setKazan(player.getKazan() + valueInHole);
        }
        player.reset();
    }


    // ===================================
    //   Used for testing private method
    // ===================================

    public BoardStatus testCheckResult() {
        return checkResult();
    }
    
    public BoardStatus testCheckResultOnImpossible() {
        return checkResultOnImpossible();
    }

    public BoardStatus testEndMove(int lastHoleFilled, Player player, Player opponent, Player currentBoard) {
        return endMove(lastHoleFilled, player, opponent, currentBoard);
    }

    public boolean testCheckIfMovePossible(Player currentPlayer) {
        return checkIfMovePossible(currentPlayer);
    }

}
