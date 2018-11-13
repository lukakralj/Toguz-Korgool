
/**
 * Class representing a board of the game.
 *
 * @author Luka Kralj, Karolina Szafranek
 * @version 13 November 2018
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
     * Represents making a move on the board
     * @param hole The hole the move starts with
     * @param player player whose turn it is this move
     * @param opponent other player
     * @return Status of the board
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
                if (hole == 8) {
                    switchBoards();
                    hole = 0;
                }
                else {
                    hole++;
                }
            }
        }

        return endMove(hole - 1, player, opponent, currentBoard);

//        if (isWhiteCurrent) {
//            korgools = holesW[hole];
//            holesW[hole] = 0;
//        }
//        else {
//            korgools = holesB[hole];
//            holesB[hole] = 0;
//        }
//
//        if (korgools == 0) {
//            return BoardStatus.MOVE_UNSUCCESSFUL;
//        }
//        if (korgools == 1) {
//            if (hole == 8) {
//                hole = 0;
//                isWhiteTurn != isWhiteTurn;
//            }
//            else {
//                hole++;
//            }
//
//            if (isWhiteCurrent) {
//                holesW[hole] += 1;
//            }
//            else {
//                holesB[hole] += 1;
//            }
//
//        } else {
//            for (int i = korgools; i > 0; --i) {
//
//                if (isWhiteCurrent) {
//                    holesW[hole] += 1;
//                }
//                else {
//                    holesB[hole] += 1;
//                }
//                if (i == 1) {
//                    break;
//                }
//                if (hole == 8) {
//                    isWhiteCurrent = !isWhiteCurrent;
//                    hole = 0;
//                }
//                else {
//                    hole++;
//                }
//            }
//        }
//        return endMove(hole, isWhiteCurrent, isWhiteTurn);
    }

    /**
     * Helper function implementing ending conditions of a move
     * @param lastHoleFilled The index of hole that was filled last
     * @param player player whose turn it is this move
     * @param opponent other player
     * @param currentBoard player whose board we are on
     * @return Status of the board
     */
    private BoardStatus endMove(int lastHoleFilled, Player player, Player opponent, Player currentBoard) {
        if(currentBoard.equals(opponent) && lastHoleFilled != player.getTuz()) {
            if (opponent.getHoleAt(lastHoleFilled) == 3 && player.getTuz() == -1 && opponent.getTuz() != lastHoleFilled && lastHoleFilled != 8) {
                player.setTuz(lastHoleFilled);
            } else if (opponent.getHoleAt(lastHoleFilled) % 2 ==0){
                player.setKazan(player.getKazan() + opponent.getHoleAt(lastHoleFilled));
                opponent.setHole(lastHoleFilled, 0);
            }
        }

//        if (isWhiteTurn && !isOnWhiteSide && lastHoleFilled != tuzW) {
//            if (holesB[lastHoleFilled] == 3 && tuzW == -1 && tuzB != lastHoleFilled && lastHoleFilled !=8) {
//                tuzW = lastHoleFilled;
//            }
//            else if (holesB[lastHoleFilled] % 2 == 0) {
//                kazanW += holesB[lastHoleFilled];
//                holesB[lastHoleFilled] = 0;
//            }
//        }
//        else if (!isWhiteTurn && isOnWhiteSide && lastHoleFilled != tuzB) {
//            if (holesW[lastHoleFilled] == 3 && tuzB == -1 && tuzW != lastHoleFilled && lastHoleFilled !=8) {
//                tuzB = lastHoleFilled;
//            }
//            else if (holesW[lastHoleFilled] % 2 == 0) {
//                kazanB += holesW[lastHoleFilled];
//                holesW[lastHoleFilled] = 0;
//            }
//        }

        if (player.getTuz() != -1) {
            player.setKazan(player.getKazan() + opponent.getHoleAt(player.getTuz()));
        }

        if (opponent.getTuz() != -1) {
            opponent.setKazan(opponent.getKazan() + player.getHoleAt(opponent.getTuz()));
        }


        return checkResult();

    }

    /**
     * Helper function checking status of the board after a move
     * @return Status of the board
     */
    private BoardStatus checkResult() {
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
     * Helper function determines whether given player can make a move,
     * ie if there are any non-empty hole on player's board
     * @param currentPlayer player whose turn it is this move
     * @return true if move is possible, false otherwise
     */
    private boolean checkIfMovePossible(Player currentPlayer) {
        for (int hole : currentPlayer.getHoles()) {
            if (hole != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function switching that toggles current board between player's and opponent's board
     */
    private void switchBoards() {
        Player temp = currentBoard;
        currentBoard = otherBoard;
        otherBoard = temp;
    }


    // ===================================
    //   Used for testing private method
    // ===================================

    public BoardStatus testCheckResult() {
        return checkResult();
    }

    public BoardStatus testEndMove(int lastHoleFilled, Player player, Player opponent, Player currentBoard) {
        return endMove(lastHoleFilled, player, opponent, currentBoard);
    }

    public boolean testCheckIfMovePossible(Player currentPlayer) {
        return checkIfMovePossible(currentPlayer);
    }

//    public BoardStatus testCheckResult() {
//        return checkResult();
//    }
//
//    public BoardStatus testEndMove(int lastHoleFilled, boolean isOnWhiteSide, boolean isWhiteTurn) {
//        return endMove(lastHoleFilled, isOnWhiteSide, isWhiteTurn);
//    }
//
//    public boolean testCheckIfMovePossible(boolean isWhiteTurn) {
//        return checkIfMovePossible(isWhiteTurn);
//    }
}

/**
 * Enum class representing statuses a board can have
 */
enum BoardStatus {
    SUCCESSFUL, //move went well but the game is not finished, next player should make a move
    MOVE_UNSUCCESSFUL,
    MOVE_IMPOSSIBLE,
    B_WON,
    W_WON,
    DRAW
}

