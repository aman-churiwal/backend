package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.OftenHavingFlu;

import java.util.List;
import java.util.Optional;

public interface OftenHavingFluRepository {

    Try<Optional<OftenHavingFlu>> find(Long id);

    Try<List<OftenHavingFlu>> findAll();

    Try<Long> save(OftenHavingFlu oftenHavingFlu);

}