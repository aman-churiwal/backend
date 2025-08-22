package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.NewProductAvailable;

import java.util.List;
import java.util.Optional;

public interface NewProductAvailableRepository {

    Try<Optional<NewProductAvailable>> find(Long id);

    Try<List<NewProductAvailable>> findAll();

    Try<Long> save(NewProductAvailable newProductAvailable);

}