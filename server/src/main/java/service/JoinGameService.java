package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

public class JoinGameService {
    private DataAccess dataAccess;

    public JoinGameService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public record JoinGameRequest(String playerColor, int gameID){}

    public void joinGame(String authToken, JoinGameRequest request)
            throws Unauthorized, BadRequest, DataAccessException, AlreadyTaken {
        GameData currentGame = dataAccess.getGame(request.gameID());
        if (dataAccess.getAuth(authToken) == null){
            throw new Unauthorized("Error: unauthorized");
        }
        else if (currentGame == null) {
            throw new BadRequest("Error: bad request");
        }
        else if (request.playerColor() == null) {
            return;
        }

        String username = dataAccess.getAuth(authToken).username();

        if (request.playerColor().equals("WHITE")) {
            if (currentGame.whiteUsername() != null) {
                throw new AlreadyTaken("Error: already taken");
            }
            else {
                dataAccess.joinGame(username, "WHITE", request.gameID());
            }
        }
        else if (request.playerColor().equals("BLACK")) {
            if (currentGame.blackUsername() != null) {
                throw new AlreadyTaken("Error: already taken");
            }
            else {
                dataAccess.joinGame(username, "BLACK", request.gameID());
            }
        }
        else {
            throw new BadRequest("Error: bad request");
        }
    }
}
