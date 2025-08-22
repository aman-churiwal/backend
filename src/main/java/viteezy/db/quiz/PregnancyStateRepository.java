package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.PregnancyState;

import java.util.List;
import java.util.Optional;

public interface PregnancyStateRepository {

    Try<Optional<PregnancyState>> find(Long id);

    Try<List<PregnancyState>> findAll();

    Try<Long> save(PregnancyState pregnancyState);

}