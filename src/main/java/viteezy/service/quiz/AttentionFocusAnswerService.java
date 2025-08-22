package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AttentionFocusAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AttentionFocusAnswerService {

    Either<Throwable, Optional<AttentionFocusAnswer>> find(Long id);

    Either<Throwable, Optional<AttentionFocusAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AttentionFocusAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AttentionFocusAnswer> update(CategorizedAnswer categorizedAnswer);

}