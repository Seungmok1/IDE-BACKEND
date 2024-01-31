package everyide.webide.config.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import everyide.webide.config.auth.dto.request.LoginRequestDto;
import everyide.webide.config.auth.dto.response.UserDto;
import everyide.webide.config.auth.jwt.JwtTokenProvider;
import everyide.webide.config.auth.user.CustomUserDetails;
import everyide.webide.config.auth.user.CustomUserDetailsService;
import everyide.webide.user.UserRepository;
import everyide.webide.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationFilter authenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            CustomUserDetailsService customUserDetailsService,
            String url) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        setFilterProcessesUrl(url);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        log.info("loginRequestDto 초기화 완료");

        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
            log.info("Request 내용 스트림 변환 후 LoginRequestDto에 삽입");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 사용자의 로그인 정보를 기반으로 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword());
        log.info("UsernamePasswordAuthenticationToken 생성 완료");
        try {
            log.info("시도중");
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.info("Something Wrong");
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("크레덴셜 로그인 인증 성공");
        //토큰 발급 시작
        String token = jwtTokenProvider.createToken(authResult);
        String refresh = jwtTokenProvider.createRefreshToken(authResult);
        log.info(token);
        log.info(refresh);
        ObjectMapper om = new ObjectMapper();
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("RefreshToken", "Bearer " + refresh);
        log.info("헤더에 토큰 삽입 완료");
        String role = authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).findAny().orElse("");
        String userEmail = "";
        if(role.equals("ROLE_USER")){
            CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
            userEmail = customUserDetails.getUsername();
        }
        User user = customUserDetailsService.selcetUser(userEmail);
        user.setRefreshToken("Bearer " + refresh);
        userRepository.save(user);
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setAccessToken("Bearer " + token);
        userDto.setRefreshToken(user.getRefreshToken());
        log.info("Response Body에 User정보 반환 완료");
        String result = om.registerModule(new JavaTimeModule()).writeValueAsString(userDto);
        response.getWriter().write(result);
    }


}
