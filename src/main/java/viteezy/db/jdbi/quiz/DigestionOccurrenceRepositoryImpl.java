package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.DigestionOccurrenceRepository;
import viteezy.domain.quiz.DigestionOccurrence;

import java.util.List;
import java.util.Optional;

public class DigestionOccurrenceRepositoryImpl implements DigestionOccurrenceRepository {

    private static final String SELECT_ALL = "SELECT digestion_occurrences.* FROM digestion_occurrences ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO digestion_occurrences (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public DigestionOccurrenceRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<DigestionOccurrence>> find(Long id) {
        final HandleCallback<Optional<DigestionOccurrence>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(DigestionOccurrence.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<DigestionOccurrence>> findAll() {
        final HandleCallback<List<DigestionOccurrence>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(DigestionOccurrence.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(DigestionOccurrence digestionOccurrence) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(digestionOccurrence)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}