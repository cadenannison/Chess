package movement;

import chess.*;

import java.util.List;

public class PawnMovesCalculator implements PawnInterfaceCalculator{

    public static List<ChessMove> makeMoves(ChessBoard board, ChessPosition start) {
        ChessPiece pawn = board.getPiece(start);

        return PawnInterfaceCalculator.possiblePawnMoves(board, start, pawn.getTeamColor());
    }
}
