package websocket.messages;

import chess.ChessMove;
import websocket.commands.UserGameCommand;

public class ErrorMessage extends ServerMessage{
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
}
