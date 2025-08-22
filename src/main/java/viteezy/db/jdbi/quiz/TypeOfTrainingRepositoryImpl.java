package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.TypeOfTrainingRepository;
import viteezy.domain.quiz.TypeOfTraining;

import java.util.List;
import java.util.Optional;

public class TypeOfTrainingRepositoryImpl implements TypeOfTrainingRepository {

    private static final String SELECT_ALL = "SELECT type_of_trainings.* FROM type_of_trainings ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO type_of_trainings (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public TypeOfTrainingRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<TypeOfTraining>> find(Long id) {
        final HandleCallback<Optional<TypeOfTraining>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(TypeOfTraining.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<TypeOfTraining>> findAll() {
        final HandleCallback<List<TypeOfTraining>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(TypeOfTraining.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(TypeOfTraining typeOfTraining) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(typeOfTraining)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}