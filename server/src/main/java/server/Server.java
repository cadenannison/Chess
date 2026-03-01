package server;

import com.google.gson.Gson;
import dataaccess.CompMemDataAccess;
import dataaccess.DataAccess;
import io.javalin.*;
import service.AlreadyTaken;
import service.BadRequest;
import service.ClearService;
import service.RegisterService;

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
                ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
            }
        });

        RegisterService registerService = new RegisterService(dataAccess);
        Gson gson = new Gson();
        javalin.post("/user", ctx -> {
            try {
                RegisterService.RegisterRequest request = gson.fromJson(ctx.body(), RegisterService.RegisterRequest.class);
                RegisterService.RegisterResult result = registerService.registerUser(request);
                ctx.status(200);
                ctx.json(gson.toJson(result)); // bug wihtout gson.toJson
            } catch (BadRequest e) {
                ctx.status(400);
                ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
            } catch (AlreadyTaken e) {
                ctx.status(403);
                ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
            } catch (Exception e) {
                ctx.status(500);
                ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
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
