package everyide.webide.websocket.domain;

import everyide.webide.chat.domain.MessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EnterResponseDto {
    private List<UserSession> userSessions;
    private List<MessageResponseDto> messages;
}
