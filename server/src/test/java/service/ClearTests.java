package service;

import dataaccess.CompMemDataAccess;
import dataaccess.DataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTests {
    private ClearService clearService;
    private CreateGameService createGameService;
    private RegisterService registerService;
    private DataAccess dataAccess;

    @BeforeEach
    void setup() {
        dataAccess = new CompMemDataAccess();
        clearService = new ClearService(dataAccess);
        createGameService = new CreateGameService(dataAccess);
        registerService = new RegisterService(dataAccess);
    }

    @Test
    void ClearTest() throws Exception {
        RegisterService.RegisterResult result = registerService.registerUser
                (new RegisterService.RegisterRequest("jimmy", "abc123", "jimmy@gmail.com"));
        createGameService.createGame(result.authToken(),
                new CreateGameService.CreateGameRequest("Game"));
        clearService.clear();
        assertNull(dataAccess.getUser("jimmy"));
        assertNull(dataAccess.getAuth(result.authToken()));
        assertTrue(dataAccess.listGames().size() == 0);
    }
}
