package everyide.webide.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Session {
    private Long userId;
    private Long containerId;
}
