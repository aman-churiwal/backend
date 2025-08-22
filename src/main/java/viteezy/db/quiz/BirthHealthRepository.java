package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.BirthHealth;

import java.util.List;
import java.util.Optional;

public interface BirthHealthRepository {

    Try<Optional<BirthHealth>> find(Long id);

    Try<List<BirthHealth>> findAll();

    Try<Long> save(BirthHealth birthHealth);

}