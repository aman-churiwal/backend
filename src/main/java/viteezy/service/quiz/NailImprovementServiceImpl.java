package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.NailImprovementRepository;
import viteezy.domain.quiz.NailImprovement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class NailImprovementServiceImpl implements NailImprovementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NailImprovementService.class);
    private final NailImprovementRepository nailImprovementRepository;

    public NailImprovementServiceImpl(NailImprovementRepository nailImprovementRepository) {
        this.nailImprovementRepository = nailImprovementRepository;
    }

    @Override
    public Either<Throwable, Optional<NailImprovement>> find(Long id) {
        return nailImprovementRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<NailImprovement>> findAll() {
        return nailImprovementRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, NailImprovement> save(NailImprovement nailImprovement) {
        return nailImprovementRepository
                .save(nailImprovement)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, NailImprovement>> retrieveById() {
        return id -> {
            Try<Optional<NailImprovement>> optionalTry = nailImprovementRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "NailImprovement entity was saved but could not be retrieved from db";
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