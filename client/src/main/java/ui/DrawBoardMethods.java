package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class DrawBoardMethods {
    public static String draw(ChessBoard board, boolean whitePerspective){
        var boardAsString = new StringBuilder();

        int rowStart;
        int rowEnd;
        int rowDirection;
        int colStart;
        int colEnd;
        int colDirection;

        if (whitePerspective) {
            rowStart = 8;
            rowEnd = 0;
            rowDirection = -1;
            colStart = 1;
            colEnd = 9;
            colDirection = 1;
        }
        else {
            rowStart = 1;
            rowEnd = 9;
            rowDirection = 1;
            colStart = 8;
            colEnd = 0;
            colDirection = -1;
        }
        boardAsString.append(printLetterRow(colStart, colEnd, colDirection));

        for (int i = rowStart; i != rowEnd; i += rowDirection){
            for (int j = colStart; j != colEnd; j += colDirection){
                if ((i+j) % 2 == 0){
                    boardAsString.append(SET_BG_COLOR_WHITE);
                }
                else {
                    boardAsString.append(SET_BG_COLOR_BLACK);

                }
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null){
                    boardAsString.append(EMPTY);
                }
                else {
                    boardAsString.append(returnPiece()); //need to get piece here
                }
                boardAsString.append(RESET_BG_COLOR);
            }
        }
    }

    private static String printLetterRow(int colStart, int colEnd, int colDirection) {
        var rowString = new StringBuilder();
        rowString.append("   ");
        for (int i = colStart; i != colEnd; i += colDirection) {
            rowString.append((char)(i + 96)); //easier way to b able to print out the letters in
            // the top and bottom rows
            rowString.append("  ");
        }
        rowString.append("   \n");
        return rowString.toString();
    }

    private static String returnPiece(ChessPiece piece) {

    }


}
