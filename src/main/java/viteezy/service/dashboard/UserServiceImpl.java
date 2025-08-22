package viteezy.service.dashboard;

import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.controller.dto.dashboard.JwtResponse;
import viteezy.controller.dto.dashboard.SignUpRequest;
import viteezy.db.dashboard.UserRepository;
import viteezy.domain.dashboard.User;
import viteezy.domain.dashboard.UserRoles;
import viteezy.traits.ExceptionLoggerTrait;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class UserServiceImpl implements UserService, ExceptionLoggerTrait {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final JwtTokenService jwtTokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    protected UserServiceImpl(UserRepository userRepository, AuthenticationService authenticationService, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Either<Throwable, Optional<User>> find(Long id) {
        return userRepository.findById(id)
                .toEither()
                .peekLeft(logException(LOGGER));
    }

    @Override
    public Either<Throwable, List<User>> findAll() {
        return userRepository.findAll()
                .toEither()
                .peekLeft(logException(LOGGER));
    }

    @Override
    public Either<Throwable, Optional<User>> findByCredentials(String email, String password) {
        return checkPassword()
                .apply(userRepository.findByEmail(email), password)
                .peekLeft(logException(LOGGER));
    }

    @Override
    public Either<Throwable, Optional<User>> upsert(SignUpRequest signUpRequest) {
        return userRepository
                .upsert(adaptAndHashPassword(signUpRequest))
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(logException(LOGGER));
    }

    @Override
    public Either<Throwable, JwtResponse> login(String email, String password) {
        return findByCredentials(email, password)
                .flatMap(optionalToNarrowedEitherAuthenticationException())
                .map(jwtTokenService::generateToken)
                .flatMap(authenticationService::upsert)
                .peekLeft(logException(LOGGER))
                .map(authToken -> new JwtResponse(authToken.getToken(), authToken.getUserId()));
    }

    private <T> Function<Optional<T>, Either<Throwable, T>> optionalToNarrowedEitherAuthenticationException() {
        return optional -> optional
                .<Either<Throwable, T>>map(Either::right)
                .orElseGet(() -> Either.left(new AuthenticationException("Username or password incorrect")));
    }

    private User adaptAndHashPassword(SignUpRequest signUpRequest) {
        return new User(null, signUpRequest.getEmail(), authenticationService.encryptPassword(signUpRequest.getPassword()),
                signUpRequest.getFirstName(), signUpRequest.getLastName(), LocalDateTime.now(), UserRoles.USER);
    }

    private Function<Long, Either<Throwable, ? extends Optional<User>>> retrieveById() {
        return entityId -> {
            Try<Optional<User>> entityTry = userRepository.findById(entityId);
            if (entityTry.isSuccess() && entityTry.get().isPresent()) {
                return Either.right(entityTry.get());
            } else if (entityTry.isSuccess() && entityTry.get().isEmpty()) {
                final String message = "User was saved but could not be retrieved from db";
                LOGGER.error("{}", message);
                return Either.left(new NoSuchElementException(message));
            } else {
                LOGGER.error(entityTry.getCause().toString());
                return Either.left(entityTry.getCause());
            }
        };
    }

    private BiFunction<Try<Optional<User>>, String, Either<Throwable, Optional<User>>> checkPassword() {
        return (entityTry, password) -> {
            if (entityTry.isSuccess() && entityTry.get().isPresent()) {
                if (authenticationService.checkPassword(password, entityTry.get().get().getPassword())) {
                    return Either.right(entityTry.get());
                } else {
                    return Either.left(new AuthenticationException("Username or password incorrect"));
                }
            } else if (entityTry.isSuccess() && entityTry.get().isEmpty()) {
                final String message = "User could not be retrieved from db";
                return Either.left(new NoSuchElementException(message));
            } else {
                LOGGER.error(entityTry.getCause().toString());
                return Either.left(entityTry.getCause());
            }
        };
    }
}
