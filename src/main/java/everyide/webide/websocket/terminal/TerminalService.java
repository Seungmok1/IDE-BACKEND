package everyide.webide.websocket.terminal;

import everyide.webide.websocket.terminal.domain.TerminalExecuteRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TerminalService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void executeTerminal(TerminalExecuteRequestDto requestDto, String roomId, String sessionId) {
        simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/room/" + roomId + "/terminal", requestDto);
    }
}
