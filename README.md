# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## Code For Sequence Diagram

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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