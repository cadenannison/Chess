package dataaccess;
import model.AuthData;
import model.UserData;

import java.util.HashMap;

public interface DataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuthToken(String authToken) throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;


}

