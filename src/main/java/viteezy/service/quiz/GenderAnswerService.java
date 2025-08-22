package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.GenderAnswer;

import java.util.Optional;
import java.util.UUID;

public interface GenderAnswerService {

    Either<Throwable, Optional<GenderAnswer>> find(Long id);

    Either<Throwable, Optional<GenderAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, GenderAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, GenderAnswer> update(CategorizedAnswer categorizedAnswer);

}