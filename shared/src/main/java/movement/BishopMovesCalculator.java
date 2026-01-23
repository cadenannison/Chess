package movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{

    public static List<ChessMove> makeMoves(ChessBoard board, ChessPosition start) {
        int[][] theDirections = {{1,1}, {-1,1}, {-1,-1}, {1,-1}};
        return PieceMovesCalculator.possibleMoves(board, start, theDirections, true);
    }

}
