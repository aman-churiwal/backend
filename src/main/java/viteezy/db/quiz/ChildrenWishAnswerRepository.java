package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.ChildrenWishAnswer;

import java.util.Optional;
import java.util.UUID;

public interface ChildrenWishAnswerRepository {

    Try<Optional<ChildrenWishAnswer>> find(Long id);

    Try<Optional<ChildrenWishAnswer>> find(UUID quizExternalReference);

    Try<Long> save(ChildrenWishAnswer childrenWishAnswer);

    Try<Long> update(ChildrenWishAnswer childrenWishAnswer);
}