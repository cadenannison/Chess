package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import chess.ChessBoard;
import model.GameData;
import ui.DrawBoardMethods;

import static java.lang.Integer.parseInt;
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
                var msg = e.getMessage();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_BLUE);    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "list" -> listGames();
                case "create" -> createGame(params);
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "help" -> help();
                case "quit" -> "quit";
                default -> help();
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

    public String logout() throws Exception {
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logout(authToken);
        authToken = null;
        return "Signed out";
    }

    public String createGame(String... params) throws Exception {
        assertSignedIn();
        if (params.length == 1) {
            server.createGame(authToken, params[0]);
            games = server.listGames(authToken); //added this so that the games object autoupdates
            return String.format("Created game: %s", params[0]);
        }
        throw new Exception("Expected: <gameName>");
    }

    public String listGames() throws Exception {
        assertSignedIn();
        games = server.listGames(authToken);
        var string = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            GameData currentGame = games.get(i);
            String white;
            if (currentGame.whiteUsername() != null) {
                white = currentGame.whiteUsername();
            }
            else {
                white = "available";
            }
            String black;
            if (currentGame.blackUsername() != null) {
                black = currentGame.blackUsername();
            }
            else {
                black = "available";
            }
            string.append((i + 1) + ". " + currentGame.gameName() +
                 " | white: " + white + " | black: " + black + "\n");
        }
        return string.toString();
    }

    public String observeGame(String... params) throws Exception {
        assertSignedIn();
        if (games.isEmpty()) {
            games = server.listGames(authToken);
        }
        if (params.length == 1) {
            int gameId = parseInt(params[0]);
            gameId--;
            if (gameId < 0 || gameId >= games.size()) {
                throw new Exception("Bad game number please use 'list'");
            }
            int displayNum = gameId + 1;
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            String boardString = DrawBoardMethods.draw(board, true);
            return "Observing game " + displayNum + "\n" + boardString;
        }
        throw new Exception("Expected: <gameNumber>");
    }

    public String playGame(String... params) throws Exception {
        assertSignedIn();
        if (games.isEmpty()) {
            games = server.listGames(authToken);
        }
        if (params.length == 2) {
            int gameId = parseInt(params[0]);
            gameId--;
            //check if the bounds are ok
            if (gameId < 0 || gameId >= games.size()){
                throw new Exception("Bad game number. Please use 'list'");
            }
            int gameNum = games.get(gameId).gameID();
            String playerColor = params[1].toUpperCase();
            server.joinGame(authToken, playerColor, gameNum);
            int displayNum = gameId + 1;
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            String boardString = DrawBoardMethods.draw(board, playerColor.equals("WHITE"));
            return "Joined game " + displayNum + " as " + playerColor + "\n" + boardString;
        }
        else {
            throw new Exception("Expected <gameNumber> <playerColor>");
        }
    }

        public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - register <username> <password> <email>
                    - login <username> <password>
                    - help
                    - quit
                    """;
        }
        return """
                - list
                - create <GameName>
                - play <GameNumber> <WHITE | BLACK> 
                - observe <GameNumber>
                - logout
                - help
                - quit
                """;
    }

    private void assertSignedIn() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new Exception("You gotta sign in");
        }
    }

}

