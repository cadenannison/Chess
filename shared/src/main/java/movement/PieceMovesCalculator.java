package movement;


import chess.*;

import java.util.ArrayList;
import java.util.List;

public interface PieceMovesCalculator {

    static List<ChessMove> possibleMoves(ChessBoard board, ChessPosition start) {
        List<ChessMove> movesList = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(start);
        ChessGame.TeamColor teamColor = currentPiece.getTeamColor();


    }
}