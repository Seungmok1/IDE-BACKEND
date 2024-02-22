package everyide.webide.websocket.domain;

import everyide.webide.chat.domain.MessageResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EnterResponseDto {
    private List<MessageResponseDto> messages;
    private List<UserSession> userSessions;
}
