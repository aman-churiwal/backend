package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.VitaminIntakeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface VitaminIntakeAnswerService {

    Either<Throwable, Optional<VitaminIntakeAnswer>> find(Long id);

    Either<Throwable, Optional<VitaminIntakeAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, VitaminIntakeAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, VitaminIntakeAnswer> update(CategorizedAnswer categorizedAnswer);

}