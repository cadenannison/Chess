package service;

import dataaccess.CompMemDataAccess;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ListGamesTests {
    private DataAccess dataAccess;
    private ListGamesService listGamesService;
    private String authToken;

    @BeforeEach
    public void setup() throws Unauthorized, AlreadyTaken, DataAccessException {
        dataAccess = new CompMemDataAccess();
        listGamesService = new ListGamesService(dataAccess);
        RegisterService registerService = new RegisterService(dataAccess);
        RegisterService.RegisterRequest request = new RegisterService.RegisterRequest
                ("jimmy", "abc123", "jimmy@email.com");
        RegisterService.RegisterResult result = registerService.registerUser(request);
        authToken = result.authToken();
        dataAccess.createGame("Game1");
        dataAccess.createGame("Game2");
        dataAccess.createGame("Game3");

    }

    @Test
    void listGamesSuccess() throws Exception {
        ListGamesService.ListGamesResult result = listGamesService.listGames(authToken);
        Assertions.assertDoesNotThrow(()-> listGamesService.listGames(authToken));
    }

    @Test
    void authFail() throws Exception {
        ListGamesService.ListGamesResult result = listGamesService.listGames(authToken);
        Assertions.assertThrows(Unauthorized.class, () -> listGamesService.listGames("NoToken"));

    }
}
