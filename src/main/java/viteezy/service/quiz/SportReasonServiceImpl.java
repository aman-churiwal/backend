package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.SportReasonRepository;
import viteezy.domain.quiz.SportReason;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SportReasonServiceImpl implements SportReasonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportReasonService.class);
    private final SportReasonRepository sportReasonRepository;

    public SportReasonServiceImpl(SportReasonRepository sportReasonRepository) {
        this.sportReasonRepository = sportReasonRepository;
    }

    @Override
    public Either<Throwable, Optional<SportReason>> find(Long id) {
        return sportReasonRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<SportReason>> findAll() {
        return sportReasonRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, SportReason> save(SportReason sportReason) {
        return sportReasonRepository
                .save(sportReason)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, SportReason>> retrieveById() {
        return id -> {
            Try<Optional<SportReason>> optionalTry = sportReasonRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "SportReason entity was saved but could not be retrieved from db";
                LOGGER.error("{}", message);
                return Either.left(new NoSuchElementException(message));
            } else {
                LOGGER.error(optionalTry.getCause().toString());
                return Either.left(optionalTry.getCause());
            }
        };
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}