package chess;

import movement.*;

import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myStartPosition) {
        ChessPiece piece = board.getPiece(myStartPosition);
        switch (piece)
        {
            case PieceType.KING:
                return KingMovesCalculator.makeMoves(board, myStartPosition);
                break;
            case PieceType.QUEEN:
                return QueenMovesCalculator.makeMoves(board, myStartPosition);
                break;
            case PieceType.BISHOP:
                return BishopMovesCalculator.makeMoves(board, myStartPosition);
                break;
            case PieceType.KNIGHT:
                return KnightMovesCalculator.makeMoves(board, myStartPosition);
                break;
            case PieceType.ROOK:
                return RookMovesCalculator.makeMoves(board, myStartPosition);
                break;
            case PieceType.PAWN:

                break;
        }
//        if (piece.getPieceType() == PieceType.BISHOP) {
//            return PieceMovesCalculator();
//
//        }



        return List.of();
    }
}
