package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.NameAnswer;

import java.util.Optional;
import java.util.UUID;

public interface NameAnswerService {

    Either<Throwable, Optional<NameAnswer>> find(Long id);

    Either<Throwable, Optional<NameAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, NameAnswer> save(UUID quizExternalReference, String name);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, NameAnswer> update(UUID quizExternalReference, String name);

}