package viteezy.db.jdbi.health;

import com.codahale.metrics.health.HealthCheck;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHealthCheck extends HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHealthCheck.class);

    private final Jdbi dbi;

    public DatabaseHealthCheck(Jdbi dbi) {
        this.dbi = dbi;
    }

    @Override
    protected Result check() throws Exception {
        Handle handle = null;
        Exception throwable = null;
        try {
            handle = dbi.open();
            if (handle.getConnection().isValid(1)) {
                return Result.healthy();
            } else {
                LOGGER.error("Can't ping db");
                return Result.unhealthy("Can't ping db");
            }
        } catch (Exception e) {
            LOGGER.error("Can't connect to db: {}", e.getMessage());
            throwable = e;
            throw throwable;
        } finally {
            try {
                if (handle != null && !handle.isClosed()) {
                    handle.close();
                }
            } catch (Exception throwableClose) {
                if (throwable != null) {
                    throwable.addSuppressed(throwableClose);
                } else {
                    throwable = throwableClose;
                }
                LOGGER.error("Can't close db connection", throwable);
                throw throwable;
            }
        }
    }
}