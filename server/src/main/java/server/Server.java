package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MYSQLDataAccess;
import dataaccess.DataAccess;
import io.javalin.*;
import server.websocket.WebSocketHandler;
import service.*;

public class Server {
    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        DataAccess dataAccess;
        try {
            dataAccess = new MYSQLDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage());
        }
        // Register your endpoints and exception handlers here.

        //handlers that have exceptions attached
        UserHandler userHandler = new UserHandler(dataAccess);
        GameHandler gameHandler = new GameHandler(dataAccess);
        server.websocket.WebSocketHandler webSocketHandler = new WebSocketHandler();

        javalin.delete("/db", gameHandler::clear);
        javalin.post("/user", userHandler::register);
        javalin.post("/session", userHandler::login);
        javalin.delete("/session", userHandler::logout);
        javalin.post("/game", gameHandler::createGame);
        javalin.get("/game", gameHandler::listGames);
        javalin.put("/game", gameHandler::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}

