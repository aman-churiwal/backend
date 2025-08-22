package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.MenstruationSideIssue;

import java.util.List;
import java.util.Optional;

public interface MenstruationSideIssueRepository {

    Try<Optional<MenstruationSideIssue>> find(Long id);

    Try<List<MenstruationSideIssue>> findAll();

    Try<Long> save(MenstruationSideIssue menstruationSideIssue);

}