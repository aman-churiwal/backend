package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.SportReasonAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SportReasonAnswerService {

    Either<Throwable, Optional<SportReasonAnswer>> find(Long id);

    Either<Throwable, Optional<SportReasonAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SportReasonAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SportReasonAnswer> update(CategorizedAnswer categorizedAnswer);

}