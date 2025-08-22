package viteezy.traits;

import io.vavr.control.Either;
import io.vavr.control.Try;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public interface EnforcePresenceTrait {

    default <T> Function<Optional<T>, Either<Throwable, T>> optionalToNarrowedEither() {
        return optional -> optional
                .<Either<Throwable, T>>map(Either::right)
                .orElseGet(() -> Either.left(new NoSuchElementException()));
    }

    default <T> Function<Optional<T>, Try<T>> optionalToNarrowedTry() {
        return optional -> optional
                .map(Try::success)
                .orElseGet(() -> Try.failure(new NoSuchElementException()));
    }
}
