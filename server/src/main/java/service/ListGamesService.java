package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public class ListGamesService {
    private DataAccess dataAccess;

    public ListGamesService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public record ListGamesResult(Collection<GameData> games) {}

    public ListGamesResult listGames(String authToken) throws DataAccessException, Unauthorized {
        if (dataAccess.getAuth(authToken) == null){
            throw new Unauthorized("Error: unauthorized");
        }
        else {
            return new ListGamesResult(dataAccess.listGames());
        }

    }


}
