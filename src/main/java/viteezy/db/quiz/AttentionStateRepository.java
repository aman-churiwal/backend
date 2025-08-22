package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AttentionState;

import java.util.List;
import java.util.Optional;

public interface AttentionStateRepository {

    Try<Optional<AttentionState>> find(Long id);

    Try<List<AttentionState>> findAll();

    Try<Long> save(AttentionState attentionState);

}