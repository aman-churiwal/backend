package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.NailImprovement;

import java.util.List;
import java.util.Optional;

public interface NailImprovementRepository {

    Try<Optional<NailImprovement>> find(Long id);

    Try<List<NailImprovement>> findAll();

    Try<Long> save(NailImprovement nailImprovement);

}