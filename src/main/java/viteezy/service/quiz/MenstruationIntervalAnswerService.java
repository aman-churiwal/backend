package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.MenstruationIntervalAnswer;

import java.util.Optional;
import java.util.UUID;

public interface MenstruationIntervalAnswerService {

    Either<Throwable, Optional<MenstruationIntervalAnswer>> find(Long id);

    Either<Throwable, Optional<MenstruationIntervalAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationIntervalAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationIntervalAnswer> update(CategorizedAnswer categorizedAnswer);

}