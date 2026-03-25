package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import model.GameData;

import static ui.EscapeSequences.*;

public class ChessClient {
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private List<GameData> games = new ArrayList<>();

    public ChessClient(String serverUrl) throws Exception {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess! Sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login"    -> login(params);
                case "logout"   -> logout();
                case "list"     -> listGames();
                case "create"   -> createGame(params);
                case "play"     -> playGame(params);
                case "observe"  -> observeGame(params);
                case "help"     -> help();
                case "quit"     -> "quit";
                default         -> help();
            };
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws Exception {
        if (params.length == 2) {
            var result = server.login(params[0], params[1]);
            authToken = result.authToken();
            state = State.SIGNEDIN;
            return String.format("Logged in as %s.", result.username());
        }
        throw new Exception("Need: <username> <password>");
    }

    public String register(String... params) throws Exception {
        if (params.length == 3) {
            var result = server.register(params[0], params[1], params[2]);
            authToken = result.authToken();
            state = State.SIGNEDIN;
            return String.format("Logged in as %s.", result.username());
        }
        throw new Exception("Expected: <username> <password> <email>");
    }

    public String signOut() throws Exception {
        assertloggedin();
        state = State.LOGEDOUT;
        return String.format("%s left the shop", visitorName);
    }

    private GameData getGame(int gameId) throws Exception {
        for (GameData game : server.listGames()) {
            if (game.id() == id) {
                return game;
            }
        }
        return null;
    }

    public String help() {
        if (state == State.LOGEDOUT) {
            return """
                    - login <yourname>
                    - quit
                    """;
        }
        return """
                - list games
                - create game
                - join game
                - observe
                - logout
                - help
                - quit
                """;
    }

    private void assertSignedIn() throws Exception {
        if (state == State.LOGEDOUT) {
            throw new Exception(Exception, "You must sign in");
        }
    }
}

