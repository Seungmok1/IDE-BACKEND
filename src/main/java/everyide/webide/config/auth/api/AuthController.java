package everyide.webide.config.auth.api;

import everyide.webide.config.auth.dto.request.PasswordChangeRequest;
import everyide.webide.config.auth.dto.request.SignRequestDto;
import everyide.webide.config.auth.user.CustomUserDetailsService;
import everyide.webide.user.UserRepository;
import everyide.webide.user.UserService;
import everyide.webide.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUpUser(@RequestBody SignRequestDto signRequestDto) {
        User user = User.builder()
                .name(signRequestDto.getName())
                .email(signRequestDto.getUsername())
                .password(signRequestDto.getPassword())
                .build();
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/user/info")
    public ResponseEntity<?> patchUser(@RequestBody SignRequestDto signRequestDto) {
        return ResponseEntity.ok("success");
    }

    @PostMapping("/user/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(passwordChangeRequest.getEmail(), passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
        return ResponseEntity.ok().body("Password changed successfully");
    }
}
