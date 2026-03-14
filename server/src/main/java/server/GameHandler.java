package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.http.Context;
import service.*;

public class GameHandler {
    private final CreateGameService createGameService;
    private final ListGamesService listGameService;
    private final JoinGameService joinGameService;
    private final ClearService clearService;
    private final Gson gson = new Gson();

    public GameHandler(DataAccess dataAccess) {
        this.createGameService = new CreateGameService(dataAccess);
        this.listGameService = new ListGamesService(dataAccess);
        this.joinGameService = new JoinGameService(dataAccess);
        this.clearService = new ClearService(dataAccess);
    }

    public void clear(Context ctx) {
        try {
            clearService.clear();
            ctx.status(200);
            ctx.result("{}");
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
    }

    public void createGame(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            CreateGameService.CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameService.CreateGameRequest.class);
            CreateGameService.CreateGameResult result = createGameService.createGame(authToken, request);
            ctx.status(200);
            ctx.result(gson.toJson(result));
        } catch (BadRequest e) {
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: bad request\" }");
        } catch (Unauthorized e) {
            ctx.status(401);
            ctx.result("{ \"message\": \"Error: unauthorized\" }");
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
    }

    public void listGames(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            ListGamesService.ListGamesResult result = listGameService.listGames(authToken);
            ctx.status(200);
            ctx.result(gson.toJson(result));
        } catch (Unauthorized e) {
            ctx.status(401);
            ctx.result("{ \"message\": \"Error: unauthorized\" }");
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
    }

    public void joinGame(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            JoinGameService.JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameService.JoinGameRequest.class);
            joinGameService.joinGame(authToken, request);
            ctx.status(200);
            ctx.result("{}");
        } catch (BadRequest e) {
            ctx.status(400);
            ctx.result("{ \"message\": \"Error: bad request\" }");
        } catch (Unauthorized e) {
            ctx.status(401);
            ctx.result("{ \"message\": \"Error: unauthorized\" }");
        } catch (AlreadyTaken e) {
            ctx.status(403);
            ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
    }
}