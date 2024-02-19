package everyide.webide.websocket;

import everyide.webide.config.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final WebSocketUserSessionMapper webSocketUserSessionMapper;
    private final WebSocketRoomUserCountMapper webSocketRoomUserCountMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("full message={}", message);
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//        String authToken = headerAccessor.getFirstNativeHeader("Authorization");
//
//        if (authToken != null && authToken.startsWith("Bearer ")) {
//            String token = authToken.substring(7);
//            if (!jwtTokenProvider.validateToken(token).equals("success")) {
//                return message;
//            }
//        }
//        log.error("토큰 오류");
//        return null;
        return message;
    }

    @EventListener
    public void connect(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getUser().getName();
        log.info("입장 세션={}", sessionId);

        //TODO 세션에서 roomId를 가져와서 브로드캐스팅하기
        messagingTemplate.convertAndSend("/topic/room/{roomId}/state", "현재 유저 정보");
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getUser().getName();
        log.info("퇴장 세션={}", sessionId);

        webSocketUserSessionMapper.remove(sessionId);
        webSocketRoomUserCountMapper.decrease(getHeaderValue(headerAccessor, "projectId"));

        //TODO 세션에서 roomId를 가져와서 브로드캐스팅하기
        messagingTemplate.convertAndSend("/topic/room/{roomId}/state", "현재 유저 정보");
    }

    private String getHeaderValue(SimpMessageHeaderAccessor headerAccessor, String headerKey) {
        String value = headerAccessor.getFirstNativeHeader(headerKey);
        if (value == null || value.isEmpty()) {
            log.error("Header {} not found", headerKey);
            throw new IllegalArgumentException("Required header not found: " + headerKey);
        }
        return value;
    }
}
