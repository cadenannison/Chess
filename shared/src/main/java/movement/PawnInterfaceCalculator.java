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
        int row = start.getRow();;
        int col = start.getColumn();
        ChessPosition pawnPos = new ChessPosition(row + direction, col);

        if (PieceMovesCalculator.outOfBounds(pawnPos) == false && board.getPiece(pawnPos) == null) {
            promotionCheck(board, start, pawnMoves, pawnPos, teamColor);
        }

        if (start.getRow() == 2 && teamColor == ChessGame.TeamColor.WHITE) {
            ChessPosition goTwo = new ChessPosition(row+2, col);

            if (PieceMovesCalculator.outOfBounds(goTwo) == false && board.getPiece(goTwo) == null && board.getPiece(pawnPos) == null) {
                pawnMoves.add(new ChessMove(start, goTwo, null));
            }
        }

        if (start.getRow() == 7 && teamColor == ChessGame.TeamColor.BLACK) {
            ChessPosition goTwo = new ChessPosition(row-2, col);

            if (PieceMovesCalculator.outOfBounds(goTwo) == false && board.getPiece(goTwo) == null && board.getPiece(pawnPos) == null) {
                pawnMoves.add(new ChessMove(start, goTwo, null));
            }
        }

        int[][] pawnDiagonalMove = {{direction,1}, {direction,-1}};

        for (int[] diagonal : pawnDiagonalMove) {
            ChessPosition pawnDiagonalCheck = new ChessPosition(row+diagonal[0], col+diagonal[1]);

            if (PieceMovesCalculator.outOfBounds(pawnDiagonalCheck) == false) {
                ChessPiece pawnAttack = board.getPiece(pawnDiagonalCheck);
                if (pawnAttack != null && pawnAttack.getTeamColor() != teamColor){
                    promotionCheck(board, start, pawnMoves, pawnDiagonalCheck, teamColor);
                }
            }
        }

        return pawnMoves;
    }

    private static void promotionCheck(ChessBoard board, ChessPosition start, List<ChessMove> promotionSpot, ChessPosition end, ChessGame.TeamColor teamColor) {
        int promote;

        if (teamColor == ChessGame.TeamColor.BLACK) {
            promote = 1;
        }
        else {
            promote = 8;
        }
        if (promote == end.getRow()) {
            promotionSpot.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
            promotionSpot.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
            promotionSpot.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
            promotionSpot.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        }
        else {
            promotionSpot.add(new ChessMove(start, end, null));
        }
    }

}
