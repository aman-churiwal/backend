package viteezy.service.dashboard;

import io.jsonwebtoken.Claims;
import viteezy.domain.dashboard.AuthToken;
import viteezy.domain.dashboard.User;

public interface JwtTokenService {

    AuthToken generateToken(User user);

    boolean validateToken(String token);
}
