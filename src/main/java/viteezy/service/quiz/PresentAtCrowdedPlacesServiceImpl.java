package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.PresentAtCrowdedPlacesRepository;
import viteezy.domain.quiz.PresentAtCrowdedPlaces;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class PresentAtCrowdedPlacesServiceImpl implements PresentAtCrowdedPlacesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PresentAtCrowdedPlacesService.class);
    private final PresentAtCrowdedPlacesRepository presentAtCrowdedPlacesRepository;

    public PresentAtCrowdedPlacesServiceImpl(PresentAtCrowdedPlacesRepository presentAtCrowdedPlacesRepository) {
        this.presentAtCrowdedPlacesRepository = presentAtCrowdedPlacesRepository;
    }

    @Override
    public Either<Throwable, Optional<PresentAtCrowdedPlaces>> find(Long id) {
        return presentAtCrowdedPlacesRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<PresentAtCrowdedPlaces>> findAll() {
        return presentAtCrowdedPlacesRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, PresentAtCrowdedPlaces> save(PresentAtCrowdedPlaces presentAtCrowdedPlaces) {
        return presentAtCrowdedPlacesRepository
                .save(presentAtCrowdedPlaces)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, PresentAtCrowdedPlaces>> retrieveById() {
        return id -> {
            Try<Optional<PresentAtCrowdedPlaces>> optionalTry = presentAtCrowdedPlacesRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "PresentAtCrowdedPlaces entity was saved but could not be retrieved from db";
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