package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.BingeEatingReasonAnswer;

import java.util.Optional;
import java.util.UUID;

public interface BingeEatingReasonAnswerService {

    Either<Throwable, Optional<BingeEatingReasonAnswer>> find(Long id);

    Either<Throwable, Optional<BingeEatingReasonAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BingeEatingReasonAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BingeEatingReasonAnswer> update(CategorizedAnswer categorizedAnswer);

}