package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.VitaminOpinionRepository;
import viteezy.domain.quiz.VitaminOpinion;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class VitaminOpinionServiceImpl implements VitaminOpinionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitaminOpinionService.class);
    private final VitaminOpinionRepository vitaminOpinionRepository;

    public VitaminOpinionServiceImpl(VitaminOpinionRepository vitaminOpinionRepository) {
        this.vitaminOpinionRepository = vitaminOpinionRepository;
    }

    @Override
    public Either<Throwable, Optional<VitaminOpinion>> find(Long id) {
        return vitaminOpinionRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<VitaminOpinion>> findAll() {
        return vitaminOpinionRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, VitaminOpinion> save(VitaminOpinion vitaminOpinion) {
        return vitaminOpinionRepository
                .save(vitaminOpinion)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, VitaminOpinion>> retrieveById() {
        return id -> {
            Try<Optional<VitaminOpinion>> optionalTry = vitaminOpinionRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "VitaminOpinion entity was saved but could not be retrieved from db";
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