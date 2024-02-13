package everyide.webide.config.auth.jwt;


import com.amazonaws.services.kms.model.NotFoundException;
import everyide.webide.user.domain.User;
import everyide.webide.user.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import static everyide.webide.config.auth.jwt.JwtProperties.SECRET_KEY;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserRepository userRepository;

    public String createToken(Authentication authentication) {

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("roles", roles)
                .expiration(new Date(new Date().getTime() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }


    public String createRefreshToken(Authentication authentication) {

        return Jwts.builder()
                .expiration(new Date(new Date().getTime() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String validateToken(String token) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            return "Success";
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return "signature is wrong.";
        } catch(ExpiredJwtException e) {
            return "token expired.";
        } catch (UnsupportedJwtException e) {
            return "token is unsupported";
        } catch (IllegalArgumentException e) {
            return "token is wrong";
        }
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        Optional<User> optionalUser = userRepository.findAllByRefreshToken(refreshToken);
        User user = optionalUser.orElseThrow();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", user.getRole())
                .expiration(new Date(new Date().getTime() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateRefreshToken(String token) {
        Optional<User> optionalUser = userRepository.findAllByRefreshToken(token);
        return optionalUser.isPresent();
    }
}
