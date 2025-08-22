package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AverageSleepingHours;

import java.util.List;
import java.util.Optional;

public interface AverageSleepingHoursRepository {

    Try<Optional<AverageSleepingHours>> find(Long id);

    Try<List<AverageSleepingHours>> findAll();

    Try<Long> save(AverageSleepingHours averageSleepingHours);

}