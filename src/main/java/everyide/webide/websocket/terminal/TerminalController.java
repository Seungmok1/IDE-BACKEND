package everyide.webide.websocket.terminal;

import everyide.webide.websocket.terminal.domain.TerminalExecuteRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TerminalController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/room/{roomId}/terminal")
    public void terminal(
            @Payload TerminalExecuteRequestDto requestDto,
            @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor
    ) throws Exception {
        String sessionId = getSessionId(headerAccessor);
        simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/room/" + roomId + "/terminal", requestDto);
    }

    private String getSessionId(SimpMessageHeaderAccessor headerAccessor) throws Exception {
        ConcurrentHashMap<String, String> simpSessionAttributes = (ConcurrentHashMap<String, String>) headerAccessor.getMessageHeaders().get("simpSessionAttributes");
        if (simpSessionAttributes == null) {
            log.trace("웹소켓 세션 아이디를 찾을 수 없습니다!");
            throw new Exception("웹소켓 세션 아이디를 찾을 수 없습니다.");
        }
        return simpSessionAttributes.get("SessionId");
    }
}
