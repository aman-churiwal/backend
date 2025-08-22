package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.ThirtyMinutesOfSunAnswer;

import java.util.Optional;
import java.util.UUID;

public interface ThirtyMinutesOfSunAnswerService {

    Either<Throwable, Optional<ThirtyMinutesOfSunAnswer>> find(Long id);

    Either<Throwable, Optional<ThirtyMinutesOfSunAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ThirtyMinutesOfSunAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ThirtyMinutesOfSunAnswer> update(CategorizedAnswer categorizedAnswer);

}