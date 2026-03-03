package service;

import dataaccess.CompMemDataAccess;
import dataaccess.DataAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTests {
    private LoginService loginService;
    private RegisterService registerService;
    private DataAccess dataAccess;

    @BeforeEach
    void setup() {
        dataAccess = new CompMemDataAccess();
        loginService = new LoginService(dataAccess);
        registerService = new RegisterService(dataAccess);
    }

    @Test
    void loginSuccess() throws Exception {
        registerService.registerUser(new RegisterService.RegisterRequest("jimmy", "abc123", "jimmy@gmail.com"));
        LoginService.LoginRequest request = new LoginService.LoginRequest("jimmy", "abc123");
        LoginService.LoginResult result = loginService.loginUser(request);

        assertNotNull(result.authToken());
        assertEquals("jimmy", result.username());
    }

    @Test
    void loginWrongPassword() throws Exception {
        registerService.registerUser(new RegisterService.RegisterRequest("jimmy", "abc123", "jimmy@gmail.com"));
        LoginService.LoginRequest request = new LoginService.LoginRequest("jimmy", "BadPass");
        assertThrows(Unauthorized.class, () -> loginService.loginUser(request));
    }
}

