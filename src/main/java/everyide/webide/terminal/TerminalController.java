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

    @MessageMapping("/container/{containerId}/terminal")
    @SendToUser(value = "/queue/container/{containerId}/terminal", broadcast = false)
    public String execute(
            @DestinationVariable Long containerId,
            String command,
            SimpMessageHeaderAccessor headerAccessor
    ) throws Exception {
        log.info("웹소켓 터미널 실행, containerId={}, command={}", containerId, command);

        String sessionId = headerAccessor.getUser().getName();

        return terminalService.executeCommand(containerId, command, sessionId);
    }
}