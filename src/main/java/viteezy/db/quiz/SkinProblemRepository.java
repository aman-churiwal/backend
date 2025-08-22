package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SkinProblem;

import java.util.List;
import java.util.Optional;

public interface SkinProblemRepository {

    Try<Optional<SkinProblem>> find(Long id);

    Try<List<SkinProblem>> findAll();

    Try<Long> save(SkinProblem skinProblem);

}