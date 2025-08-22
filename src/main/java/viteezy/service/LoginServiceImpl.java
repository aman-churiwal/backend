package viteezy.service;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.LoginRepository;
import viteezy.domain.Customer;
import viteezy.domain.LoggingEvent;
import viteezy.domain.Login;
import viteezy.service.mail.EmailService;
import viteezy.traits.EnforcePresenceTrait;

import java.net.URI;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoginServiceImpl implements LoginService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    private static final int TOKEN_BYTE_SIZE = 16;
    private static final int TOKEN_EXPIRE_MINUTES = 10;

    private static final String AUTHENTICATED_REDIRECT_URL = "%s/domain?reference=%s";
    private static final String LOGIN_REDIRECT_URL = "%s/login?%s";
    private static final String AUTHENTICATE_FAILED = "authenticate-failed";

    private final LoginRepository loginRepository;
    private final EmailService emailService;
    private final CustomerService customerService;
    private final LoggingService loggingService;
    private final String frontendBaseUrl;

    private final SecureRandom random = new SecureRandom();

    protected LoginServiceImpl(LoginRepository loginRepository, EmailService emailService,
                               CustomerService customerService, LoggingService loggingService, String frontendBaseUrl) {
        this.loginRepository = loginRepository;
        this.emailService = emailService;
        this.customerService = customerService;
        this.loggingService = loggingService;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @Override
    public Try<Void> sendAuthenticationMail(String email) {
        return customerService.find(email)
                .flatMap(customer -> createAndSendToken(customer.getEmail()));
    }

    private Try<Void> createAndSendToken(String email) {
        String token = createToken();
        return loginRepository.saveOrUpdate(email, token)
                .flatMap(__ -> sendMagicLink(email, token))
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public URI authenticate(String email, String token) {
        return authenticateToken(email, token)
                .flatMap(login -> findCustomerByEmail(login.getEmail()))
                .peek(customer -> loggingService.create(customer.getId(), LoggingEvent.CUSTOMER_LOGGED_IN, "Klant ingelogd"))
                .map(customer -> createAuthenticatedRedirect(customer.getExternalReference()))
                .onFailure(peekException())
                .getOrElse(createLoginRedirect());
    }
    
    private Try<Void> sendMagicLink(String email, String token) {
        return emailService.sendMagicLink(email, token);
    }

    private Try<Login> authenticateToken (String email, String token) {
        final Supplier<Throwable> invalidTokenExceptionSupplier = () -> new BadCredentialsException("Invalid auth token for email: " + email);
        final Supplier<Throwable> expiredTokenExceptionSupplier = () -> new BadCredentialsException("Expired token");
        return loginRepository.find(email)
                .filter(login -> login.getToken().equals(token), invalidTokenExceptionSupplier)
                .filter(login -> LocalDateTime.now().isBefore(login.getLastUpdated().plusMinutes(TOKEN_EXPIRE_MINUTES)), expiredTokenExceptionSupplier);
    }

    private Try<Customer> findCustomerByEmail(String email) {
            return customerService.find(email);
    }

    private URI createAuthenticatedRedirect(UUID externalReference) {
        return URI.create(String.format(AUTHENTICATED_REDIRECT_URL, frontendBaseUrl, externalReference));
    }

    private URI createLoginRedirect() {
        return URI.create(String.format(LOGIN_REDIRECT_URL, frontendBaseUrl, AUTHENTICATE_FAILED));
    }

    private String createToken() {
        byte[] bytes = new byte[TOKEN_BYTE_SIZE];
        random.nextBytes(bytes);
        return String.valueOf(Hex.encode(bytes));
    }

    private Consumer<Throwable> peekException() {
        return throwable -> {
            if (throwable instanceof BadCredentialsException)
                LOGGER.warn(throwable.getMessage());
            else
                LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
        };
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
