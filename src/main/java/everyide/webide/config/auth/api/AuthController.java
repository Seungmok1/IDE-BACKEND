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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

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

    @GetMapping("/refresh")
    public String refresh() {
        return "API End-point for Refresh Token";
    }

    @PostMapping("/user/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean passwordChanged = userService.changePassword(passwordChangeRequest.getEmail(), passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
            if (!passwordChanged) {
                // 비밀번호 변경 시도 실패 (예: 기존 비밀번호 불일치)
                response.put("status", true);
                response.put("message", "Current password is incorrect.");
                // 로그인 상태 유지를 위해 BadRequest 응답
                return ResponseEntity.badRequest().body(response);
            }
            // 비밀번호 변경 성공
            response.put("status", false);
            response.put("message", "Password changed successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 처리
            response.put("status", true);
            response.put("message", e.getMessage());
            // 서버 에러 상황에서도 클라이언트에 적절한 응답을 제공
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
