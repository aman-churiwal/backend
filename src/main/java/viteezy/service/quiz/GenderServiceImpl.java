package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.GenderRepository;
import viteezy.domain.quiz.Gender;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenderServiceImpl implements GenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenderService.class);
    private final GenderRepository genderRepository;

    public GenderServiceImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    @Override
    public Either<Throwable, Optional<Gender>> find(Long id) {
        return genderRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<Gender>> findAll() {
        return genderRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, Gender> save(Gender gender) {
        return genderRepository
                .save(gender)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, Gender>> retrieveById() {
        return id -> {
            Try<Optional<Gender>> optionalTry = genderRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "Gender entity was saved but could not be retrieved from db";
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