package everyide.webide.chat.domain;

import lombok.Getter;

@Getter
public class MessageRequestDto {
    private Long userId;
    private String content;
}
