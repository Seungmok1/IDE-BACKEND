package everyide.webide.config.auth.jwt;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

import static everyide.webide.config.auth.jwt.JwtProperties.SECRET_KEY;

public class JwtTokenProvider {

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
}
