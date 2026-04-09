package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, List<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameId, Session session) {
        if (connections.get(gameId) == null){ //if the list doesnt exist then make it and add the session.
            List<Session> list = new ArrayList<>();
            list.add(session);
            connections.put(gameId, list); // then put that into the connections hashmap w the gameID
        }
        else {
            connections.get(gameId).add(session);
        }
    }

    public void remove(int gameId, Session session) {
        if (connections.get(gameId) == null){
            return;
        }
        connections.get(gameId).remove(session);
    }

    public void broadcast(Session excludeSession, Notification notification) throws IOException {
        String msg = notification.toString();
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
