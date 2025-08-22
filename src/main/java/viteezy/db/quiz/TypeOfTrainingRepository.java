package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TypeOfTraining;

import java.util.List;
import java.util.Optional;

public interface TypeOfTrainingRepository {

    Try<Optional<TypeOfTraining>> find(Long id);

    Try<List<TypeOfTraining>> findAll();

    Try<Long> save(TypeOfTraining typeOfTraining);

}