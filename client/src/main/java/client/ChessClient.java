package client;

import java.util.*;

import chess.ChessBoard;
import chess.ChessMove;
import model.GameData;
import ui.DrawBoardMethods;
import websocket.messages.ServerMessage;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;

public class ChessClient implements NotificationHandler{
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private List<GameData> games = new ArrayList<>();
    private Map<Integer, Integer> gameListNumToId = new HashMap<>();
    private WebsocketFacade ws;
    private boolean whitePerspective = true;
    private final String serverUrl;
    private Integer currentGameId = null;
    private ChessBoard currentBoard;

    //create map/array for gameId vs order in list of game
    // look for client error appending
    // make gamelist number map to real game ID
    // catch number format exception in play and observe game, print readable message to client here

    public ChessClient(String serverUrl) throws Exception {
        this.serverUrl = serverUrl;
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

    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case LOAD_GAME -> {
                websocket.messages.LoadGameMessage loadMessage = (websocket.messages.LoadGameMessage) notification;
                ChessBoard board = loadMessage.getChessGame().getBoard();
                this.currentBoard = board;
                String boardString = DrawBoardMethods.draw(board, whitePerspective);
                System.out.println("\n" + boardString);
            }
            case ERROR -> {
                websocket.messages.ErrorMessage errorMessage = (websocket.messages.ErrorMessage) notification;
                System.out.println("Error: " + errorMessage.getErrorMessage());
            }
            case NOTIFICATION -> {
                websocket.messages.NotificationMessage notifMessage =
                        (websocket.messages.NotificationMessage) notification;
                System.out.println(notifMessage.getNotificationMessage());
            }
        }
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_BLUE);    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            if (state == State.PLAYINGGAME) {
                return switch (cmd) {
                    case "leave" -> leave();
                    case "redraw" -> redrawBoard();
                    case "move" -> makeMove(params);
                    case "highlight" -> highlight(params);
                    case "resign" -> resign();
                    case "help" -> help();
                    default -> "Unknown- Type 'help' for options.";
                };
            }
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

    public String redrawBoard() throws Exception{
        if (currentBoard == null) {
            return "Board hasnt loaded yet";
        }
        return DrawBoardMethods.draw(currentBoard, whitePerspective);
    }

    public String highlight(String... params) throws Exception {
        if (params.length < 1) {
            throw new Exception("Expected: highlight <position> (ex. e2)");
        }
        String position = params[0];
        int column = position.charAt(0) - 'a' + 1;
        int row = Character.getNumericValue(position.charAt(1));
        if (currentBoard == null) {
            return "Board hasnt loaded.";
        }
        chess.ChessPosition chessPos = new chess.ChessPosition(row, column);
        chess.ChessGame phantomGame = new chess.ChessGame();
        phantomGame.setBoard(currentBoard);
        var validMoves = phantomGame.validMoves(chessPos);
        var highlights = new java.util.ArrayList<chess.ChessPosition>();
        highlights.add(chessPos);

        if (validMoves != null) {
            for (chess.ChessMove move : validMoves) {
                highlights.add(move.getEndPosition());
            }
        }
        return DrawBoardMethods.draw(currentBoard, whitePerspective, highlights);
    }

    public String makeMove(String... params) throws Exception{
        if (params.length < 1){
            throw new Exception("expected: (move) i.e. a2a6");
        }
        String moveAsString = params[0];
        if (moveAsString.length() < 4) {
            throw new Exception("Bad move Format");
        }
        int startColumn = moveAsString.charAt(0) - 'a' + 1;
        int startRow = Character.getNumericValue(moveAsString.charAt(1));
        int endColumn = moveAsString.charAt(2) - 'a' + 1;
        int endRow = Character.getNumericValue(moveAsString.charAt(3));

        chess.ChessPiece.PieceType promotionPiece = null;
        if (moveAsString.length() == 5) {
            char p = moveAsString.charAt(4);
            switch (p) {
                case 'q' -> promotionPiece = chess.ChessPiece.PieceType.QUEEN;
                case 'r' -> promotionPiece = chess.ChessPiece.PieceType.ROOK;
                case 'b' -> promotionPiece = chess.ChessPiece.PieceType.BISHOP;
                case 'n' -> promotionPiece = chess.ChessPiece.PieceType.KNIGHT;
                default -> promotionPiece = null;
            };
        }
        chess.ChessPosition startPosition = new chess.ChessPosition(startRow, startColumn);
        chess.ChessPosition endPosition = new chess.ChessPosition(endRow, endColumn);
        chess.ChessMove move = new chess.ChessMove(startPosition, endPosition, promotionPiece);
        var command = new websocket.commands.MakeMoveCommand(authToken, currentGameId, move);
        ws.makeMove(command);
        return "Making move " + moveAsString;
    }

    public String resign() throws Exception {
        System.out.print("Are you sure you want to resign? (yes/no): ");
        Scanner confirmScanner = new Scanner(System.in);
        String answer = confirmScanner.nextLine().trim().toLowerCase();
        if (answer.equals("yes")) {
            ws.resign(authToken, currentGameId);
            return "Resignation sent.";
        }
        return "Resignation cancelled.";
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

    public String leave() throws Exception {
        ws.leaveGame(authToken, currentGameId);
        this.state = State.SIGNEDIN;
        this.ws = null;
        this.currentGameId = null;
        this.currentBoard = null;
        return "Left the game.";
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
            gameListNumToId.clear();
            for (int i = 0; i < games.size(); i++) {
                gameListNumToId.put(i + 1, games.get(i).gameID());
            }
            return String.format("Created game: %s", params[0]);
        }
        throw new Exception("Expected: <gameName>");
    }

    public String listGames() throws Exception {
        assertSignedIn();
        games = server.listGames(authToken);
        gameListNumToId.clear();
        var string = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            GameData currentGame = games.get(i);
            gameListNumToId.put(i + 1, currentGame.gameID());
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
        if (gameListNumToId.isEmpty()) {
            games = server.listGames(authToken);
            for (int i = 0; i < games.size(); i++) {
                gameListNumToId.put(i + 1, games.get(i).gameID());
            }
        }
        if (params.length == 1) {
            int listNum;
            try {
                listNum = parseInt(params[0]);
            }
            catch (NumberFormatException e) {
                return "'" + params[0] + "' is not a valid game number. Please enter a number from 'list'.";
            }
            if (!gameListNumToId.containsKey(listNum)) {
                throw new Exception("Bad game number please use 'list'");
            }
            int gameNum = gameListNumToId.get(listNum);
            this.currentGameId = gameNum;
            this.whitePerspective = true;
            ws = new WebsocketFacade(serverUrl, this);
            ws.joinGame(authToken, gameNum);
            this.state = State.PLAYINGGAME;
            return "Now observing game " + listNum;
        }
        throw new Exception("Expected: <gameNumber>");
    }

    public String playGame(String... params) throws Exception {
        assertSignedIn();
        if (gameListNumToId.isEmpty()) {
            games = server.listGames(authToken);
            for (int i = 0; i < games.size(); i++) {
                gameListNumToId.put(i + 1, games.get(i).gameID());
            }
        }
        if (params.length == 2) {
            int listNum;
            try {
                listNum = parseInt(params[0]);
            }
            catch (NumberFormatException e) {
                return "'" + params[0] + "' is not a valid game number. Please enter a number from 'list'.";
            }
            if (!gameListNumToId.containsKey(listNum)) {
                throw new Exception("Bad game number. Please use 'list'");
            }
            int gameNum = gameListNumToId.get(listNum);
            this.currentGameId = gameNum;
            String playerColor = params[1].toUpperCase();
            if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
                throw new Exception("Expected <gameNumber> <white|black>");
            }
            this.whitePerspective = playerColor.equals("WHITE");
            server.joinGame(authToken, playerColor, gameNum);
            ws = new WebsocketFacade(serverUrl, this);
            ws.joinGame(authToken, gameNum);
            this.state = State.PLAYINGGAME;
            return "Joined game " + listNum + " as " + playerColor;
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
        if (state == State.PLAYINGGAME) {
            return """
                - redraw
                - move <move> (ex. move a2a3 or move a7a8q for promotion)
                - highlight <position> (i.e. highlight e6)
                - resign
                - leave
                - help
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

