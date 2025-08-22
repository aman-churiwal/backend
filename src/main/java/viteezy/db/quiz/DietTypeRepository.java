package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DietType;

import java.util.List;
import java.util.Optional;

public interface DietTypeRepository {

    Try<Optional<DietType>> find(Long id);

    Try<List<DietType>> findAll();

    Try<Long> save(DietType dietType);

}