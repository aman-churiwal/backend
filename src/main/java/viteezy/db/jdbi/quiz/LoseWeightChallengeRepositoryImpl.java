package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.LoseWeightChallengeRepository;
import viteezy.domain.quiz.LoseWeightChallenge;

import java.util.List;
import java.util.Optional;

public class LoseWeightChallengeRepositoryImpl implements LoseWeightChallengeRepository {

    private static final String SELECT_ALL = "SELECT lose_weight_challenges.* FROM lose_weight_challenges ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO lose_weight_challenges (name, code, subtitle) " +
            "VALUES (:name, :code, :subtitle)";

    private final Jdbi jdbi;

    public LoseWeightChallengeRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<LoseWeightChallenge>> find(Long id) {
        final HandleCallback<Optional<LoseWeightChallenge>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(LoseWeightChallenge.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<LoseWeightChallenge>> findAll() {
        final HandleCallback<List<LoseWeightChallenge>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(LoseWeightChallenge.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(LoseWeightChallenge loseWeightChallenge) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(loseWeightChallenge)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

}