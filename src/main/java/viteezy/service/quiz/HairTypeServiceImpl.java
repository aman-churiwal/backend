package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.HairTypeRepository;
import viteezy.domain.quiz.HairType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class HairTypeServiceImpl implements HairTypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HairTypeService.class);
    private final HairTypeRepository hairTypeRepository;

    public HairTypeServiceImpl(HairTypeRepository hairTypeRepository) {
        this.hairTypeRepository = hairTypeRepository;
    }

    @Override
    public Either<Throwable, Optional<HairType>> find(Long id) {
        return hairTypeRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<HairType>> findAll() {
        return hairTypeRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, HairType> save(HairType hairType) {
        return hairTypeRepository
                .save(hairType)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, HairType>> retrieveById() {
        return id -> {
            Try<Optional<HairType>> optionalTry = hairTypeRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "HairType entity was saved but could not be retrieved from db";
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