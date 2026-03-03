package service;

import dataaccess.CompMemDataAccess;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JoinGameTests {
    private DataAccess dataAccess;
    private JoinGameService joinGameService;
    private String authToken;
    private int gameID;

    @BeforeEach
    public void setup() throws AlreadyTaken, DataAccessException {
        dataAccess = new CompMemDataAccess();
        joinGameService = new JoinGameService(dataAccess);

        RegisterService registerService = new RegisterService(dataAccess);
        RegisterService.RegisterRequest request = new RegisterService.RegisterRequest
                ("jimmy", "abc123", "jimmy@email.com");

        RegisterService.RegisterResult result = registerService.registerUser(request);

        authToken = result.authToken();
        gameID = dataAccess.createGame("newGame");
    }

    @Test
    void JoinGameSuccess() throws Exception {
        JoinGameService.JoinGameRequest request =
                new JoinGameService.JoinGameRequest("WHITE", gameID);
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(authToken, request));
    }

    @Test
    void JoinWrongColorGameFail() throws Exception {
        JoinGameService.JoinGameRequest firstRequest = new JoinGameService.JoinGameRequest("BLACK", gameID);
        joinGameService.joinGame(authToken, firstRequest);
        RegisterService registerService = new RegisterService(dataAccess);
        RegisterService.RegisterResult secondRequest;
        secondRequest = registerService.registerUser
                (new RegisterService.RegisterRequest("alex", "123abc", "alex@email.com"));
        JoinGameService.JoinGameRequest second = new JoinGameService.JoinGameRequest("BLACK", gameID);
        Assertions.assertThrows(AlreadyTaken.class, () -> joinGameService.joinGame(secondRequest.authToken(), second));
    }
}
