package viteezy.service.quiz;

import com.google.common.base.Throwables;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.quiz.LoseWeightChallengeRepository;
import viteezy.domain.quiz.LoseWeightChallenge;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LoseWeightChallengeServiceImpl implements LoseWeightChallengeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoseWeightChallengeService.class);
    private final LoseWeightChallengeRepository loseWeightChallengeRepository;

    public LoseWeightChallengeServiceImpl(LoseWeightChallengeRepository loseWeightChallengeRepository) {
        this.loseWeightChallengeRepository = loseWeightChallengeRepository;
    }

    @Override
    public Either<Throwable, Optional<LoseWeightChallenge>> find(Long id) {
        return loseWeightChallengeRepository.find(id)
                .toEither();
    }

    @Override
    public Either<Throwable, List<LoseWeightChallenge>> findAll() {
        return loseWeightChallengeRepository.findAll()
                .toEither();
    }

    @Override
    public Either<Throwable, List<LoseWeightChallenge>> findActive() {
        return loseWeightChallengeRepository.findAll()
                .map(loseWeightChallenges -> loseWeightChallenges.stream()
                        .filter(LoseWeightChallenge::isActive)
                        .collect(Collectors.toList())
                ).toEither();
    }

    @Override
    public Either<Throwable, LoseWeightChallenge> save(LoseWeightChallenge loseWeightChallenge) {
        return loseWeightChallengeRepository
                .save(loseWeightChallenge)
                .toEither()
                .flatMap(retrieveById())
                .peekLeft(peekException())
                .peekLeft(rollbackTransaction());
    }

    private Function<Long, Either<Throwable, LoseWeightChallenge>> retrieveById() {
        return id -> {
            Try<Optional<LoseWeightChallenge>> optionalTry = loseWeightChallengeRepository.find(id);
            if (optionalTry.isSuccess() && optionalTry.get().isPresent()) {
                return Either.right(optionalTry.get().get());
            } else if (optionalTry.isSuccess() && optionalTry.get().isEmpty()) {
                final String message = "LoseWeightChallenge entity was saved but could not be retrieved from db";
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