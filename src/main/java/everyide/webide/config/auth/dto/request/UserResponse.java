package everyide.webide.config.auth.dto.request;

import lombok.Data;

@Data
public class UserResponse {
    private String name;
    private String email;

    public UserResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
