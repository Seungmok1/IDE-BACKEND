package everyide.webide.websocket.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import everyide.webide.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message extends BaseEntity implements Serializable {

    private String id;
    @NonNull
    private Long chatId;
    @NonNull
    private String contentType;
    @NonNull
    private String content;
    private String sender;
    private Long senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Chat chat;

    public void setId(String id) {
        this.id = id;
    }

}
