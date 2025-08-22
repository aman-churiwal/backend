package viteezy.service;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

public interface LoginService {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Void> sendAuthenticationMail(String email);

    URI authenticate(String email, String token);
}
