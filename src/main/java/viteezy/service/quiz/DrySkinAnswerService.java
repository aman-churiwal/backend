package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DrySkinAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DrySkinAnswerService {

    Either<Throwable, Optional<DrySkinAnswer>> find(Long id);

    Either<Throwable, Optional<DrySkinAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DrySkinAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DrySkinAnswer> update(CategorizedAnswer categorizedAnswer);

}