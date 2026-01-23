package movement;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public interface PawnInterfaceCalculator extends PieceMovesCalculator{

    static List<ChessMove> possiblePawnMoves(ChessBoard board, ChessPosition start, ChessGame.TeamColor teamColor) {

        List<ChessMove> pawnMoves = new ArrayList<>();
        int direction = 0;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            direction = 1;
        }
        else {
            direction = -1;
        }




    }
}
