package viteezy.db.jdbi.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.quiz.MenstruationSideIssueAnswerRepository;
import viteezy.domain.quiz.MenstruationSideIssueAnswer;

import java.util.Optional;
import java.util.UUID;

public class MenstruationSideIssueAnswerRepositoryImpl implements MenstruationSideIssueAnswerRepository {

    private static final String SELECT_ALL = "SELECT menstruation_side_issue_answers.* FROM menstruation_side_issue_answers ";
    private static final String SELECT_BY_QUIZ_EXT_REF = SELECT_ALL +
            " JOIN quiz q on menstruation_side_issue_answers.quiz_id = q.id " +
            " WHERE q.external_reference = :quizExternalReference";

    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE menstruation_side_issue_answers " +
            "SET menstruation_side_issue_id = :menstruationSideIssueId, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_QUIZ_ID = UPDATE_BASE_QUERY + "WHERE quiz_id = :quizId";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO menstruation_side_issue_answers(quiz_id, menstruation_side_issue_id) " +
            "VALUES(:quizId, :menstruationSideIssueId);";

    private final Jdbi jdbi;

    public MenstruationSideIssueAnswerRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Optional<MenstruationSideIssueAnswer>> find(Long id) {
        final HandleCallback<Optional<MenstruationSideIssueAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(MenstruationSideIssueAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<MenstruationSideIssueAnswer>> find(UUID quizExternalReference) {
        final HandleCallback<Optional<MenstruationSideIssueAnswer>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_BY_QUIZ_EXT_REF)
                .bind("quizExternalReference", quizExternalReference)
                .mapTo(MenstruationSideIssueAnswer.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(MenstruationSideIssueAnswer menstruationSideIssueAnswer) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(menstruationSideIssueAnswer)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(MenstruationSideIssueAnswer menstruationSideIssueAnswer) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_QUIZ_ID)
                .bindBean(menstruationSideIssueAnswer)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(integer -> menstruationSideIssueAnswer.getId());
    }
}