package service;

import dataaccess.CompMemDataAccess;
import dataaccess.DataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTests {
    private LoginService loginService;
    private RegisterService registerService;
    private DataAccess dataAccess;
    private LogoutService logoutService;

    @BeforeEach
    void setup() {
        dataAccess = new CompMemDataAccess();
        loginService = new LoginService(dataAccess);
        registerService = new RegisterService(dataAccess);
        logoutService = new LogoutService(dataAccess);
    }

    @Test
    void LogoutSuccess() throws Exception {
        registerService.registerUser(new RegisterService.RegisterRequest("jimmy", "abc123", "jimmy@gmail.com"));
        LoginService.LoginResult result = loginService.loginUser(new LoginService.LoginRequest("jimmy", "abc123"));
        String authToken = result.authToken();
        // check that logout works and that the auth token is gone
        assertDoesNotThrow(() -> logoutService.logoutUser(authToken));
        assertNull(dataAccess.getAuth(authToken));
    }

    @Test
    void logoutBadToken() {
        assertThrows(Unauthorized.class, () -> logoutService.logoutUser("wrongAuthTOken"));
    }
}
