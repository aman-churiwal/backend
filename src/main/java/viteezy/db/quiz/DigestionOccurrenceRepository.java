package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DigestionOccurrence;

import java.util.List;
import java.util.Optional;

public interface DigestionOccurrenceRepository {

    Try<Optional<DigestionOccurrence>> find(Long id);

    Try<List<DigestionOccurrence>> findAll();

    Try<Long> save(DigestionOccurrence digestionOccurrence);

}