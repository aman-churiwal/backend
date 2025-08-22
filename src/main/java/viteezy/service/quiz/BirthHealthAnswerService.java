package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.BirthHealthAnswer;

import java.util.Optional;
import java.util.UUID;

public interface BirthHealthAnswerService {

    Either<Throwable, Optional<BirthHealthAnswer>> find(Long id);

    Either<Throwable, Optional<BirthHealthAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BirthHealthAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BirthHealthAnswer> update(CategorizedAnswer categorizedAnswer);

}