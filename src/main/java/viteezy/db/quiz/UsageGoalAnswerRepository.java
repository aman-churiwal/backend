package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.UsageGoalAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsageGoalAnswerRepository {

    Try<Optional<UsageGoalAnswer>> find(Long id);

    Try<Optional<UsageGoalAnswer>> find(UUID quizExternalReference, Long usageGoalId);

    Try<List<UsageGoalAnswer>> find(UUID quizExternalReference);

    Try<Long> save(UsageGoalAnswer usageGoalAnswer);

    Try<Void> delete(Long id);
}