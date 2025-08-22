package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TransitionPeriodComplaints;

import java.util.List;
import java.util.Optional;

public interface TransitionPeriodComplaintsRepository {

    Try<Optional<TransitionPeriodComplaints>> find(Long id);

    Try<List<TransitionPeriodComplaints>> findAll();

    Try<Long> save(TransitionPeriodComplaints transitionPeriodComplaints);

}