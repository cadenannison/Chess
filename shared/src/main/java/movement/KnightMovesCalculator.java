package movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator{

    public static List<ChessMove> makeMoves(ChessBoard board, ChessPosition start) {
        int[][] theDirections = {};
        return PieceMovesCalculator.possibleMoves(board, start, theDirections);
    }
}
