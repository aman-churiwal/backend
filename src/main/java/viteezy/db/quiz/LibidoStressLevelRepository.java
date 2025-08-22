package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.LibidoStressLevel;

import java.util.List;
import java.util.Optional;

public interface LibidoStressLevelRepository {

    Try<Optional<LibidoStressLevel>> find(Long id);

    Try<List<LibidoStressLevel>> findAll();

    Try<Long> save(LibidoStressLevel libidoStressLevel);

}