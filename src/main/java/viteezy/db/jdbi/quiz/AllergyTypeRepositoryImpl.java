package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.AllergyTypeRepository;
import viteezy.domain.quiz.AllergyType;

import java.util.List;
import java.util.Optional;

public class AllergyTypeRepositoryImpl implements AllergyTypeRepository {

    private static final String SELECT_ALL = "SELECT allergy_types.* FROM allergy_types ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO allergy_types (name, code) " +
            "VALUES (:name, :code)";

    private final Jdbi jdbi;

    public AllergyTypeRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<AllergyType>> find(Long id) {
        final HandleCallback<Optional<AllergyType>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(AllergyType.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<AllergyType>> findAll() {
        final HandleCallback<List<AllergyType>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(AllergyType.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(AllergyType allergyType) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(allergyType)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}