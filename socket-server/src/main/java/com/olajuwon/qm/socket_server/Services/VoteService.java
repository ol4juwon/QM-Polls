package com.olajuwon.qm.socket_server.Services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.olajuwon.qm.socket_server.Controller.ResultController;
import com.olajuwon.qm.socket_server.Entity.Poll;
import com.olajuwon.qm.socket_server.Repository.PollRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import io.vertx.core.json.JsonObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VoteService {

    private final WebClient webclient;

    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    VoteService(@Value("${POLL_SVC_URL:http://localhost:5006/api/v1}") String  voteURl) {
        this.webclient = WebClient.create(voteURl);
    }
    public JsonObject vote(String pollid, String username, String option) throws JSONException {
log.info("vote: {}, {}, {}", pollid, username, option);
try {
    Map<String, String> body = Map.of("userId", username, "option", option);

    Map response = webclient.post().uri("/polls/{id}/vote", pollid).bodyValue(body).retrieve().bodyToMono(Map.class).block();
    log.info("response: {}", response);
    if (response == null) {
        return new JsonObject().put("status", false).put("message", "Failed to post vote");
    }

    if (response.get("status").equals(true)) {
        return new JsonObject().put("status", true).put("message", "Vote posted successfully");
    } else {
        return new JsonObject().put("status", false).put("message", "Failed to post vote");
    }
} catch (WebClientResponseException e) {
    // HTTP error response
    log.error("error: {}", e.getMessage());
    String errorBody = e.getResponseBodyAsString();
    int statusCode = e.getRawStatusCode();

    System.out.println("Error status: " + statusCode);
    System.out.println("Error body: " + errorBody);
//}

//catch (Exception e){
//    log.error("error: {}", e.getMessage(), e);
    return new JsonObject().put("status", false).put("message", "Failed to post vote");
}
     }


    @Scheduled(fixedRate = 60000)
    public void closeExpiredPolls() {
        log.info("closing polls {}", java.time.Instant.now().plus(java.time.Duration.ofHours(1)));
        List<Poll> expiredPolls = pollRepository.findByScheduledEndTimeBeforeAndClosedFalse(Instant.now().plus(java.time.Duration.ofHours(1)));
        log.info("Found {} expired polls", expiredPolls.size());
        for (var poll : expiredPolls) {
            closePollAsync(poll.getId());
        }
    }

    @Async("pollExecutor")
    public void closePollAsync(String pollId) {
        try {
            pollRepository.findById(pollId).ifPresent(poll -> {
                poll.setClosed(true);
                pollRepository.save(poll);
            });
            String key = "poll:" + pollId;
            var finalResult = redisTemplate.opsForHash().entries(key);
            ResultController.broadcastResult(pollId, new ObjectMapper().writeValueAsString(finalResult));
        }catch (Exception e){
            System.out.println("Error closing poll: " + e.getMessage());
        }
    }
}
