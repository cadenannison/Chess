package client;

public class Repl {
    private final ServerFacade server;

    public Repl(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
    }

    public void run() {
    }
}
