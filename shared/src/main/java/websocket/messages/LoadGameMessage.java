package websocket.messages;

import java.nio.FloatBuffer;

public class LoadGameMessage extends ServerMessage{
    private String loadGameMessage;

    public String getLoadGameMessage() {
        return loadGameMessage;
    }

    public LoadGameMessage(String errorMessage) {
        super(ServerMessageType.LOAD_GAME);
        this.loadGameMessage = loadGameMessage;
    }
}


