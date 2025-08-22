package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.LackOfConcentrationRepository;
import viteezy.domain.quiz.LackOfConcentration;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class LackOfConcentrationServiceImpl implements LackOfConcentrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LackOfConcentrationService.class);
    private final LackOfConcentrationRepository lackOfConcentrationRepository;

    public LackOfConcentrationServiceImpl(LackOfConcentrationRepository lackOfConcentrationRepository) {
        this.lackOfConcentrationRepository = lackOfConcentrationRepository;
    }

    @Override
    public Either<Throwable, Optional<LackOfConcentration>> find(Long id) {
        return lackOfConcentrationRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<LackOfConcentration>> findAll() {
        return lackOfConcentrationRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, LackOfConcentration> save(LackOfConcentration lackOfConcentration) {
        return lackOfConcentrationRepository
                .save(lackOfConcentration)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, LackOfConcentration>> retrieveById() {
        return id -> {
            Try<Optional<LackOfConcentration>> optionalTry = lackOfConcentrationRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "LackOfConcentration entity was saved but could not be retrieved from db";
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