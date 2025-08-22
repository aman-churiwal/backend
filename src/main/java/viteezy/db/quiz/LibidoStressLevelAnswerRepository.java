package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.LibidoStressLevelAnswer;

import java.util.Optional;
import java.util.UUID;

public interface LibidoStressLevelAnswerRepository {

    Try<Optional<LibidoStressLevelAnswer>> find(Long id);

    Try<Optional<LibidoStressLevelAnswer>> find(UUID quizExternalReference);

    Try<Long> save(LibidoStressLevelAnswer libidoStressLevelAnswer);

    Try<Long> update(LibidoStressLevelAnswer libidoStressLevelAnswer);
}