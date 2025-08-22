package viteezy.db.dashboard;

import io.vavr.control.Try;
import viteezy.domain.dashboard.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Try<List<User>> findAll();

    Try<Optional<User>> findById(Long id);

    Try<Optional<User>> findByEmail(String email);

    Try<Long> upsert(User user);
}
