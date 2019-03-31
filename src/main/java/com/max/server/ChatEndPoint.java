package com.max.server;

import com.max.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chat/{user}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class )
public class ChatEndPoint {
    private static final Logger LOG = LoggerFactory.getLogger(ChatEndPoint.class);

    private Session session;

    private static Set<ChatEndPoint> chats = new CopyOnWriteArraySet<ChatEndPoint>();
    private static Map<String, String> sessionIdToUser = new HashMap<String, String>();


    @OnOpen
    public void onOpen(Session session, @PathParam("user") String userName) {
        this.session = session;

        LOG.info("New connection created for user: {}", userName);
        chats.add(this);
        sessionIdToUser.put(session.getId(), userName);

        Message message = new Message(userName, "Connected!");
        broadcast(message);

    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        message.setFrom(sessionIdToUser.get(session.getId()));
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        String id = session.getId();
        chats.remove(this);
        Message message = new Message(sessionIdToUser.get(id), "Disconnected!");
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    private static void broadcast(final Message message) {
        chats.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
