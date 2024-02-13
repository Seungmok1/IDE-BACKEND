package everyide.webide.websocket.chat;

import everyide.webide.websocket.chat.domain.Message;
import everyide.webide.websocket.chat.domain.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    @Transactional
    @MessageMapping("/room/{roomId}/chat")
    @SendTo("/topic/room/{roomId}/chat")
    public MessageDto message(MessageDto messageDto, @DestinationVariable String roomId) {
        Message message = Message.builder()
                .roomId(roomId)
                .contentType(messageDto.getContentType())
                .content(messageDto.getContent())
                .senderId(messageDto.getSenderId())
                .build();
        messageRepository.save(message);
        return messageDto;
    }
}
