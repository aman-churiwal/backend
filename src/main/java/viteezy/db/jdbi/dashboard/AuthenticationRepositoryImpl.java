package viteezy.db.jdbi.dashboard;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.dashboard.AuthenticationRepository;
import viteezy.domain.dashboard.AuthToken;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class AuthenticationRepositoryImpl implements AuthenticationRepository {

    private static final String SELECT_ALL = "SELECT * FROM tokens";
    private static final String WHERE_ID = " WHERE id = :id";
    private static final String FIND_BY_ID = SELECT_ALL + WHERE_ID;
    private static final String FIND_BY_TOKEN = SELECT_ALL + " WHERE token = :token";
    private static final String UPDATE_LAST_SEEN_BASE_QUERY = "UPDATE tokens SET last_access = :lastAccess";
    private static final String UPSERT_QUERY = "" +
            "INSERT INTO tokens (token, user_id, user_role, last_access)" +
            "VALUES(:token, :userId, :userRole, :lastAccess)" +
            "ON DUPLICATE KEY UPDATE token=:token, user_role=:userRole, last_access=NOW(), id = LAST_INSERT_ID(id)";

    private final Jdbi jdbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationRepository.class);

    public AuthenticationRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<AuthToken> find(Long id) {
        final HandleCallback<AuthToken, RuntimeException> queryCallback = handle -> handle
                .createQuery(FIND_BY_ID)
                .bind("id", id)
                .mapTo(AuthToken.class)
                .one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<AuthToken> findByToken(String token) {
        final HandleCallback<AuthToken, RuntimeException> queryCallback = handle -> handle
                .createQuery(FIND_BY_TOKEN)
                .bind("token", token)
                .mapTo(AuthToken.class)
                .one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Integer> updateLastAccessTime(Long id) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_LAST_SEEN_BASE_QUERY + WHERE_ID)
                .bind("id", id)
                .bind("lastAccess", LocalDateTime.now())
                .execute();
        final Supplier<Integer> updateQuerySupplier = () -> jdbi.withHandle(queryCallback);
        return Try.ofSupplier(updateQuerySupplier);
    }

    @Override
    public Try<Long> upsert(AuthToken token) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPSERT_QUERY)
                .bindBean(token)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }
}
