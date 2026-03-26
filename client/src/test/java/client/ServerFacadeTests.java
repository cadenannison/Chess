package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        facade.clearDb(); //resets the database for all the tests
    }

    @Test
    void registerPositive() throws Exception{
        AuthData result = facade.register("username", "password", "email");
        assertNotNull(result.authToken()); //check if the register request worked and made authtoken
    }

    @Test
    void registerNegative() throws Exception {
        assertThrows(Exception.class, () -> { facade.register("user", null, "email");
        });
    }

    @Test
    void loginPositive() throws Exception {
        facade.register("username", "password", "email");
        AuthData login = facade.login("username", "password") ;
        assertNotNull(login.authToken());
    }

    @Test
    void loginFail() throws Exception {

    }





    @AfterAll
    static void stopServer() {
        server.stop();
    }




}
