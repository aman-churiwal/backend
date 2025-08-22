package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.UsageGoal;

import java.util.List;
import java.util.Optional;

public interface UsageGoalRepository {

    Try<Optional<UsageGoal>> find(Long id);

    Try<List<UsageGoal>> findAll();

    Try<Long> save(UsageGoal usageGoal);

}