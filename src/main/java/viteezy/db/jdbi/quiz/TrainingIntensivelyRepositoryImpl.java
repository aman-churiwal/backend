package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TrainingIntensivelyRepository;
import viteezy.domain.quiz.TrainingIntensively;

import java.util.List;
import java.util.Optional;

public class TrainingIntensivelyRepositoryImpl implements TrainingIntensivelyRepository {

    private static final String SELECT_ALL = "SELECT training_intensivelys.* FROM training_intensivelys ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO training_intensivelys (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public TrainingIntensivelyRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TrainingIntensively>> find(Long id) {
        final HandleCallback<Optional<TrainingIntensively>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TrainingIntensively.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<TrainingIntensively>> findAll() {
        final HandleCallback<List<TrainingIntensively>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(TrainingIntensively.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TrainingIntensively trainingIntensively) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(trainingIntensively)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}