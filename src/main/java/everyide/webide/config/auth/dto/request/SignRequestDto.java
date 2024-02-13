package everyide.webide.config.auth.dto.request;

import lombok.Data;

@Data
public class SignRequestDto {

    private String email;
    private String name;
    private String password;
    private String checkPassword;

    public SignRequestDto(String email, String name, String password, String checkPassword) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.checkPassword = checkPassword;
    }
}
