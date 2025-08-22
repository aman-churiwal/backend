package viteezy.service.dashboard;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.dashboard.JwtConfiguration;
import viteezy.domain.dashboard.AuthToken;
import viteezy.domain.dashboard.User;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

public class JwtTokenServiceImpl implements JwtTokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenService.class);
    private final JwtConfiguration jwtConfiguration;
    private final SecretKey secretKey;

    protected JwtTokenServiceImpl(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfiguration.getSecret()));
    }

    @Override
    public AuthToken generateToken(User user) {
        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtConfiguration.getExpiration() * 1000L))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
        return new AuthToken(null, token, user.getId(),
                user.getRole().toString(), LocalDateTime.now());
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            LOGGER.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }
}
