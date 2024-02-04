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
//    private final ChatService chatService;
//
//    @SubscribeMapping
//    public void enter(SimpMessageHeaderAccessor headerAccessor) {
//        System.out.println("check");
//        System.out.println(headerAccessor);
//        chatService.enter();
//    }
//
//    @MessageMapping("/message/{roomId}")
//    public void message(MessageDto messageDto, @DestinationVariable String roomId){
//        messageService.send(messageDto, roomId);
//    }
//
////    private String getSessionId(SimpMessageHeaderAccessor headerAccessor) {
////        ConcurrentHashMap<String, String> simpSessionAttributes = (ConcurrentHashMap<String, String>) headerAccessor.get
////    }
//}
