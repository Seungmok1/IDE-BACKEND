package everyide.webide.websocket.userstate;

import everyide.webide.chat.MessageRepository;
import everyide.webide.chat.domain.MessageResponseDto;
import everyide.webide.websocket.WebSocketRoomUserSessionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserStateController {

    private final WebSocketRoomUserSessionMapper webSocketRoomUserSessionMapper;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @SubscribeMapping("/container/{containerId}/enter")
    public void enter(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String containerId) {
        sendUserState(containerId);

        List<MessageResponseDto> messages = new java.util.ArrayList<>(messageRepository.findTop20ByContainerIdOrderBySendDateDesc(containerId)
                .stream()
                .map((message -> MessageResponseDto.builder()
                        .userId(message.getUserId())
                        .name(message.getUserName())
                        .content(message.getContent())
                        .build()))
                .toList());
        Collections.reverse(messages);

        String sessionId = headerAccessor.getUser().getName();

        messagingTemplate.convertAndSendToUser(sessionId, "topic/container/" + containerId + "/state", messages);
    }

    // 유저가 입장이나 퇴장할 때 수정된 유저들의 정보를 브로드캐스팅
    public void sendUserState(String containerId) {
        messagingTemplate.convertAndSend(
                "/topic/container/" + containerId + "/state",
                            webSocketRoomUserSessionMapper.getAllSessionsInContainer(containerId)
                );
    }
}
