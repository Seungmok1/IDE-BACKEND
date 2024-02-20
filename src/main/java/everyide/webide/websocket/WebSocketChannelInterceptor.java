package everyide.webide.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("full message={}", message);
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//        String authToken = headerAccessor.getFirstNativeHeader("Authorization");
//
//        if (authToken != null && authToken.startsWith("Bearer ")) {
//            String token = authToken.substring(7);
//            if (!jwtTokenProvider.validateToken(token).equals("success")) {
//                return message;
//            }
//        }
//        log.error("토큰 오류");
//        return null;
        return message;
    }
}
