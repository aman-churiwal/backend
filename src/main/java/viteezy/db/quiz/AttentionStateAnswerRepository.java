package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AttentionStateAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AttentionStateAnswerRepository {

    Try<Optional<AttentionStateAnswer>> find(Long id);

    Try<Optional<AttentionStateAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AttentionStateAnswer attentionStateAnswer);

    Try<Long> update(AttentionStateAnswer attentionStateAnswer);
}