package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.HelpGoalAnswer;

import java.util.Optional;
import java.util.UUID;

public interface HelpGoalAnswerRepository {

    Try<Optional<HelpGoalAnswer>> find(Long id);

    Try<Optional<HelpGoalAnswer>> find(UUID quizExternalReference);

    Try<Long> save(HelpGoalAnswer helpGoalAnswer);

    Try<Long> update(HelpGoalAnswer helpGoalAnswer);
}