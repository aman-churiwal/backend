package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.IronPrescribed;

import java.util.List;
import java.util.Optional;

public interface IronPrescribedRepository {

    Try<Optional<IronPrescribed>> find(Long id);

    Try<List<IronPrescribed>> findAll();

    Try<Long> save(IronPrescribed ironPrescribed);

}