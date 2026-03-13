package dataacess;

import dataaccess.DataAccessException;
import dataaccess.MYSQLDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

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
        dataAccess.createUser(new UserData("jimmy", "abc123", "jimmy@gmail.com"));
        dataAccess.createAuth(new AuthData("auth", "user"));
        dataAccess.createGame("Game1");
        dataAccess.clear();
        assertNull(dataAccess.getUser("jimmy"));
        assertNull(dataAccess.getAuth("auth"));
        assertTrue(dataAccess.listGames().size() == 0);
    }

    @Test
    void createUserValid() throws DataAccessException {
        dataAccess.createUser(new UserData("jimmy", "abc123", "jimmy@gmail.com"));
        assertNotNull(dataAccess.getUser("jimmy"));
    }
    @Test
    void createUserFail() throws DataAccessException {
        dataAccess.createUser(new UserData("jimmy", "abc123", "jimmy@gmail.com"));
        assertThrows(DataAccessException.class , () ->  dataAccess.createUser(new
                UserData("jimmy", "new", "new@gmail.com")));
    }

    @Test
    void getUser() throws DataAccessException {
        dataAccess.createUser(new UserData("jimmy", "abc123", "jimmy@gmail.com"));
        assertNotNull(dataAccess.getUser("jimmy"));
    }
    @Test
    void getUserFail() throws DataAccessException {
        dataAccess.createUser(new UserData("jimmy", "abc123", "jimmy@gmail.com"));
        assertNull(dataAccess.getUser("james"));
    }

    @Test
    void createAuth() throws DataAccessException {
        dataAccess.createAuth(new AuthData("auth", "jimmy"));
        assertNotNull(dataAccess.getAuth("auth"));
    }
    @Test
    void createAuthFail() throws DataAccessException {
        dataAccess.createAuth(new AuthData("auth", "jimmy"));
        assertThrows(DataAccessException.class, () -> dataAccess.createAuth(new
                AuthData("auth", "john")));
    }

    @Test
    void getAuth() throws DataAccessException {
        dataAccess.createAuth(new AuthData("auth", "jimmy"));
        assertNotNull(dataAccess.getAuth("auth"));
    }
    @Test
    void getAuthFail() throws DataAccessException {
        assertNull(dataAccess.getAuth("auth"));
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
