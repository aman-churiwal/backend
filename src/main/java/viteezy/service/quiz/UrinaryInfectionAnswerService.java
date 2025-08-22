package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.UrinaryInfectionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface UrinaryInfectionAnswerService {

    Either<Throwable, Optional<UrinaryInfectionAnswer>> find(Long id);

    Either<Throwable, Optional<UrinaryInfectionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, UrinaryInfectionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, UrinaryInfectionAnswer> update(CategorizedAnswer categorizedAnswer);

}