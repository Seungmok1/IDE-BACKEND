package everyide.webide.websocket.chat.domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class MessageDto implements Serializable {
    private Long chatId;
    private String contentType;
    private String content;
    private Long senderId;
}
