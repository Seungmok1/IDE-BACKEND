package everyide.webide.websocket;

import everyide.webide.websocket.domain.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketSessionMapper {

    private final ConcurrentHashMap<String, Session> webSocketSessionMap = new ConcurrentHashMap<>();

    public void put(String sessionId, Session session) {
        webSocketSessionMap.put(sessionId, session);
        log.info("Session added: {}", sessionId);
    }

    public Session get(String sessionId) {
        Session session = webSocketSessionMap.get(sessionId);
        if (session == null) {
            log.warn("존재하지 않는 세션: {}", sessionId);
            throw new NoSuchElementException("Session does not exist for ID: " + sessionId);
        }
        log.info("Session retrieved: {}", sessionId);
        return session;
    }

    public Session remove(String sessionId) {
        Session session = webSocketSessionMap.remove(sessionId);
        if (session == null) {
            log.error("존재하지 않는 세션: {}", sessionId);
            throw new NoSuchElementException("Session does not exist for ID: " + sessionId);
        }
        log.info("Session removed: {}", sessionId);
        return session;
    }

    public boolean isExisted(String sessionId) {
        return webSocketSessionMap.containsKey(sessionId);
    }
}
