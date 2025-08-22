package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.NailImprovementAnswer;

import java.util.Optional;
import java.util.UUID;

public interface NailImprovementAnswerRepository {

    Try<Optional<NailImprovementAnswer>> find(Long id);

    Try<Optional<NailImprovementAnswer>> find(UUID quizExternalReference);

    Try<Long> save(NailImprovementAnswer nailImprovementAnswer);

    Try<Long> update(NailImprovementAnswer nailImprovementAnswer);
}