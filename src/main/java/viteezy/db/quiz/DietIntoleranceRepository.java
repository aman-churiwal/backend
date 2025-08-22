package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DietIntolerance;

import java.util.List;
import java.util.Optional;

public interface DietIntoleranceRepository {

    Try<Optional<DietIntolerance>> find(Long id);

    Try<List<DietIntolerance>> findAll();

    Try<Long> save(DietIntolerance dietIntolerance);

}