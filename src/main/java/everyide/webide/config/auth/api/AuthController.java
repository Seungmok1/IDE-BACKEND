package everyide.webide.config.auth.api;

import everyide.webide.config.auth.dto.request.PasswordChangeRequest;
import everyide.webide.config.auth.dto.request.SignRequestDto;
import everyide.webide.config.auth.dto.request.UserResponse;
import everyide.webide.config.auth.jwt.JwtTokenProvider;
import everyide.webide.user.UserService;
import everyide.webide.user.domain.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/signup")
    public String signupForm() {
        // 회원가입 폼을 반환하는 로직
        return "signup";
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SignRequestDto signRequestDto) {
        userService.signUpUser(signRequestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/user/info")
    public ResponseEntity<?> patchUser(@RequestBody SignRequestDto signRequestDto) {
        return ResponseEntity.ok("success");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader(value="Authorization") String token, HttpServletResponse response) {
        Claims claims = jwtTokenProvider.getClaims(token.substring(7));
        String email = claims.getSubject();
        userService.clearRefreshToken(email);

        Cookie cookie = new Cookie("RefreshToken", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok("User logged out successfully");
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/user/updatepassword")
    public UserResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // JWT에서 사용자의 이메일 가져오기

        // 이메일을 사용하여 사용자 정보 조회
        User user = userService.findByEmail(email);

        // 필요한 정보만 UserResponse 객체에 담아 반환
        return new UserResponse(user.getName(), user.getEmail());
    }



    @PatchMapping("/user/updatepassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(passwordChangeRequest.getEmail(), passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
        return ResponseEntity.ok().body("Password changed successfully");
    }
}
