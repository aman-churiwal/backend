package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.quiz.Quiz;
import viteezy.gateways.facebook.FacebookService;
import viteezy.service.CustomerService;
import viteezy.traits.EnforcePresenceTrait;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class QuizServiceImpl implements QuizService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;
    private final CustomerService customerService;
    private final FacebookService facebookService;

    public QuizServiceImpl(QuizRepository quizRepository,
                           CustomerService customerService, FacebookService facebookService) {
        this.quizRepository = quizRepository;
        this.customerService = customerService;
        this.facebookService = facebookService;
    }

    @Override
    public Try<Quiz> find(@NonNull Long id) {
        return quizRepository.find(id);
    }

    @Override
    public Try<Quiz> find(UUID externalReference) {
        return quizRepository.find(externalReference);
    }

    @Override
    public Try<Optional<Quiz>> findByCustomerExternalReference(UUID customerExternalReference) {
        return customerService.find(customerExternalReference)
                .flatMap(customer -> quizRepository.findByCustomerId(customer.getId()))
                .onFailure(peekException());
    }

    @Override
    public Try<Quiz> save(Quiz quiz, String facebookPixel, String facebookClick, String userAgent, String userIpAddress, String referer) {
        return quizRepository.save(quiz)
                .peek(__ -> facebookService.sendStartQuiz(quiz, facebookPixel, facebookClick, userAgent, userIpAddress, referer))
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
