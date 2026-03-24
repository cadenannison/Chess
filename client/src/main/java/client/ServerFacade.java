package client;

import com.google.gson.Gson;
import model.AuthData;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int portNum) {
        this.serverUrl = "http://localhost:" + portNum;
        // from client-md suggestion for letting the user change port number
    }
    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }

    private record RegisterRequest(String username, String password, String email) {}
    public record AuthData(String username, String authToken) {}

    public AuthData register(String username, String password, String email) throws Exception {

    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }



}
