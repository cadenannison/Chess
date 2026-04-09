package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
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
        int gameID = incrementId++;
        // initializes the game
        GameData newGame = new GameData(gameID, null, null, gameName, new chess.ChessGame());
        gameData.put(gameID, newGame);
        return gameID;
    }

    public GameData getGame(int gameId) throws DataAccessException {
        return gameData.get(gameId);
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return gameData.values();
    }

    public void updateGame(GameData game) throws DataAccessException {
        if (!games.containsKey(game.gameID())) {
            throw new DataAccessException("Game dont exist");
        }
        games.put(game.gameID(), game);
    }

    public void joinGame(String username, String playerColor, int gameID) throws DataAccessException {
        GameData desiredGame = gameData.get(gameID);
        if (playerColor.equals("WHITE")){
            gameData.put(gameID, new GameData(desiredGame.gameID(), username,
                    desiredGame.blackUsername(), desiredGame.gameName(), desiredGame.game()));
        }
        else {
            gameData.put(gameID, new GameData(desiredGame.gameID(), desiredGame.whiteUsername(),
                    username, desiredGame.gameName(), desiredGame.game()));
        }
    }
}
