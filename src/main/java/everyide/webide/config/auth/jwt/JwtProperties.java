package everyide.webide.config.auth.jwt;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public interface JwtProperties {
    SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 1 * 1000L;
    long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L;
}
