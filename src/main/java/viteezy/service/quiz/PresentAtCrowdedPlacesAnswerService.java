package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.PresentAtCrowdedPlacesAnswer;

import java.util.Optional;
import java.util.UUID;

public interface PresentAtCrowdedPlacesAnswerService {

    Either<Throwable, Optional<PresentAtCrowdedPlacesAnswer>> find(Long id);

    Either<Throwable, Optional<PresentAtCrowdedPlacesAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PresentAtCrowdedPlacesAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PresentAtCrowdedPlacesAnswer> update(CategorizedAnswer categorizedAnswer);

}