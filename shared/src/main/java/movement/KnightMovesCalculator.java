package movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator{

    public static List<ChessMove> makeMoves(ChessBoard board, ChessPosition start) {
        int[][] theDirections = {{2,1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}, {1,-2}, {2,-1}};
        return PieceMovesCalculator.possibleMoves(board, start, theDirections);
    }
}
