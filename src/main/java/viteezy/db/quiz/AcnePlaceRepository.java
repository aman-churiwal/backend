package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AcnePlace;

import java.util.List;
import java.util.Optional;

public interface AcnePlaceRepository {

    Try<Optional<AcnePlace>> find(Long id);

    Try<List<AcnePlace>> findAll();

    Try<Long> save(AcnePlace acnePlace);

}