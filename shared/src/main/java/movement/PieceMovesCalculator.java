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
            int spacesMoved = 1;

            while (!endSpot){
                int row = start.getRow();
                int col = start.getColumn();
                row += eachDirection[1] * spacesMoved;
                col += eachDirection[0] * spacesMoved;
                ChessPosition newPos = new ChessPosition(row, col);

                if () {
                    ;
                }
                spacesMoved++;
                movesList += directions;

            }
        }
    }
}