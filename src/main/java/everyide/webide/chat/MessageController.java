package everyide.webide.chat;

import everyide.webide.chat.domain.Message;
import everyide.webide.chat.domain.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

//    @SubscribeMapping("/topic/room/{roomId}/chat")
//    @SendTo("topic/room/{roomId}/chat")
//    public MessageDto enter(@DestinationVariable String roomId) {
//        return new MessageDto("1", "TALK", "입장!", 1L);
//    }

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
