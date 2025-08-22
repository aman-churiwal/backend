package viteezy.traits;

import com.google.common.base.Throwables;
import org.slf4j.Logger;

import java.util.function.Consumer;

public interface ExceptionLoggerTrait {

    default Consumer<Throwable> logException(Logger logger) {
        return throwable -> logger.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
