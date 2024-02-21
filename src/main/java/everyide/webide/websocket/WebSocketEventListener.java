package everyide.webide.websocket;

import everyide.webide.websocket.domain.UserSession;
import everyide.webide.websocket.userstate.UserStateController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;
    private final UserStateController userStateController;

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String containerId = headerAccessor.getFirstNativeHeader("projectId");
        String sessionId = headerAccessor.getUser().getName();
        log.info("퇴장 세션={}", sessionId);

        webSocketRoomUserSessionMapper.removeSession(containerId, sessionId);

        userStateController.sendUserState(containerId);
    }
}
