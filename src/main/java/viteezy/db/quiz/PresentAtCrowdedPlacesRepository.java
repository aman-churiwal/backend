package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.PresentAtCrowdedPlaces;

import java.util.List;
import java.util.Optional;

public interface PresentAtCrowdedPlacesRepository {

    Try<Optional<PresentAtCrowdedPlaces>> find(Long id);

    Try<List<PresentAtCrowdedPlaces>> findAll();

    Try<Long> save(PresentAtCrowdedPlaces presentAtCrowdedPlaces);

}