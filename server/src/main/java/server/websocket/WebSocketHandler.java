package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import javax.swing.*;
import java.io.IOException;
import static org.eclipse.jetty.util.PathWatcher.DirAction.ENTER;
import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.ERROR;
import static websocket.messages.ServerMessage.ServerMessageType.LOAD_GAME;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final DataAccess dataAccess;

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> gameConnect(ctx.session, userGameCommand);
                case MAKE_MOVE -> { //deserialize both parts of the object to get move positions
                    MakeMoveCommand moveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
                    makeMove(ctx.session, moveCommand);
                }
                case LEAVE -> leave(ctx.session, userGameCommand);
                case RESIGN -> resign(ctx.session, userGameCommand);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void gameConnect(Session session, UserGameCommand userGameCommand) throws IOException {
        try{
            var authentication = dataAccess.getAuth(userGameCommand.getAuthToken());
            if (authentication == null){
                throw new Exception("Error: Not Authorized");
            }
            GameData gamedata = dataAccess.getGame(userGameCommand.getGameID());
            if (gamedata == null){
                throw new Exception("Error: Bad Game ID");
            }
            int gameId = userGameCommand.getGameID();
            String username = authentication.username();
            connections.add(gameId, session);

            var loadMessage = new LoadGameMessage(gamedata.game());
            connections.sendDirectMessage(gameId, session, loadMessage);

            String message;
            if (username.equals(gamedata.whiteUsername())) {
                message = username + " joined the game as White";
            }
            else if (username.equals(gamedata.blackUsername())) {
                message = username + " joined the game as Black";
            }
            else {
                message = username + " joined the game as an observer";
            }

            NotificationMessage notification = new NotificationMessage(message);
            connections.broadcast(gameId, session, notification);
        }
        catch (Exception e) {
            errorSender(session, "Error: " + e.getMessage());
        }
    }

    private void errorSender(Session session, String message) throws IOException {
        var error = new websocket.messages.ErrorMessage(message);
        if (session.isOpen()) {
            session.getRemote().sendString(new Gson().toJson(error));
        }
    }

    private void makeMove(Session session, MakeMoveCommand makeMoveCommand) throws IOException, DataAccessException {
        var authorization = dataAccess.getAuth(makeMoveCommand.getAuthToken());
        var gameData = dataAccess.getGame(makeMoveCommand.getGameID());

        if (authorization == null) {
            errorSender(session, "Error: Not Authorized");
            return;
        }
        if (gameData == null) {
            errorSender(session, "Error: wrong gameID");
            return;
        }

        ChessGame game = gameData.game();
        String username = authorization.username();
        ChessGame.TeamColor playerColor = null;

        if (username.equals(gameData.whiteUsername())) {
            playerColor = ChessGame.TeamColor.WHITE;
        }
        else if (username.equals(gameData.blackUsername())) {
            playerColor = ChessGame.TeamColor.BLACK;
        }
        if (playerColor == null) {
            errorSender(session, "Error: Observers cant make moves");
            return;
        }

        if (game.getTeamTurn() != playerColor) {
            errorSender(session, "Error: Its not your turn");
            return;
        }
        if (game.isOver() == true){
            errorSender(session, "Error: game has already ended");
            return;
        }

        try {
            game.makeMove(makeMoveCommand.getMove());
            dataAccess.updateGame(gameData);
            connections.broadcast(gameData.gameID(), null, new LoadGameMessage(game));
            String playerMove = username + " moved to " + makeMoveCommand.getMove().toString();
            connections.broadcast(gameData.gameID(), session, new NotificationMessage(playerMove));
            ChessGame.TeamColor opponent;
            if (playerColor == ChessGame.TeamColor.WHITE) {
                opponent = ChessGame.TeamColor.BLACK;
            }
            else {
                opponent = ChessGame.TeamColor.WHITE;
            }
            if (game.isInCheckmate(opponent)) {
                game.setGameOver(true);
                dataAccess.updateGame(gameData);
                connections.broadcast(gameData.gameID(), null,
                        new NotificationMessage("Checkmate " + username + " wins the game!"));
            }
            else if (game.isInCheck(opponent)) {
                connections.broadcast(gameData.gameID(), null,
                        new NotificationMessage("Check :)"));
            }
        }
        catch (Exception ex){
            errorSender(session, "Error: " + ex.getMessage());
        }
    }

    private void resign(Session session, UserGameCommand userGameCommand) throws IOException {

    }

    private void leave(Session session, UserGameCommand userGameCommand) throws IOException {

    }

    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


}