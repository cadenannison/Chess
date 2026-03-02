package service;

import dataaccess.CompMemDataAccess;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameTests {
    private LoginService loginService;
    private RegisterService registerService;
    private DataAccess dataAccess;
    private CreateGameService gameService;
    private String authToken;


    @BeforeEach
    void setup() throws AlreadyTaken, DataAccessException {
        dataAccess = new CompMemDataAccess();
        loginService = new LoginService(dataAccess);
        registerService = new RegisterService(dataAccess);
        gameService = new CreateGameService(dataAccess);

        registerService.registerUser(new RegisterService.RegisterRequest("jimmy", "abc123", "jimmy@gmail.com"));
        LoginService.LoginRequest request = new LoginService.LoginRequest("jimmy", "abc123");
        LoginService.LoginResult result = loginService.loginUser(request);
        authToken = result.authToken();
    }

    @Test
    void CreateGameSuccess() throws Exception {
        CreateGameService.CreateGameRequest request = new CreateGameService.CreateGameRequest("Mock Game");
        CreateGameService.CreateGameResult result = gameService.createGame(authToken, request);
        // make sure it worked
        assertTrue(result.gameID() > 0);
    }

    @Test
    void CreateGameFail() throws Exception {
        CreateGameService.CreateGameRequest request = new CreateGameService.CreateGameRequest("Bad Game");
        assertThrows(Unauthorized.class, () -> gameService.createGame("bad", request));
    }
}

