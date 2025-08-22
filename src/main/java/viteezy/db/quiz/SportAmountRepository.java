package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SportAmount;

import java.util.List;
import java.util.Optional;

public interface SportAmountRepository {

    Try<Optional<SportAmount>> find(Long id);

    Try<List<SportAmount>> findAll();

    Try<Long> save(SportAmount sportAmount);

}