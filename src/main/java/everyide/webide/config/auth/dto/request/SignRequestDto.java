package everyide.webide.config.auth.dto.request;

import lombok.Data;

@Data
public class SignRequestDto {

    private String email;
    private String name;
    private String password;
    private String checkPassword;

}
