package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.MentalFitnessAnswer;

import java.util.Optional;
import java.util.UUID;

public interface MentalFitnessAnswerRepository {

    Try<Optional<MentalFitnessAnswer>> find(Long id);

    Try<Optional<MentalFitnessAnswer>> find(UUID quizExternalReference);

    Try<Long> save(MentalFitnessAnswer mentalFitnessAnswer);

    Try<Long> update(MentalFitnessAnswer mentalFitnessAnswer);
}