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

                if (outOfBounds(newPos) == true) {
                    break;
                }

                ChessPiece finPiece = board.getPiece(newPos);

                if (finPiece == null) {
                    movesList.add(new ChessMove(start, newPos, null));
                }
                else if (finPiece.getTeamColor() != teamColor) {
                    movesList.add(new ChessMove(start, newPos, null));
                    endSpot = true;
                }
                else {
                    endSpot = true;
                }
                spacesMoved++;
            }
        }
        return movesList;
    }

    static boolean outOfBounds(ChessPosition chessPos){
        int col = chessPos.getColumn();
        int row = chessPos.getRow();
        if (row >= 8 || row <= 1 || col >= 8 || col <= 1){
            return true;
        }
        else {
            return false;
        }

    }
}