package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.TypeOfTrainingRepository;
import viteezy.domain.quiz.TypeOfTraining;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class TypeOfTrainingServiceImpl implements TypeOfTrainingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TypeOfTrainingService.class);
    private final TypeOfTrainingRepository typeOfTrainingRepository;

    public TypeOfTrainingServiceImpl(TypeOfTrainingRepository typeOfTrainingRepository) {
        this.typeOfTrainingRepository = typeOfTrainingRepository;
    }

    @Override
    public Either<Throwable, Optional<TypeOfTraining>> find(Long id) {
        return typeOfTrainingRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<TypeOfTraining>> findAll() {
        return typeOfTrainingRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, TypeOfTraining> save(TypeOfTraining typeOfTraining) {
        return typeOfTrainingRepository
                .save(typeOfTraining)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, TypeOfTraining>> retrieveById() {
        return id -> {
            Try<Optional<TypeOfTraining>> optionalTry = typeOfTrainingRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "TypeOfTraining entity was saved but could not be retrieved from db";
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