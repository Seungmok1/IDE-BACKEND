package everyide.webide.websocket.chat.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Document(collection = "message")
@Getter
@ToString
@NoArgsConstructor
public class Message{

    private String id;
    private String roomId;
    private String contentType;
    private String content;
    private Long senderId;
    private LocalDateTime sendDate;

    @Builder
    public Message(String roomId, String contentType, String content, Long senderId) {
        id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.contentType = contentType;
        this.content = content;
        this.senderId = senderId;
        sendDate = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
}
