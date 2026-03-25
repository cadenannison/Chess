package client;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) throws Exception{
        this.client = new ChessClient(serverUrl);
    }

    public void run() {
        client.run();
    }
}
