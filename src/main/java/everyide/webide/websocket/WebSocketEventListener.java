package everyide.webide.websocket;

import everyide.webide.config.auth.jwt.JwtTokenProvider;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import everyide.webide.websocket.domain.UserSession;
import everyide.webide.websocket.userstate.UserStateController;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;
    private final UserStateController userStateController;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @EventListener
    public void connect(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String containerId = getHeaderValue(headerAccessor, "ProjectId");
        String sessionId = headerAccessor.getUser().getName();
        log.info("입장 세션={}", sessionId);

        webSocketRoomUserSessionMapper.putSession(
                containerId, sessionId, createUserSession(headerAccessor, containerId)
        );
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getUser().getName();
        log.info("퇴장 세션={}", sessionId);
        UserSession userSession = webSocketRoomUserSessionMapper.removeSession(sessionId);

        userStateController.sendUserState(userSession.getContainerId());
    }

    private UserSession createUserSession(SimpMessageHeaderAccessor headerAccessor, String containerId) {
        String token = getHeaderValue(headerAccessor, "Authorization").substring(7);
        User user = getUserFromToken(token);
        return new UserSession(user.getId(), user.getName(), user.getEmail(), containerId);
    }

    private String getHeaderValue(SimpMessageHeaderAccessor headerAccessor, String headerKey) {
        String value = headerAccessor.getFirstNativeHeader(headerKey);
        if (value == null || value.isEmpty()) {
            log.error("Header {} not found", headerKey);
            throw new IllegalArgumentException("Required header not found: " + headerKey);
        }
        return value;
    }

//    private User getUserFromToken(String token) {
//        if (!jwtTokenProvider.validateToken(token).equals("Success")) {
//            System.out.println(jwtTokenProvider.validateToken(token));
//            log.error("token 오류");
//            throw new SecurityException("Invalid token");
//        }
//        String userEmail = jwtTokenProvider.getClaims(token).getSubject();
//        return userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new EntityNotFoundException("User not found for email: " + userEmail));
//    }
}
