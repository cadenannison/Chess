package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class CompMemDataAccess implements DataAccess {
    private final HashMap<String, AuthData> authData = new HashMap<>(); //computer memorry for each data type
    private final HashMap<String, UserData> userData = new HashMap<>();
    private final HashMap<Integer, GameData> gameData = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int incrementId = 1;

    public void clear() {
        userData.clear();
        gameData.clear();
        authData.clear();
    }

    public void createUser(UserData user) throws DataAccessException {
        // need to check first if person exists
        if (userData.containsKey(user.username())) {
            throw new DataAccessException("This username already exists");
        }
        else {
            userData.put(user.username(), user);
        }
    }
    public UserData getUser(String username) throws DataAccessException {
        return userData.get(username);
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        authData.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authData.get(authToken);
    }

    public void deleteAuthToken(String authToken) throws DataAccessException {
        authData.remove(authToken);
    }

    public int createGame(String gameName) throws DataAccessException {
        HashMap<Integer, GameData> games = new HashMap<>();
        int gameID = incrementId++;
        GameData newGame = new GameData(gameID, null, null, gameName, new chess.ChessGame());
        games.put(gameID, newGame); // add newGame to games
        return gameID;
    }
}
