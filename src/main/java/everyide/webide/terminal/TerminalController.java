package everyide.webide.terminal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TerminalController {

    private final TerminalService terminalService;

    @MessageMapping("/room/{roomId}/terminal")
    @SendToUser("/room/{roomId}/terminal")
    public void execute(
            @DestinationVariable String roomId,
            String command,
            SimpMessageHeaderAccessor headerAccessor
    ) throws Exception {
        log.info("웹소켓 터미널 실행, roomId={}, command={}", roomId, command);

        ConcurrentHashMap<String, String> simpSessionAttributes = (ConcurrentHashMap<String, String>) headerAccessor.getMessageHeaders().get("simpSessionAttributes");
        if (simpSessionAttributes == null) {
            log.info("웹소켓 세션 아이디를 찾을 수 없습니다!");
            throw new Exception("웹소켓 세션 아이디를 찾을 수 없습니다.");
        }
        String sessionId = simpSessionAttributes.get("sessionId");

        terminalService.executeCommand(roomId, command, sessionId);
    }
}