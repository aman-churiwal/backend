package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.BingeEatingReasonAnswer;

import java.util.Optional;
import java.util.UUID;

public interface BingeEatingReasonAnswerRepository {

    Try<Optional<BingeEatingReasonAnswer>> find(Long id);

    Try<Optional<BingeEatingReasonAnswer>> find(UUID quizExternalReference);

    Try<Long> save(BingeEatingReasonAnswer bingeEatingReasonAnswer);

    Try<Long> update(BingeEatingReasonAnswer bingeEatingReasonAnswer);
}