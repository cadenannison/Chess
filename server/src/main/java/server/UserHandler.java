package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.http.Context;
import service.*;

public class UserHandler {
    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final Gson gson = new Gson();

    public UserHandler(DataAccess dataAccess) {
        this.registerService = new RegisterService(dataAccess);
        this.loginService = new LoginService(dataAccess);
        this.logoutService = new LogoutService(dataAccess);
    }

    public void register(Context ctx) {
        try {
            RegisterService.RegisterRequest request = gson.fromJson(ctx.body(), RegisterService.RegisterRequest.class);
            RegisterService.RegisterResult result = registerService.registerUser(request);
            ctx.status(200);
            ctx.result(gson.toJson(result));
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
    }

    public void login(Context ctx) {
        try {
            LoginService.LoginRequest request = gson.fromJson(ctx.body(), LoginService.LoginRequest.class);
            LoginService.LoginResult result = loginService.loginUser(request);
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

    public void logout(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            logoutService.logoutUser(authToken);
            ctx.status(200);
            ctx.result("{}");
        } catch (Unauthorized e) {
            ctx.status(401);
            ctx.result("{ \"message\": \"Error: unauthorized\" }");
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
        }
    }
}