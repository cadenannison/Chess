package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class CompMemDataAccess implements DataAccess{
    private final HashMap<String, AuthData> auths = new HashMap<>(); //computer memorry for each data type
    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public void clear() {
        users.clear();
        games.clear();
        auths.clear();
    }

}
