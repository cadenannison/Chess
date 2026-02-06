package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board;
    TeamColor teamTurn;

//    board = new ChessBoard();
//    board.resetBoard();

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        List<ChessMove> validChessMoves = new ArrayList<>();
        // get all moves for that chess piece,
        // simulate move, then check if king is in check
        // if not, then add to list
        ChessPiece[][] boardCopy = new ChessPiece[8][8];

        return validChessMoves;
    }

        /**
         * Makes a move in a chess game
         *
         * @param move chess move to perform
         * @throws InvalidMoveException if move is invalid
         */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        int rowNumber = 1;
        for (ChessPiece[] row : board.squares) { // check each row
            int columnNumber = 1;
            for (ChessPiece piece : row) { // check each col in that row
                if (piece != null && piece.getTeamColor() != teamColor) { //as long as the piece isn't null and is opposite color
                    for (ChessMove piecePossibleMove : piece.pieceMoves(board, new ChessPosition(rowNumber, columnNumber))){ // for each possible position in that piece move list
                        ChessPosition endSpot = piecePossibleMove.getEndPosition(); //find the end position of each of those possible moves
                        if (board.getPiece(endSpot) != null && board.getPiece(endSpot).getPieceType() == ChessPiece.PieceType.KING){ //then check and see if the end spot is a king with board.getpiece.getpiecetype
                            return true;
                        }
                    }
                }
                columnNumber++;
            }
            rowNumber++;
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(ChessGame.TeamColor teamColor) {
        //fix later
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return board;
    }



}
