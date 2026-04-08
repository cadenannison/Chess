package websocket.messages;

import chess.ChessGame;

import java.nio.FloatBuffer;

public class LoadGameMessage extends ServerMessage{
    private ChessGame game;

    public ChessGame getChessGame() {
        return game;
    }

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}


