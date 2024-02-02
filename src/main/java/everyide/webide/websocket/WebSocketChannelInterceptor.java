package everyide.webide.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

//@Slf4j
//@Component
//@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//        log.info("full message={}", message);
//
//        //TODO 인증 및 인가 기능 추가
//
//        return ChannelInterceptor.super.preSend(message, channel);
//    }
//
//    @EventListener
//    public void connect(SessionConnectEvent event) {
//        log.info("입장");
//    }
//
//    @EventListener
//    public void disconnect(SessionDisconnectEvent event) {
//        log.info("퇴장");
//    }
}
