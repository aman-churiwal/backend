package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.EasternMedicineOpinionRepository;
import viteezy.domain.quiz.EasternMedicineOpinion;

import java.util.List;
import java.util.Optional;

public class EasternMedicineOpinionRepositoryImpl implements EasternMedicineOpinionRepository {

    private static final String SELECT_ALL = "SELECT eastern_medicine_opinions.* FROM eastern_medicine_opinions ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO eastern_medicine_opinions (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public EasternMedicineOpinionRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<EasternMedicineOpinion>> find(Long id) {
        final HandleCallback<Optional<EasternMedicineOpinion>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(EasternMedicineOpinion.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<EasternMedicineOpinion>> findAll() {
        final HandleCallback<List<EasternMedicineOpinion>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(EasternMedicineOpinion.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(EasternMedicineOpinion easternMedicineOpinion) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(easternMedicineOpinion)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}