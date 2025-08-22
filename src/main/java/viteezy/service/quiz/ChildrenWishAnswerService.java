package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.ChildrenWishAnswer;

import java.util.Optional;
import java.util.UUID;

public interface ChildrenWishAnswerService {

    Either<Throwable, Optional<ChildrenWishAnswer>> find(Long id);

    Either<Throwable, Optional<ChildrenWishAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ChildrenWishAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ChildrenWishAnswer> update(CategorizedAnswer categorizedAnswer);

}