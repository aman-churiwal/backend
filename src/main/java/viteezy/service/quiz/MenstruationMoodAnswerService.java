package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.MenstruationMoodAnswer;

import java.util.Optional;
import java.util.UUID;

public interface MenstruationMoodAnswerService {

    Either<Throwable, Optional<MenstruationMoodAnswer>> find(Long id);

    Either<Throwable, Optional<MenstruationMoodAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationMoodAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationMoodAnswer> update(CategorizedAnswer categorizedAnswer);

}