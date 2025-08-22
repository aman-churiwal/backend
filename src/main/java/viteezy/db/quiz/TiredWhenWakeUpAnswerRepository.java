package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TiredWhenWakeUpAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TiredWhenWakeUpAnswerRepository {

    Try<Optional<TiredWhenWakeUpAnswer>> find(Long id);

    Try<Optional<TiredWhenWakeUpAnswer>> find(UUID quizExternalReference);

    Try<Long> save(TiredWhenWakeUpAnswer tiredWhenWakeUpAnswer);

    Try<Long> update(TiredWhenWakeUpAnswer tiredWhenWakeUpAnswer);
}