package everyide.webide.terminal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TerminalController {

    private final TerminalService terminalService;

    @MessageMapping("/execute-command/{containerId}")
    public void handleCommandExecution(@DestinationVariable Long containerId, String command, @Header("simpSessionId") String sessionId) throws IOException, InterruptedException {
        log.info(" 여기 11 !!");
        terminalService.executeCommand(containerId, command, sessionId);
        log.info(" 여기 22 !!");
    }

}
