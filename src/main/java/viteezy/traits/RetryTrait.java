package viteezy.traits;

import io.vavr.control.Try;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.function.CheckedSupplier;

import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

public interface RetryTrait {

    default <T> Try<T> triggerCallWithRetry(
            CheckedSupplier<Try<T>> getSupplier, Consumer<T> onSuccessConsumer,
            Consumer<Throwable> onFailureConsumer, RetryPolicy<Try<T>> retryPolicy
    ) {
        return Failsafe
                .with(retryPolicy)
                .get(getSupplier)
                .onFailure(onFailureConsumer)
                .onSuccess(onSuccessConsumer);
    }

    default <T> Try<T> triggerCallWithRetry(
            CheckedSupplier<Try<T>> getSupplier,
            Consumer<Throwable> onFailureConsumer,
            RetryPolicy<Try<T>> retryPolicy
    ) {
        return Failsafe
                .with(retryPolicy)
                .get(getSupplier)
                .onFailure(onFailureConsumer);
    }

    default <T> RetryPolicy<Try<T>> retryPolicy() {
        return new RetryPolicy<Try<T>>()
                .handleIf((tryEntity, throwable) -> tryEntity.isFailure())
                .withBackoff(RetryDefaults.MIN_DELAY_SECS, RetryDefaults.MAX_DELAY_SECS, ChronoUnit.SECONDS)
                .withMaxRetries(RetryDefaults.MAX_ATTEMPTS);
    }

    class RetryDefaults {
        public static final int MIN_DELAY_SECS = 1;
        public static final int MAX_DELAY_SECS = 120;
        public static final int MAX_ATTEMPTS = 5;
    }
}
