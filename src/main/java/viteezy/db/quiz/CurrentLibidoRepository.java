package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.CurrentLibido;

import java.util.List;
import java.util.Optional;

public interface CurrentLibidoRepository {

    Try<Optional<CurrentLibido>> find(Long id);

    Try<List<CurrentLibido>> findAll();

    Try<Long> save(CurrentLibido currentLibido);

}