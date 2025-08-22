package viteezy.service.dashboard;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.controller.dto.dashboard.JwtResponse;
import viteezy.controller.dto.dashboard.SignUpRequest;
import viteezy.domain.dashboard.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Either<Throwable, Optional<User>> find(Long id);

    Either<Throwable, List<User>> findAll();

    Either<Throwable, Optional<User>> findByCredentials(String email, String password);

    @Transactional(transactionManager = "transactionManager", isolation = Isolation.SERIALIZABLE)
    Either<Throwable, Optional<User>> upsert(SignUpRequest signUpRequest);


    Either<Throwable, JwtResponse> login(String email, String password);
}
