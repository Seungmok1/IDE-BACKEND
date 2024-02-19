package everyide.webide.websocket;

import everyide.webide.websocket.domain.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketUserSessionMapper {

    private final ConcurrentHashMap<String, UserSession> webSocketUserSessionMap = new ConcurrentHashMap<>();

    public void put(String sessionId, UserSession userSession) {
        webSocketUserSessionMap.put(sessionId, userSession);
        log.info("UserSession added: {}", sessionId);
    }

    public UserSession get(String sessionId) {
        UserSession userSession = webSocketUserSessionMap.get(sessionId);
        if (userSession == null) {
            log.warn("존재하지 않는 세션: {}", sessionId);
            throw new NoSuchElementException("UserSession does not exist for ID: " + sessionId);
        }
        log.info("UserSession retrieved: {}", sessionId);
        return userSession;
    }

    public UserSession remove(String sessionId) {
        UserSession userSession = webSocketUserSessionMap.remove(sessionId);
        if (userSession == null) {
            log.error("존재하지 않는 세션: {}", sessionId);
            throw new NoSuchElementException("UserSession does not exist for ID: " + sessionId);
        }
        log.info("UserSession removed: {}", sessionId);
        return userSession;
    }

    public boolean isExisted(String sessionId) {
        return webSocketUserSessionMap.containsKey(sessionId);
    }
}
