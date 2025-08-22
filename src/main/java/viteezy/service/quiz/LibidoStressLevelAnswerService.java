package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.LibidoStressLevelAnswer;

import java.util.Optional;
import java.util.UUID;

public interface LibidoStressLevelAnswerService {

    Either<Throwable, Optional<LibidoStressLevelAnswer>> find(Long id);

    Either<Throwable, Optional<LibidoStressLevelAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LibidoStressLevelAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LibidoStressLevelAnswer> update(CategorizedAnswer categorizedAnswer);

}