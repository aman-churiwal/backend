package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.VitaminIntake;

import java.util.List;
import java.util.Optional;

public interface VitaminIntakeRepository {

    Try<Optional<VitaminIntake>> find(Long id);

    Try<List<VitaminIntake>> findAll();

    Try<Long> save(VitaminIntake vitaminIntake);

}