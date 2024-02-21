package everyide.webide.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSession {
    private Long userId;
    private String name;
    private String email;
    private Long containerId;
}
