package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.TiredWhenWakeUpAnswer;

import java.util.Optional;
import java.util.UUID;

public interface TiredWhenWakeUpAnswerService {

    Either<Throwable, Optional<TiredWhenWakeUpAnswer>> find(Long id);

    Either<Throwable, Optional<TiredWhenWakeUpAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TiredWhenWakeUpAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TiredWhenWakeUpAnswer> update(CategorizedAnswer categorizedAnswer);

}