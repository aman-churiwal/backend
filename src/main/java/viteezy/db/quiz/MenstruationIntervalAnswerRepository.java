package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.MenstruationIntervalAnswer;

import java.util.Optional;
import java.util.UUID;

public interface MenstruationIntervalAnswerRepository {

    Try<Optional<MenstruationIntervalAnswer>> find(Long id);

    Try<Optional<MenstruationIntervalAnswer>> find(UUID quizExternalReference);

    Try<Long> save(MenstruationIntervalAnswer menstruationIntervalAnswer);

    Try<Long> update(MenstruationIntervalAnswer menstruationIntervalAnswer);
}