package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.BingeEatingReason;

import java.util.List;
import java.util.Optional;

public interface BingeEatingReasonRepository {

    Try<Optional<BingeEatingReason>> find(Long id);

    Try<List<BingeEatingReason>> findAll();

    Try<Long> save(BingeEatingReason bingeEatingReason);

}