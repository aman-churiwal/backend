package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.HealthComplaintsAnswer;

import java.util.Optional;
import java.util.UUID;

public interface HealthComplaintsAnswerRepository {

    Try<Optional<HealthComplaintsAnswer>> find(Long id);

    Try<Optional<HealthComplaintsAnswer>> find(UUID quizExternalReference);

    Try<Long> save(HealthComplaintsAnswer healthComplaintsAnswer);

    Try<Long> update(HealthComplaintsAnswer healthComplaintsAnswer);
}