package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.LoseWeightChallengeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface LoseWeightChallengeAnswerService {

    Either<Throwable, Optional<LoseWeightChallengeAnswer>> find(Long id);

    Either<Throwable, Optional<LoseWeightChallengeAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LoseWeightChallengeAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LoseWeightChallengeAnswer> update(CategorizedAnswer categorizedAnswer);

}