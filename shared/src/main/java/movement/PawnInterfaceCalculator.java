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
            pawnMoves.add(new ChessMove(start, pawnPos, null));
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
                    pawnMoves.add(new ChessMove(start, pawnDiagonalCheck, null));
                }
            }
        }

        if (pawn)

        return pawnMoves;
    }




}
