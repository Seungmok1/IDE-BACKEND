package everyide.webide.config.auth.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {

    public String username; //이메일
    public String password;

}
