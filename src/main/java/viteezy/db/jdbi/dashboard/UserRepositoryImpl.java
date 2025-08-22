package viteezy.db.jdbi.dashboard;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.dashboard.UserRepository;
import viteezy.domain.dashboard.User;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private static final String SELECT_ALL = "SELECT * FROM users";
    private static final String FIND_BY_ID = SELECT_ALL + " WHERE id = :id";
    private static final String FIND_BY_EMAIL = SELECT_ALL + " WHERE email = :email";
    private static final String UPSERT_QUERY = "" +
            "INSERT INTO users (email, password, first_name, last_name, creation_date, role)" +
            "VALUES(:email, :password, :firstName, :lastName, :creationDate, :role)" +
            "ON DUPLICATE KEY UPDATE email=:email, password=:password, first_name=:firstName, last_name=:lastName, role=:role, id = LAST_INSERT_ID(id)";

    private final Jdbi jdbi;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    public UserRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<List<User>> findAll() {
        final HandleCallback<List<User>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(User.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<User>> findById(Long id) {
        final HandleCallback<Optional<User>, RuntimeException> queryCallback = handle -> handle
                .createQuery(FIND_BY_ID)
                .bind("id", id)
                .mapTo(User.class)
                .findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<User>> findByEmail(String email) {
        final HandleCallback<Optional<User>, RuntimeException> queryCallback = handle -> handle
                .createQuery(FIND_BY_EMAIL)
                .bind("email", email)
                .mapTo(User.class)
                .findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> upsert(User user) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPSERT_QUERY)
                .bindBean(user)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }
}
