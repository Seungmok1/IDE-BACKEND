//package everyide.webide.websocket;
//
//import everyide.webide.websocket.chat.ChatService;
//import everyide.webide.websocket.chat.MessageService;
//import everyide.webide.websocket.chat.domain.MessageDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.annotation.SubscribeMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//@RestController
//@RequiredArgsConstructor
//public class WebSocketController {
//
//    private final MessageService messageService;
//
//    @MessageMapping("/message/{roomId}")
//    public void message(MessageDto messageDto, @DestinationVariable String roomId) {
//        messageService.send(messageDto, roomId);
//    }
//
//    @MessageMapping("/run/{roomId}")
//    public void run(@DestinationVariable String roomId) {
//
//    }
//}
