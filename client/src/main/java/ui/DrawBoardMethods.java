package ui;

import chess.ChessBoard;

public class DrawBoardMethods {
    public static String draw(ChessBoard board, boolean whitePerspective){
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
        } else {
            rowStart = 1;
            rowEnd = 9;
            rowDirection = 1;
            colStart = 8;
            colEnd = 0;
            colDirection = -1;
        }
        for (int i = rowStart; i != rowEnd; i += rowDirection){
            for (int j = colStart; j != colEnd; j += colDirection){

            }
        }
    }



}
