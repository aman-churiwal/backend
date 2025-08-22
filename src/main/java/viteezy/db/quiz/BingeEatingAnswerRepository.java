package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.BingeEatingAnswer;

import java.util.Optional;
import java.util.UUID;

public interface BingeEatingAnswerRepository {

    Try<Optional<BingeEatingAnswer>> find(Long id);

    Try<Optional<BingeEatingAnswer>> find(UUID quizExternalReference);

    Try<Long> save(BingeEatingAnswer bingeEatingAnswer);

    Try<Long> update(BingeEatingAnswer bingeEatingAnswer);
}