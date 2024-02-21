package everyide.webide.websocket;

import everyide.webide.websocket.domain.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebSocketRoomUserSessionMapper {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, UserSession>> roomUserSessionMap = new ConcurrentHashMap<>();

    public void putSession(String containerId, String sessionId, UserSession userSession) {
        if (isExistedContainer(containerId)) {
            roomUserSessionMap.get(containerId).put(sessionId, userSession);
        } else {
            ConcurrentHashMap<String, UserSession> sessionMap = new ConcurrentHashMap<>();
            sessionMap.put(sessionId, userSession);
            roomUserSessionMap.put(containerId, sessionMap);
        }
    }

    public UserSession getSession(String containerId, String sessionId) {
        return findSession(containerId, sessionId);
    }

    public List<UserSession> getAllSessionsInContainer(String containerId) {
        ConcurrentHashMap<String, UserSession> sessionMap = roomUserSessionMap.get(containerId);
        if (sessionMap == null) {
            log.warn("존재하지 않는 컨테이너={}", containerId);
            throw new NoSuchElementException("Container does not exist for ID: " + containerId);
        }
        return sessionMap.values().stream().toList();
    }

    public int countSession(String containerId) {
        ConcurrentHashMap<String, UserSession> sessionMap = roomUserSessionMap.get(containerId);
        if (sessionMap == null) {
            log.warn("존재하지 않는 컨테이너={}", containerId);
            throw new NoSuchElementException("Container does not exist for ID: " + containerId);
        }
        return sessionMap.entrySet().size();
    }

    public UserSession removeSession(String containerId, String sessionId) {
        return roomUserSessionMap.get(containerId).remove(sessionId);
    }

    private UserSession findSession(String containerId, String sessionId) {
        ConcurrentHashMap<String, UserSession> sessionMap = roomUserSessionMap.get(containerId);
        if (sessionMap == null) {
            log.warn("존재하지 않는 컨테이너={}", containerId);
            throw new NoSuchElementException("Container does not exist for ID: " + containerId);
        }
        UserSession userSession = sessionMap.get(sessionId);
        if (userSession == null) {
            log.warn("존재하지 않는 세션: {}", sessionId);
            throw new NoSuchElementException("UserSession does not exist for ID: " + sessionId);
        }
        return userSession;
    }

    private boolean isExistedContainer(String containerId) {
        return roomUserSessionMap.containsKey(containerId);
    }
}
