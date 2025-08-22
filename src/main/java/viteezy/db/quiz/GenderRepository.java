package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.Gender;

import java.util.List;
import java.util.Optional;

public interface GenderRepository {

    Try<Optional<Gender>> find(Long id);

    Try<List<Gender>> findAll();

    Try<Long> save(Gender gender);

}