package everyide.webide.config.auth.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class UserResponse {
    private Long userId;
    private String name;
    private String email;

    public UserResponse(Long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
}
