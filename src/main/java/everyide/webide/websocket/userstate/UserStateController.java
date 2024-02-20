package everyide.webide.websocket.userstate;

import everyide.webide.config.auth.jwt.JwtTokenProvider;
import everyide.webide.room.RoomRepository;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import everyide.webide.websocket.WebSocketRoomUserCountMapper;
import everyide.webide.websocket.WebSocketUserSessionMapper;
import everyide.webide.websocket.domain.UserSession;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserStateController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final WebSocketRoomUserCountMapper webSocketRoomUserCountMapper;
    private final WebSocketUserSessionMapper webSocketUserSessionMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @SubscribeMapping("/topic/room/{roomId}/enter")
    public void enter(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String roomId) {
        String sessionId = headerAccessor.getUser().getName();
        webSocketUserSessionMapper.put(sessionId, createUserSession(headerAccessor));

        boolean increased = webSocketRoomUserCountMapper.increase(getHeaderValue(headerAccessor, "projectId"));
        if (!increased) {
            log.warn("프로젝트 ID={}에 대한 최대 사용자 수에 도달했습니다. 세션 ID={}", getHeaderValue(headerAccessor, "projectId"), sessionId);
            messagingTemplate.convertAndSendToUser(sessionId, "/user/queue/disconnect", "입장불가");
        } else {
            sendUserState(roomId);
            log.info("입장 세션={}", sessionId);
        }
    }

    // 유저가 입장이나 퇴장할 때 수정된 유저들의 정보를 브로드캐스팅
    public void sendUserState(String roomId) {
        messagingTemplate.convertAndSend("topic/room/" + roomId + "/state", "현재 유저 정보");
    }

    private UserSession createUserSession(SimpMessageHeaderAccessor headerAccessor) {
        String token = getHeaderValue(headerAccessor, "Authorization");
        User user = getUserFromToken(token);
        Long containerId = Long.valueOf(getHeaderValue(headerAccessor, "projectId"));
        return new UserSession(user.getId(), containerId);
    }

    private String getHeaderValue(SimpMessageHeaderAccessor headerAccessor, String headerKey) {
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
