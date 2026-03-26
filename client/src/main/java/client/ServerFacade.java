package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient client = HttpClient.newHttpClient();


    public ServerFacade(int portNum) {
        this.serverUrl = "http://localhost:" + portNum;
        // from client-md suggestion for letting the user change port number
    }
    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }

    private record RegisterRequest(String username, String password, String email) {}

    public AuthData register(String username, String password, String email) throws Exception {
        var body = new RegisterRequest(username, password, email);
        var request = buildRequest("POST", "/user", body, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    private record LoginRequest(String username, String password) {}

    public AuthData login(String username, String password) throws Exception {
        var body = new LoginRequest(username, password);
        var request = buildRequest("POST", "/session", body, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout(String authToken) throws Exception {
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private record CreateGameRequest(String gameName) {}
    private record CreateGameResult(int gameID) {}

    public int createGame(String authToken, String gameName) throws Exception {
        var body = new CreateGameRequest(gameName);
        var request = buildRequest("POST", "/game", body, authToken);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class).gameID();
    }

    private record ListGamesResult(List<GameData> games) {}

    public List<GameData> listGames(String authToken) throws Exception {
        var request = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class).games();
    }

    private record JoinGameRequest(String playerColor, int gameID) {}

    public void joinGame(String authToken, String playerColor, int gameId) throws Exception {
        var body = new JoinGameRequest(playerColor, gameId);
        var request = buildRequest("PUT", "/game", body, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    //helpers for the other methods

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
        }
        else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new Exception(new Gson().fromJson(body, Map.class).get("message").toString());
            }
            throw new Exception("failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        else{
            return null;
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public void clearDb() throws Exception {
        var buildRequest = buildRequest("DELETE", "/db", null, null);
        var response = sendRequest(buildRequest);
        handleResponse(response, null);
    }
}
