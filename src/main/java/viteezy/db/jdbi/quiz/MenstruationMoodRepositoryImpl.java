package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.MenstruationMoodRepository;
import viteezy.domain.quiz.MenstruationMood;

import java.util.List;
import java.util.Optional;

public class MenstruationMoodRepositoryImpl implements MenstruationMoodRepository {

    private static final String SELECT_ALL = "SELECT menstruation_moods.* FROM menstruation_moods ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO menstruation_moods (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public MenstruationMoodRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<MenstruationMood>> find(Long id) {
        final HandleCallback<Optional<MenstruationMood>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(MenstruationMood.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<MenstruationMood>> findAll() {
        final HandleCallback<List<MenstruationMood>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(MenstruationMood.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(MenstruationMood menstruationMood) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(menstruationMood)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}