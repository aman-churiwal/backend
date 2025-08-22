package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AttentionStateAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AttentionStateAnswerService {

    Either<Throwable, Optional<AttentionStateAnswer>> find(Long id);

    Either<Throwable, Optional<AttentionStateAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AttentionStateAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AttentionStateAnswer> update(CategorizedAnswer categorizedAnswer);

}