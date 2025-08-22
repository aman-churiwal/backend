package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.CurrentLibidoAnswer;

import java.util.Optional;
import java.util.UUID;

public interface CurrentLibidoAnswerService {

    Either<Throwable, Optional<CurrentLibidoAnswer>> find(Long id);

    Either<Throwable, Optional<CurrentLibidoAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, CurrentLibidoAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, CurrentLibidoAnswer> update(CategorizedAnswer categorizedAnswer);

}