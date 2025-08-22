package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.VitaminOpinionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface VitaminOpinionAnswerRepository {

    Try<Optional<VitaminOpinionAnswer>> find(Long id);

    Try<Optional<VitaminOpinionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(VitaminOpinionAnswer vitaminOpinionAnswer);

    Try<Long> update(VitaminOpinionAnswer vitaminOpinionAnswer);
}