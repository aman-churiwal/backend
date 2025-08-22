package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.MentalFitnessRepository;
import viteezy.domain.quiz.MentalFitness;

import java.util.List;
import java.util.Optional;

public class MentalFitnessRepositoryImpl implements MentalFitnessRepository {

    private static final String SELECT_ALL = "SELECT mental_fitness.* FROM mental_fitness ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO mental_fitness (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public MentalFitnessRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<MentalFitness>> find(Long id) {
        final HandleCallback<Optional<MentalFitness>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(MentalFitness.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<MentalFitness>> findAll() {
        final HandleCallback<List<MentalFitness>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(MentalFitness.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(MentalFitness mentalFitness) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(mentalFitness)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}