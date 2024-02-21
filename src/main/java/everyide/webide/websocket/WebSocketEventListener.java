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

    private final WebSocketUserSessionMapper webSocketUserSessionMapper;
    private final WebSocketRoomUserCountMapper webSocketRoomUserCountMapper;
    private final UserStateController userStateController;

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getUser().getName();
        log.info("퇴장 세션={}", sessionId);

        UserSession userSession = webSocketUserSessionMapper.remove(sessionId);
        webSocketRoomUserCountMapper.decrease(String.valueOf(userSession.getContainerId()));

        //TODO 세션에서 roomId를 가져와서 수정된 정보 브로드캐스팅하기
        userStateController.sendUserState(userSession);
    }
}
