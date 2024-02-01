package everyide.webide.websocket.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Document(collection = "message")
@Getter
@ToString
@NoArgsConstructor
public class Message{

    private String id;
    private Long chatId;
    private String contentType;
    private String content;
    private Long senderId;
    private LocalDateTime sendDate;

    @Builder
    public Message(Long chatId, String contentType, String content, Long senderId) {
        id = UUID.randomUUID().toString();
        this.chatId = chatId;
        this.contentType = contentType;
        this.content = content;
        this.senderId = senderId;
        sendDate = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
}
