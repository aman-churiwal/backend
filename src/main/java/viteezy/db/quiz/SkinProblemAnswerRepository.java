package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SkinProblemAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SkinProblemAnswerRepository {

    Try<Optional<SkinProblemAnswer>> find(Long id);

    Try<Optional<SkinProblemAnswer>> find(UUID quizExternalReference);

    Try<Long> save(SkinProblemAnswer skinProblemAnswer);

    Try<Long> update(SkinProblemAnswer skinProblemAnswer);
}