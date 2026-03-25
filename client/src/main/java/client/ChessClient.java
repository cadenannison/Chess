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
    private State state = State.LOGEDOUT;
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
                System.out.print(BLUE + result);
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
                case "login" -> login(params);
                case "list games" -> listGames();
                case "logout" -> signOut();
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "join game" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "help" -> help();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws Exception {
        if (params.length >= 1) {
            state = State.LOGEDIN;
            authToken = String.join(params);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new Exception(Exception, "Expected: <yourname>");
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

