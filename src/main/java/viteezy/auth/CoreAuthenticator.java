package viteezy.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import viteezy.domain.dashboard.AuthToken;
import viteezy.domain.dashboard.User;
import viteezy.service.dashboard.AuthenticationService;
import viteezy.service.dashboard.JwtTokenService;
import viteezy.service.dashboard.UserService;
import viteezy.traits.EnforcePresenceTrait;
import viteezy.traits.ExceptionLoggerTrait;

import java.util.Optional;

public class CoreAuthenticator implements Authenticator<String, User>, ExceptionLoggerTrait, EnforcePresenceTrait {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreAuthenticator.class);

    @Autowired
    public CoreAuthenticator(AuthenticationService authenticationService,
                             UserService userService,
                             JwtTokenService jwtTokenService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        if (jwtTokenService.validateToken(token)) {
            return authenticationService.findToken(token)
                    .flatMap(this::updateTokenExpiration)
                    .flatMap(authToken -> userService.find(authToken.getUserId()))
                    .peekLeft(logException(LOGGER))
                    .getOrElse(Optional.empty());
        } else {
            return Optional.empty();
        }
    }

    private Either<Throwable, AuthToken> updateTokenExpiration(AuthToken authToken) {
        return authenticationService.updateLastAccessTime(authToken.getId());
    }
}
