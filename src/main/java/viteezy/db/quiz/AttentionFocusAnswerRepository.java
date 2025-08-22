package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AttentionFocusAnswer;

import java.util.Optional;
import java.util.UUID;

public interface AttentionFocusAnswerRepository {

    Try<Optional<AttentionFocusAnswer>> find(Long id);

    Try<Optional<AttentionFocusAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AttentionFocusAnswer attentionFocusAnswer);

    Try<Long> update(AttentionFocusAnswer attentionFocusAnswer);
}