package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.VitaminIntakeRepository;
import viteezy.domain.quiz.VitaminIntake;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class VitaminIntakeServiceImpl implements VitaminIntakeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitaminIntakeService.class);
    private final VitaminIntakeRepository vitaminIntakeRepository;

    public VitaminIntakeServiceImpl(VitaminIntakeRepository vitaminIntakeRepository) {
        this.vitaminIntakeRepository = vitaminIntakeRepository;
    }

    @Override
    public Either<Throwable, Optional<VitaminIntake>> find(Long id) {
        return vitaminIntakeRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<VitaminIntake>> findAll() {
        return vitaminIntakeRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, VitaminIntake> save(VitaminIntake vitaminIntake) {
        return vitaminIntakeRepository
                .save(vitaminIntake)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, VitaminIntake>> retrieveById() {
        return id -> {
            Try<Optional<VitaminIntake>> optionalTry = vitaminIntakeRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "VitaminIntake entity was saved but could not be retrieved from db";
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