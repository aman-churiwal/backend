package viteezy.db.dashboard;

import io.vavr.control.Try;
import viteezy.domain.dashboard.AuthToken;

public interface AuthenticationRepository {

    Try<AuthToken> find(Long id);

    Try<AuthToken> findByToken(String token);

    Try<Integer> updateLastAccessTime(Long id);

    Try<Long> upsert(AuthToken token);
}
