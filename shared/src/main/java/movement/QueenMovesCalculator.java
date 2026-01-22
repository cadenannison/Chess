package movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator{

    public static List<ChessMove> makeMoves(ChessBoard board, ChessPosition start) {
        int[][] theDirections = {};
        return PieceMovesCalculator.possibleMoves(board, start, theDirections);
    }
}
