package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class LogoutService {
    private DataAccess dataAccess;
    public LogoutService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    //creating the objects to store the data from web and for client
    public record LogoutRequest(String authToken) {}
    public record LogoutResult() {}

    public LogoutResult logoutUser(String authToken) throws Unauthorized, DataAccessException {
        //check for a auth token
        if (dataAccess.getAuth(authToken) == null) {
            throw new Unauthorized("Error: unauthorized");
        }
        else {
            // this is where I delete the auth key
            dataAccess.deleteAuthToken(authToken);
            return new LogoutResult();
        }
    }
}
