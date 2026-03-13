package dataacess;

import dataaccess.DataAccessException;
import dataaccess.MYSQLDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class DataAccessTests {

    private static MYSQLDataAccess dataAccess;
    @BeforeAll
    static void makeDataAccess() throws DataAccessException {
        dataAccess = new MYSQLDataAccess();
    }

    @BeforeEach
    void clearDataAccess() throws DataAccessException {
        dataAccess.clear();
    }

    @Test
    void clearTest() throws DataAccessException {

    }

    @Test
    void createUser(UserData user) throws DataAccessException {

    }

    @Test
    void getUser(String username) throws DataAccessException {

    }

    @Test
    void createAuth(AuthData auth) throws DataAccessException {

    }

    @Test
    void getAuth(String authToken) throws DataAccessException {

    }

    @Test
    void deleteAuthToken(String authToken) throws DataAccessException {

    }

    @Test
    void createGame(String gameName) throws DataAccessException {

    }

    @Test
    void getGame() throws DataAccessException {

    }

    @Test
    void listGames() throws DataAccessException {

    }

    @Test
    void joinGame(String username, String playerColor, int gameID) throws DataAccessException {

    }





}
