package everyide.webide.websocket;

import everyide.webide.websocket.chat.MessageService;
import everyide.webide.websocket.chat.domain.MessageDto;
import everyide.webide.websocket.terminal.TerminalService;
import everyide.webide.websocket.terminal.domain.TerminalExecuteRequestDto;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;
    private final TerminalService terminalService;

    @MessageMapping("/message/{roomId}")
    public void message(MessageDto messageDto, @DestinationVariable String roomId) {
        messageService.send(messageDto, roomId);
    }

    @MessageMapping("/room/{roomId}/terminal")
    public void terminal(
            @Payload TerminalExecuteRequestDto requestDto,
            @DestinationVariable String roomId, Session userSession,
            SimpMessageHeaderAccessor headerAccessor
            ) throws Exception {
        String sessionId = getSessionId(headerAccessor);
        terminalService.executeTerminal(requestDto, roomId, sessionId);
        System.out.println(userSession);
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
