package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;

public class RegisterService {
    private DataAccess dataAccess;

    public RegisterService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }
    //creating the objects to store the data from web and for client
    public record RegisterRequest(String username, String password, String email) {}
    public record RegisterResult(String username, String authToken) {}

    public RegisterResult registerUser(RegisterRequest request) throws BadRequest, AlreadyTaken, DataAccessException {
        //check for register issues
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequest("Missing field(s)");
        }
        else if (DataAccess.getUser(request.username()) != null) {
            throw new AlreadyTaken("User already exists");
        }
        else {
            //store the user request in a newUser record
            UserData newUser = new UserData(request.username(), request.password(), request.email());
            dataAccess.createUser(newUser);
            // make the auth key
            String authToken = java.util.UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, request.username());
            dataAccess.createAuth(authData);
            return new RegisterResult(request.username(), authToken);
        }
    }
}
