package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class CompMemDataAccess implements DataAccess {
    private final HashMap<String, AuthData> authData = new HashMap<>(); //computer memorry for each data type
    private final HashMap<String, UserData> userData = new HashMap<>();
    private final HashMap<Integer, GameData> gameData = new HashMap<>();

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
}
