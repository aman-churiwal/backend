package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.LoginRepository;
import viteezy.domain.Login;

import java.time.LocalDateTime;

public class LoginRepositoryImpl implements LoginRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRepositoryImpl.class);

    private static final String SELECT_ALL = "SELECT * FROM login ";
    private static final String UPSERT_QUERY = "" +
            "INSERT INTO " +
            "login (email, token, last_updated, creation_timestamp) " +
            "VALUES (:email, :token, :lastUpdated, :lastUpdated) " +
            "ON DUPLICATE KEY UPDATE token = :token, last_updated = :lastUpdated, id = LAST_INSERT_ID(id)";
    private final Jdbi jdbi;

    public LoginRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Login> find(Long id) {
        final HandleCallback<Login, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Login.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Login> find(String email) {
        final HandleCallback<Login, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE email = :email")
                .bind("email", email)
                .mapTo(Login.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Login> saveOrUpdate(String email, String token) {
        final LocalDateTime lastUpdated = LocalDateTime.now();
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPSERT_QUERY)
                .bind("email", email)
                .bind("token", token)
                .bind("lastUpdated", lastUpdated)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }
}
