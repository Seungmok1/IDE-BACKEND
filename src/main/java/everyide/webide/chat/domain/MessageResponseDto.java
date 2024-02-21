package everyide.webide.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MessageResponseDto {
    private Long userId;
    private String name;
    private String content;
}
