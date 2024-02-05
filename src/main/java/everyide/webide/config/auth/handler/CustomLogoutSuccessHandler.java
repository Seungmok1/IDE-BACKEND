package everyide.webide.config.auth.handler;

import everyide.webide.config.auth.jwt.JwtTokenProvider;
import everyide.webide.config.auth.user.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            log.info("자 인자 토큰으로 아이디 찾는다={}", token);
            String email = jwtTokenProvider.getClaims(token.substring(7)).getSubject();
            log.info("내가 안뜨면 토큰이 문제여");
            userService.clearRefreshToken(email);
        }

        Cookie cookie = new Cookie("RefreshToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
    }

}