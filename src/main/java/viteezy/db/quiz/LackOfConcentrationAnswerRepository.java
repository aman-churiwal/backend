package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.LackOfConcentrationAnswer;

import java.util.Optional;
import java.util.UUID;

public interface LackOfConcentrationAnswerRepository {

    Try<Optional<LackOfConcentrationAnswer>> find(Long id);

    Try<Optional<LackOfConcentrationAnswer>> find(UUID quizExternalReference);

    Try<Long> save(LackOfConcentrationAnswer lackOfConcentrationAnswer);

    Try<Long> update(LackOfConcentrationAnswer lackOfConcentrationAnswer);
}