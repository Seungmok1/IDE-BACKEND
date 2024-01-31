package everyide.webide.config.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import everyide.webide.config.auth.jwt.JwtTokenProvider;
import everyide.webide.config.auth.user.CustomUserDetails;
import everyide.webide.model.dao.User;
import everyide.webide.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("RefreshToken");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String tokenValidationResult = jwtTokenProvider.validateToken(accessToken.replace("Bearer ", ""));
            if ("token expired.".equals(tokenValidationResult)) {
                log.info("AccessToken이 만료됨");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } else if (!"Success".equals(tokenValidationResult)) {
                log.info("토큰이 만료되었으니 다른 토큰으로 인가 부탁드립니다.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // 토큰이 유효한 경우 사용자 인증 처리
            Authentication authentication = getUsernamePasswordAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            // 리프레시 토큰으로 새 엑세스 토큰 발급
            log.info("리프레시 토큰을 이용해서 새 토큰 발급 중 ...");
            String newAccessToken = jwtTokenProvider.generateAccessTokenFromRefreshToken(refreshToken);
            response.setHeader("NewAccessToken","Bearer " + newAccessToken); // 새 토큰을 헤더에 추가
            response.setHeader("expTime", "3600");
        }
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization")
                .replace("Bearer ", "");

        Claims claims = jwtTokenProvider.getClaims(token);
        String email = claims.getSubject();

        ObjectMapper om = new ObjectMapper();

        log.info(email);

        if(email != null) {
            Optional<User> oUser = userRepository.findByEmail(email);
            User user = oUser.get();
            CustomUserDetails customUserDetails = CustomUserDetails.create(user);

            // 이 부분 우리는 OAuth인지 일반 로그인인지 구분할 필요가 있음
            // 이 부분 설명 좀 필요함
        }
    }
}
