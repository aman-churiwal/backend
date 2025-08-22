package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SportReason;

import java.util.List;
import java.util.Optional;

public interface SportReasonRepository {

    Try<Optional<SportReason>> find(Long id);

    Try<List<SportReason>> findAll();

    Try<Long> save(SportReason sportReason);

}