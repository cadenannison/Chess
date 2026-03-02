package service;
import dataaccess.CompMemDataAccess;
import org.junit.jupiter.api.*;
import service.AlreadyTaken;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTests {
    private RegisterService service;
    @BeforeEach
    void setup() {
        service = new RegisterService(new CompMemDataAccess());
    }

    @Test
    void regSuccess() throws Exception {
        RegisterService.RegisterRequest request = new
                RegisterService.RegisterRequest("jimmy", "abc123", "email@gmail.com");
        RegisterService.RegisterResult result = service.registerUser(request);
        assertNotNull(result.authToken());
        assertEquals("jimmy", result.username());
    }

    @Test
    void userAlreadyExists() throws Exception {
        RegisterService.RegisterRequest request = new RegisterService.RegisterRequest("jimmy", "abc123", "email@gmail.com");
        service.registerUser(request);
        assertThrows(AlreadyTaken.class, () -> service.registerUser(new
                RegisterService.RegisterRequest("jimmy", "abc123", "email@gmail.com"))
        );
    }
}
