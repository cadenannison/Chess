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

        ChessGame.TeamColor playerColor = getTeamColor(gameData.gameID(), session,
                authorization.authToken(), username, gameData);
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
            System.out.println("Saved game, teamTurn is now: " + game.getTeamTurn());
            connections.broadcast(gameData.gameID(), null, new LoadGameMessage(game));

            chess.ChessMove move = makeMoveCommand.getMove();
            String from = positionConverted(move.getStartPosition());
            String to = positionConverted(move.getEndPosition());
            String playerMove = username + " moved " + from + " to " + to;

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
                        new NotificationMessage("Checkmate. " + username + " wins the game!"));
            }
            else if (game.isInCheck(opponent)) {
                String opUser;

                if (opponent == ChessGame.TeamColor.WHITE){
                    opUser = gameData.whiteUsername();
                }
                else{
                    opUser = gameData.blackUsername();
                }
                if (opUser == null){
                    opUser = "opponent";
                }

                connections.broadcast(gameData.gameID(), null,
                        new NotificationMessage(username + " put " + opUser + " in check!!"));
            }
        }
        catch (Exception ex){
            String msg;
                    if (ex.getMessage() != null) {
                        msg = ex.getMessage();
                    }
                    else {
                        msg = "Invalid move";
                    }
            errorSender(session, msg);
        }
    }

    private String positionConverted(chess.ChessPosition pos) {
        char col = (char) ('a' -1 + pos.getColumn());
        return "" + col + pos.getRow();
    }

    private void resign(Session session, UserGameCommand userGameCommand) throws IOException {
        try{
            var authorization = dataAccess.getAuth(userGameCommand.getAuthToken());
            var gameData = dataAccess.getGame(userGameCommand.getGameID());

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

            ChessGame.TeamColor playerColor = getTeamColor(gameData.gameID(), session,
                    userGameCommand.getAuthToken(), username, gameData);
            if (playerColor == null) {
                errorSender(session, "Error: Observers cant resign");
                return;
            }
            if (game.isOver()) {
                errorSender(session, "Error: The game has already ended.");
                return;
            }

            game.setGameOver(true);
            dataAccess.updateGame(gameData);

            String message = username + " has resigned and now the game is over.";
            connections.broadcast(gameData.gameID(), null, new NotificationMessage(message));


        }
        catch (Exception ex) {
            errorSender(session, "Error: " + ex.getMessage());
        }
    }

    private ChessGame.TeamColor getTeamColor(int gameId, Session session, String authToken,
                                             String username, GameData gameData) throws IOException {
        ChessGame.TeamColor playerColor = null;
        if (username.equals(gameData.whiteUsername())) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            playerColor = ChessGame.TeamColor.BLACK;
        }
        return playerColor;
    }

    private void leave(Session session, UserGameCommand userGameCommand) throws IOException {
        try {
            var authorization = dataAccess.getAuth(userGameCommand.getAuthToken());
            var gameData = dataAccess.getGame(userGameCommand.getGameID());

            if (authorization == null) {
                errorSender(session, "Error: Not Authorized");
                return;
            }
            if (gameData == null) {
                errorSender(session, "Error: wrong gameID");
                return;
            }
            String username = authorization.username();
            int gameId = gameData.gameID();

            connections.remove(gameId, session);

            String white = gameData.whiteUsername();
            String black = gameData.blackUsername();
            if (username.equals(white)) {
                white = null;
            }
            else if (username.equals(black)) {
                black = null;
            }
            GameData updatedGame = new GameData(gameId, white, black, gameData.gameName(), gameData.game());
            dataAccess.updateGame(updatedGame);

            String notification = username + " has left the game.";
            connections.broadcast(gameId, session, new NotificationMessage(notification));
        }
        catch (Exception ex){
            errorSender(session, "error: " + ex.getMessage());
        }
    }

    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }



}