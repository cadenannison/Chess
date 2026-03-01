package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class CompMemDataAccess implements DataAccess{
    private final HashMap<String, AuthData> authData = new HashMap<>(); //computer memorry for each data type
    private final HashMap<String, UserData> userData = new HashMap<>();
    private final HashMap<Integer, GameData> gameData = new HashMap<>();

    public void clear() {
        userData.clear();
        gameData.clear();
        authData.clear();
    }

}
