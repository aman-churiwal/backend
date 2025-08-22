package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.PresentAtCrowdedPlacesAnswer;

import java.util.Optional;
import java.util.UUID;

public interface PresentAtCrowdedPlacesAnswerRepository {

    Try<Optional<PresentAtCrowdedPlacesAnswer>> find(Long id);

    Try<Optional<PresentAtCrowdedPlacesAnswer>> find(UUID quizExternalReference);

    Try<Long> save(PresentAtCrowdedPlacesAnswer presentAtCrowdedPlacesAnswer);

    Try<Long> update(PresentAtCrowdedPlacesAnswer presentAtCrowdedPlacesAnswer);
}