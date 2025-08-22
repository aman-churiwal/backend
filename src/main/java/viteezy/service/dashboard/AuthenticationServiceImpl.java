package viteezy.service.dashboard;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.dashboard.AuthenticationRepository;
import viteezy.domain.dashboard.AuthToken;
import viteezy.traits.EnforcePresenceTrait;
import viteezy.traits.ExceptionLoggerTrait;

public class AuthenticationServiceImpl implements AuthenticationService, ExceptionLoggerTrait, EnforcePresenceTrait {

    private final AuthenticationRepository authenticationRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    protected AuthenticationServiceImpl(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public Either<Throwable, AuthToken> find(Long id) {
        return authenticationRepository.find(id)
                .toEither()
                .peekLeft(logException(LOGGER));
    }

    @Override
    public Either<Throwable, AuthToken> findToken(String token) {
        return authenticationRepository.findByToken(token)
                .toEither()
                .peekLeft(logException(LOGGER));
    }

    @Override
    public String encryptPassword(String password) {
        return BCrypt.with(BCrypt.Version.VERSION_2Y)
                .hashToString(12, password.toCharArray());
    }

    @Override
    public Boolean checkPassword(String password, String hashedPassword) {
        return BCrypt
                .verifyer()
                .verify(password.toCharArray(), hashedPassword)
                .verified;
    }

    @Override
    public Either<Throwable, AuthToken> updateLastAccessTime(Long id) {
        return authenticationRepository.updateLastAccessTime(id)
                .toEither()
                .peekLeft(logException(LOGGER))
                .flatMap(__ -> find(id));
    }

    @Override
    public Either<Throwable, AuthToken> upsert(AuthToken token) {
        return authenticationRepository.upsert(token)
                .toEither()
                .peekLeft(logException(LOGGER))
                .flatMap(__ -> findToken(token.getToken()));
    }
}
