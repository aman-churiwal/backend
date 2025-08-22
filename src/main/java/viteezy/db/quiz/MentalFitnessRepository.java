package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.MentalFitness;

import java.util.List;
import java.util.Optional;

public interface MentalFitnessRepository {

    Try<Optional<MentalFitness>> find(Long id);

    Try<List<MentalFitness>> findAll();

    Try<Long> save(MentalFitness mentalFitness);

}