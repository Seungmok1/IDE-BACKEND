package everyide.webide.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import everyide.webide.command.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

@RequiredArgsConstructor
public class TerminalWebSocketHandler extends TextWebSocketHandler {

    private final CommandService commandService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // JSON 메시지 파싱
        Map<String, String> messageMap = objectMapper.readValue(message.getPayload(), new TypeReference<Map<String, String>>(){});
        String email = messageMap.get("email");
        String command = messageMap.get("command");

        // 명령어 실행 및 결과 저장
        String output = commandService.executeCommand(email, command);

        // 실행 결과 전송
        session.sendMessage(new TextMessage(output));
    }
}