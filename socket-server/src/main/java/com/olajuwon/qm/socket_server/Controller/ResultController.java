package com.olajuwon.qm.socket_server.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.olajuwon.qm.socket_server.Services.VoteService;
import com.olajuwon.qm.socket_server.VoteMessageDTO;
import io.vertx.core.json.JsonObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value="/ws/polls/{pollId}", configurator = com.olajuwon.qm.socket_server.Config.SpringConfigurator.class)
@Component
public class ResultController {

    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();


    private final VoteService voteService;

    public ResultController(VoteService voteService) {
        this.voteService = voteService;
    }

    private static final ConcurrentHashMap<String, Set<Session>> pollSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("pollId") String pollId){
        sessions.add(session);
    }
    @OnClose
    public void onClose(Session session){ sessions.remove(session); }
    @OnMessage
    public void onMessage(String msg, Session session,  @PathParam("pollId") String pollId){
        try {
            log.info("Received message: {}", msg);
            ObjectMapper mapper = new ObjectMapper();
            VoteMessageDTO pollMsg = mapper.readValue(msg, VoteMessageDTO.class);

            if ("vote".equalsIgnoreCase(pollMsg.getAction())) {

              JsonObject response =  voteService.vote(pollId,pollMsg.getUsername(), pollMsg.getOption());

                if(response.getBoolean("status")) {
                    session.getBasicRemote().sendText("Vote recorded for " + pollMsg.getOption());
                }else{
                    session.getBasicRemote().sendText("Sorry, vote was not recorded");
                }
            }

        } catch (Exception e) {
            try { session.getBasicRemote().sendText("Error: " + e.getMessage()); }
            catch (IOException ignored) {}
        }
    }
    public static void broadcastResult(String pollId, String message) {
        log.info("Broadcasting result for poll: {} {}", pollId, message);
        Set<Session> sessions = pollSessions.get(pollId);
        if (sessions != null) {
            for (Session s : sessions) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException ignored) {}
            }
        }
    }
}
