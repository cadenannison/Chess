package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class LoginService {
    private DataAccess dataAccess;

    public LoginService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }
    //creating the objects to store the data from web and for client
    public record LoginRequest(String username, String password) {}
    public record LoginResult(String username, String authToken) {}

    public LoginResult loginUser(LoginRequest request) throws Unauthorized, DataAccessException {
        //check for login issues
        UserData user = dataAccess.getUser(request.username());
        if (request.username() == null || request.password() == null) {
            throw new BadRequest("Error: bad request");
        }
        if (user == null || request.password() == null
                || !user.password().equals(request.password())) {
            throw new Unauthorized("Error: unauthorized");
        }
        else {
            // make the auth key
            String authToken = java.util.UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, request.username());
            dataAccess.createAuth(authData);
            return new LoginResult(request.username(), authToken);
        }
    }
}
