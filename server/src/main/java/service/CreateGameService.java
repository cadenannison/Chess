package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

public class CreateGameService {
    private DataAccess dataAccess;
    public CreateGameService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public record CreateGameRequest(String gameName) {}
    public record CreateGameResult(int gameID) {}

    public CreateGameResult createGame(String authToken, CreateGameRequest request)
            throws Unauthorized, BadRequest, DataAccessException {

        if (dataAccess.getAuth(authToken) == null) {
            throw new Unauthorized("Error: unauthorized");
        }
        else if (request.gameName() == null) {
            throw new BadRequest("Error: bad request");
        }
        else {
            int newGameID = dataAccess.createGame(request.gameName());
            return new CreateGameResult(newGameID);
        }
    }
}
