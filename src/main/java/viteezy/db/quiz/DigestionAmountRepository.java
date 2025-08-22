package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DigestionAmount;

import java.util.List;
import java.util.Optional;

public interface DigestionAmountRepository {

    Try<Optional<DigestionAmount>> find(Long id);

    Try<List<DigestionAmount>> findAll();

    Try<Long> save(DigestionAmount digestionAmount);

}