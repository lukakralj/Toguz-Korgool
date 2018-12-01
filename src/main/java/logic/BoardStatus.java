package logic;

/**
 * Enum class representing statuses a board can have
 */
public enum BoardStatus {
    SUCCESSFUL, //move went well but the game is not finished, next player should make a move
    MOVE_UNSUCCESSFUL,
    MOVE_IMPOSSIBLE,
    B_WON,
    W_WON,
    DRAW
}