package movement;


import chess.*;

import java.util.ArrayList;
import java.util.List;

public interface PieceMovesCalculator {

    static List<ChessMove> possibleMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        List<ChessMove> movesList = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(start);
        ChessGame.TeamColor teamColor = currentPiece.getTeamColor();

        for (int[] eachDirection : directions){
            boolean endSpot = false;
            int spacesMoved = 0;

            while (!endSpot){
                spacesMoved++;

                if (ChessBoard.(eachDirection+spacesMoved)) {
                    break;
                }
                movesList += directions;

            }
        }
    }
}