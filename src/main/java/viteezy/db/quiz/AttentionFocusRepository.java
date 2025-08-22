package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AttentionFocus;

import java.util.List;
import java.util.Optional;

public interface AttentionFocusRepository {

    Try<Optional<AttentionFocus>> find(Long id);

    Try<List<AttentionFocus>> findAll();

    Try<Long> save(AttentionFocus attentionFocus);

}