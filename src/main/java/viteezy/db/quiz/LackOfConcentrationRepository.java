package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.LackOfConcentration;

import java.util.List;
import java.util.Optional;

public interface LackOfConcentrationRepository {

    Try<Optional<LackOfConcentration>> find(Long id);

    Try<List<LackOfConcentration>> findAll();

    Try<Long> save(LackOfConcentration lackOfConcentration);

}