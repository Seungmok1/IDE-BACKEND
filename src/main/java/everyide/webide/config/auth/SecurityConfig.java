package everyide.webide.config.auth;

import everyide.webide.config.auth.filter.JwtAuthenticationFilter;
import everyide.webide.config.auth.filter.JwtAuthorizationFilter;
import everyide.webide.config.auth.jwt.JwtTokenProvider;
import everyide.webide.config.auth.user.CustomUserDetails;
import everyide.webide.config.auth.user.CustomUserDetailsService;
import everyide.webide.config.auth.user.oauth2.CustomOAuth2UserService;
import everyide.webide.user.UserRepository;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //Spring Security에서 제공하는 클래스, 비밀번호를 안전하게 해싱
    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // 스프링 시큐리티의 HTTP 보안 설정에서 CSRF 보호기능 비활성화 (Cross-Site Request Forgery, 사이트 간 요청 위조)
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화 : API 서버와 같이 폼 로그인이 필요없는 방식) 비활성화
                .cors(configurer -> configurer.configurationSource(corsConfigurationSource())) // (Cross-Origin Resource Sharing) 웹 앱의 보안을 유지하면서 다른 출처의 리소스 요청을 허용하도록 설정
                .sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 서버가 사용자 세션을 유지하지 않음. 서버의 확장성을 높이고 client와 server 간의 결합도를 낮춘다.
                .addFilter(new JwtAuthenticationFilter(jwtTokenProvider, userRepository, authenticationManager(customUserDetailsService), customUserDetailsService, "/auth/token"))
                .addFilterAfter(new JwtAuthorizationFilter(userRepository, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/everyide/**")
                        ).hasRole("USER")
                        .anyRequest().permitAll());
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
