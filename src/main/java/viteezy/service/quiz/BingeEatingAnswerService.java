package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.BingeEatingAnswer;

import java.util.Optional;
import java.util.UUID;

public interface BingeEatingAnswerService {

    Either<Throwable, Optional<BingeEatingAnswer>> find(Long id);

    Either<Throwable, Optional<BingeEatingAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BingeEatingAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BingeEatingAnswer> update(CategorizedAnswer categorizedAnswer);

}