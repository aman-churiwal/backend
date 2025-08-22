package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.MenstruationSideIssueAnswer;

import java.util.Optional;
import java.util.UUID;

public interface MenstruationSideIssueAnswerRepository {

    Try<Optional<MenstruationSideIssueAnswer>> find(Long id);

    Try<Optional<MenstruationSideIssueAnswer>> find(UUID quizExternalReference);

    Try<Long> save(MenstruationSideIssueAnswer menstruationSideIssueAnswer);

    Try<Long> update(MenstruationSideIssueAnswer menstruationSideIssueAnswer);
}