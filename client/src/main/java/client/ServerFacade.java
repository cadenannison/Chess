package client;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int portNum) {
        this.serverUrl = "http://localhost:" + portNum;
        // from
    }
    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }
}
