package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.CurrentLibidoAnswer;

import java.util.Optional;
import java.util.UUID;

public interface CurrentLibidoAnswerRepository {

    Try<Optional<CurrentLibidoAnswer>> find(Long id);

    Try<Optional<CurrentLibidoAnswer>> find(UUID quizExternalReference);

    Try<Long> save(CurrentLibidoAnswer currentLibidoAnswer);

    Try<Long> update(CurrentLibidoAnswer currentLibidoAnswer);
}