package everyide.webide.websocket;

import everyide.webide.websocket.domain.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketSessionMapper {

    private final ConcurrentHashMap<String, Session> webSocketSessionMap = new ConcurrentHashMap<>();

    public void put(String sessionId, Session session) {
        this.webSocketSessionMap.put(sessionId, session);
    }

    public Session get(String sessionId) {
        return this.webSocketSessionMap.get(sessionId);
    }

    public Session remove(String sessionId) throws Exception {
        Session session = this.webSocketSessionMap.remove(sessionId);
        if (session == null) {
            throw new Exception("존재하지 않는 세션");
        }
        return session;
    }
}
