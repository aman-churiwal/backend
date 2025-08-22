package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.HealthyLifestyle;

import java.util.List;
import java.util.Optional;

public interface HealthyLifestyleRepository {

    Try<Optional<HealthyLifestyle>> find(Long id);

    Try<List<HealthyLifestyle>> findAll();

    Try<Long> save(HealthyLifestyle healthyLifestyle);

}