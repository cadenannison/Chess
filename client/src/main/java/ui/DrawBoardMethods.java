package ui;

import chess.*;

import java.util.Collection;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawBoardMethods {

    public static String draw(ChessBoard board, boolean whitePerspective) {
        return draw(board, whitePerspective, null);
    }

    public static String draw(ChessBoard board, boolean whitePerspective) {
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

        for (int i = rowStart; i != rowEnd; i += rowDirection) {
            boardAsString.append(i + "   ");
            for (int j = colStart; j != colEnd; j += colDirection) {
                if ((i + j) % 2 == 0) {
                    boardAsString.append(SET_BG_COLOR_DARK_GREY);
                }
                else {
                    boardAsString.append(SET_BG_COLOR_WHITE);
                }
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    boardAsString.append(EMPTY);
                }
                else {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        boardAsString.append(SET_TEXT_COLOR_RED);
                    }
                    else {
                        boardAsString.append(SET_TEXT_COLOR_BLUE);
                    }
                    boardAsString.append(returnPiece(piece));
                    boardAsString.append(RESET_TEXT_COLOR);
                }
                boardAsString.append(RESET_BG_COLOR);
            }
            boardAsString.append("  " + i + "\n");
        }
        boardAsString.append(printLetterRow(colStart, colEnd, colDirection));
        boardAsString.append(RESET_TEXT_COLOR);
        boardAsString.append(RESET_BG_COLOR);
        return boardAsString.toString();
    }
    //another draw method that accepts a spot for when i want to highlight the spaces
    public static String draw(ChessBoard board, boolean whitePerspective, Collection<ChessPosition> highlights) {
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

        for (int i = rowStart; i != rowEnd; i += rowDirection) {
            boardAsString.append(i + "   ");
            for (int j = colStart; j != colEnd; j += colDirection) {
                boolean isHighlighted = highlights != null && highlights.contains(new ChessPosition(i, j));
                if (isHighlighted) {
                    boardAsString.append(SET_BG_COLOR_GREEN);
                } else if ((i + j) % 2 == 0) {
                    boardAsString.append(SET_BG_COLOR_DARK_GREY);
                } else {
                    boardAsString.append(SET_BG_COLOR_WHITE);
                }
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    boardAsString.append(EMPTY);
                }
                else {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        boardAsString.append(SET_TEXT_COLOR_RED);
                    }
                    else {
                        boardAsString.append(SET_TEXT_COLOR_BLUE);
                    }
                    boardAsString.append(returnPiece(piece));
                    boardAsString.append(RESET_TEXT_COLOR);
                }
                boardAsString.append(RESET_BG_COLOR);
            }
            boardAsString.append("  " + i + "\n");
        }
        boardAsString.append(printLetterRow(colStart, colEnd, colDirection));
        boardAsString.append(RESET_TEXT_COLOR);
        boardAsString.append(RESET_BG_COLOR);
        return boardAsString.toString();
    }

    private static String printLetterRow(int colStart, int colEnd, int colDirection) {
        var rowString = new StringBuilder();
        rowString.append(RESET_TEXT_COLOR);
        rowString.append("    ");
        for (int i = colStart; i != colEnd; i += colDirection) {
            rowString.append(" ");
            rowString.append((char)(i + 96));
            rowString.append(" ");
        }
        rowString.append(" \n");
        return rowString.toString();
    }

    private static String returnPiece(ChessPiece piece) {
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return WHITE_KING;
            }
            else {
                return BLACK_KING;
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return WHITE_QUEEN;
            }
            else {
                return BLACK_QUEEN;
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return WHITE_ROOK;
            }
            else {
                return BLACK_ROOK;
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return WHITE_BISHOP;
            }
            else {
                return BLACK_BISHOP;
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return WHITE_PAWN;
            }
            else {
                return BLACK_PAWN;
            }
        }
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return WHITE_KNIGHT;
            }
            else {
                return BLACK_KNIGHT;
            }
        }
        return EMPTY;
    }
}