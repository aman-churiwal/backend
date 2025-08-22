package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.HealthComplaints;

import java.util.List;
import java.util.Optional;

public interface HealthComplaintsRepository {

    Try<Optional<HealthComplaints>> find(Long id);

    Try<List<HealthComplaints>> findAll();

    Try<Long> save(HealthComplaints healthComplaints);

}