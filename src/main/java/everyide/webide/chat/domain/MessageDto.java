package everyide.webide.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MessageDto implements Serializable {
    private String roomId;
    private String contentType;
    private String content;
    private Long senderId;
}
