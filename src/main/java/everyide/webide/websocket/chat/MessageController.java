package everyide.webide.websocket.chat;

import everyide.webide.websocket.chat.domain.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

//    private final MessageService messageService;
//
//    @MessageMapping("/message/{chatId}")
//    public void message(MessageDto messageDto, @DestinationVariable Long chatId){
//        messageService.send(messageDto, chatId);
//    }
}
