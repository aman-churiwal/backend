package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.AllergyTypeRepository;
import viteezy.domain.quiz.AllergyType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AllergyTypeServiceImpl implements AllergyTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllergyTypeService.class);
    private final AllergyTypeRepository allergyTypeRepository;

    public AllergyTypeServiceImpl(AllergyTypeRepository allergyTypeRepository) {
        this.allergyTypeRepository = allergyTypeRepository;
    }

    @Override
    public Either<Throwable, Optional<AllergyType>> find(Long id) {
        return allergyTypeRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<AllergyType>> findAll() {
        return allergyTypeRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, AllergyType> save(AllergyType allergyType) {
        return allergyTypeRepository
                .save(allergyType)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, AllergyType>> retrieveById() {
        return id -> {
            Try<Optional<AllergyType>> optionalTry = allergyTypeRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "AllergyType entity was saved but could not be retrieved from db";
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