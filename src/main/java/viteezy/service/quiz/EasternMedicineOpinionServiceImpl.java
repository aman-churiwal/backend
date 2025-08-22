package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.EasternMedicineOpinionRepository;
import viteezy.domain.quiz.EasternMedicineOpinion;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class EasternMedicineOpinionServiceImpl implements EasternMedicineOpinionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasternMedicineOpinionService.class);
    private final EasternMedicineOpinionRepository easternMedicineOpinionRepository;

    public EasternMedicineOpinionServiceImpl(EasternMedicineOpinionRepository easternMedicineOpinionRepository) {
        this.easternMedicineOpinionRepository = easternMedicineOpinionRepository;
    }

    @Override
    public Either<Throwable, Optional<EasternMedicineOpinion>> find(Long id) {
        return easternMedicineOpinionRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<EasternMedicineOpinion>> findAll() {
        return easternMedicineOpinionRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, EasternMedicineOpinion> save(EasternMedicineOpinion easternMedicineOpinion) {
        return easternMedicineOpinionRepository
                .save(easternMedicineOpinion)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, EasternMedicineOpinion>> retrieveById() {
        return id -> {
            Try<Optional<EasternMedicineOpinion>> optionalTry = easternMedicineOpinionRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "EasternMedicineOpinion entity was saved but could not be retrieved from db";
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