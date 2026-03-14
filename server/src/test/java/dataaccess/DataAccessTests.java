package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void deleteAuthToken() throws DataAccessException {
        dataAccess.createAuth(new AuthData("auth", "jimmy"));
        dataAccess.deleteAuthToken("auth");
        assertNull(dataAccess.getAuth("auth"));
    }
    @Test
    void deleteAuthTokenFail() throws DataAccessException {
        dataAccess.createAuth(new AuthData("auth1", "jimmy"));
        dataAccess.createAuth(new AuthData("auth2", "john"));
        dataAccess.deleteAuthToken("auth1");
        assertNotNull(dataAccess.getAuth("auth2"));
    }


    @Test
    void createGame() throws DataAccessException {
        dataAccess.createGame("gameTest");
        assertNotNull(dataAccess.getGame(1));
    }
    @Test
    void createGameFail() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> dataAccess.createGame(null));
    }

    @Test
    void getGame() throws DataAccessException {
        dataAccess.createGame("gameTest");
        assertNotNull(dataAccess.getGame(1));
    }
    @Test
    void getGameFail() throws DataAccessException {
        dataAccess.createGame("gameTest");
        assertNull(dataAccess.getGame(3));
    }

    @Test
    void listGames() throws DataAccessException {
        dataAccess.createGame("gameTest1");
        dataAccess.createGame("gameTest2");
        dataAccess.createGame("gameTest3");
        assertEquals(dataAccess.listGames().size(), 3);
    }
    @Test
    void listGamesFail() throws DataAccessException {
        assertEquals(dataAccess.listGames().size(), 0);
    }


    @Test
    void joinGame() throws DataAccessException {
        dataAccess.createUser(new UserData("jimmy", "abc123", "jimmy@gmail.com"));
        dataAccess.createGame("game1");
        dataAccess.joinGame("jimmy", "WHITE", 1);
        GameData game = dataAccess.getGame(1);
        assertEquals(game.whiteUsername(), "jimmy");
    }
    @Test
    void joinGameFail() throws DataAccessException {
        assertThrows(DataAccessException.class, ()->
                dataAccess.joinGame("jimmy", "WHITE", 6));
    }
}
