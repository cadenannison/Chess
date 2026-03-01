package server;

import dataaccess.CompMemDataAccess;
import dataaccess.DataAccess;
import io.javalin.*;
import service.ClearService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        DataAccess dataAccess = new CompMemDataAccess();
        ClearService clearService = new ClearService(dataAccess);
        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx -> {
            try {
                clearService.clear();
                ctx.status(200);
                ctx.result("{}");
            }
            catch (Exception e) {
                ctx.status(500);
                ctx.result(e.getMessage());
            }
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
