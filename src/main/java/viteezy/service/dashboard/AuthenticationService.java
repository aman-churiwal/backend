package viteezy.service.dashboard;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dashboard.AuthToken;

public interface AuthenticationService {

    Either<Throwable, AuthToken> find(Long id);

    Either<Throwable, AuthToken> findToken(String token);

    String encryptPassword(String password);

    Boolean checkPassword(String password, String hashedPassword);

    @Transactional(transactionManager = "transactionManager", isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AuthToken> updateLastAccessTime(Long id);

    @Transactional(transactionManager = "transactionManager", isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AuthToken> upsert(AuthToken token);
}
