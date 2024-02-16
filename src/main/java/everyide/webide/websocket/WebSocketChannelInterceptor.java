package everyide.webide.websocket;

import everyide.webide.config.auth.jwt.JwtTokenProvider;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import everyide.webide.websocket.domain.Session;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final WebSocketSessionMapper webSocketSessionMapper;
    private final WebSocketUserCountMapper webSocketUserCountMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("full message={}", message);
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String authToken = headerAccessor.getFirstNativeHeader("Authorization");

        if (authToken != null && authToken.startsWith("Bearer ")) {
            String token = authToken.substring(7);
            if (!jwtTokenProvider.validateToken(token).equals("success")) {
                return message;
            }
        }
        log.error("토큰 오류");
        return null;
    }

    @EventListener
    public void connect(SessionConnectEvent event) {
        log.info("입장");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getUser().getName();

        webSocketSessionMapper.put(sessionId, createSession(headerAccessor));
        webSocketUserCountMapper.increase(Long.valueOf(getHeaderValue(headerAccessor, "projectId")));
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        log.trace("퇴장");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getUser().getName();

        webSocketSessionMapper.remove(sessionId);
        webSocketUserCountMapper.decrease(Long.valueOf(getHeaderValue(headerAccessor, "projectId")));
    }

    private Session createSession(StompHeaderAccessor headerAccessor) {
        String token = getHeaderValue(headerAccessor, "Authorization");
        User user = getUserFromToken(token);
        Long containerId = Long.valueOf(getHeaderValue(headerAccessor, "projectId"));
        return new Session(user.getId(), containerId);
    }

    private String getHeaderValue(StompHeaderAccessor headerAccessor, String headerKey) {
        String value = headerAccessor.getFirstNativeHeader(headerKey);
        if (value == null || value.isEmpty()) {
            log.error("Header {} not found", headerKey);
            throw new IllegalArgumentException("Required header not found: " + headerKey);
        }
        return value;
    }

    private User getUserFromToken(String token) {
        if (!jwtTokenProvider.validateToken(token).equals("success")) {
            log.error("token 오류");
            throw new SecurityException("Invalid token");
        }
        String userEmail = jwtTokenProvider.getClaims(token).getSubject();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found for email: " + userEmail));
    }
}
