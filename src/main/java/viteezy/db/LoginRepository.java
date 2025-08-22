package viteezy.db;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.Login;

public interface LoginRepository {

    Try<Login> find(Long id);

    Try<Login> find(String email);

    @Transactional(transactionManager = "transactionManager")
    Try<Login> saveOrUpdate(String email, String token);
}
