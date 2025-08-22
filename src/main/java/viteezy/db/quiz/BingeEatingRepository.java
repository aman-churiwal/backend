package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.BingeEating;

import java.util.List;
import java.util.Optional;

public interface BingeEatingRepository {

    Try<Optional<BingeEating>> find(Long id);

    Try<List<BingeEating>> findAll();

    Try<Long> save(BingeEating bingeEating);

}