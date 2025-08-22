package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.HelpGoal;

import java.util.List;
import java.util.Optional;

public interface HelpGoalRepository {

    Try<Optional<HelpGoal>> find(Long id);

    Try<List<HelpGoal>> findAll();

    Try<Long> save(HelpGoal helpGoal);

}