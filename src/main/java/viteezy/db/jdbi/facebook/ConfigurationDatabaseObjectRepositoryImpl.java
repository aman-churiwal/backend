package viteezy.db.jdbi.facebook;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.configuration.ConfigurationDatabaseObjectRepository;
import viteezy.domain.ConfigurationDatabaseName;
import viteezy.domain.ConfigurationDatabaseObject;

public class ConfigurationDatabaseObjectRepositoryImpl implements ConfigurationDatabaseObjectRepository {

    private static final String SELECT_ALL = "SELECT * FROM configuration ";
    private static final String UPDATE = "UPDATE configuration " +
            "SET type = :type, value = :value, expiration_timestamp = :expirationTimestamp, " +
            "modification_timestamp = :modificationTimestamp ";
    private static final String WHERE_NAME_FACEBOOK_ACCESS_TOKEN = "WHERE name = :name";

    private final Jdbi jdbi;

    public ConfigurationDatabaseObjectRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<ConfigurationDatabaseObject> find(ConfigurationDatabaseName configurationDatabaseName) {
        final HandleCallback<ConfigurationDatabaseObject, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + WHERE_NAME_FACEBOOK_ACCESS_TOKEN)
                .bind("name", configurationDatabaseName)
                .mapTo(ConfigurationDatabaseObject.class)
                .one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<ConfigurationDatabaseObject> update(ConfigurationDatabaseObject configurationDatabaseObject) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE + WHERE_NAME_FACEBOOK_ACCESS_TOKEN)
                .bindBean(configurationDatabaseObject)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(__ -> configurationDatabaseObject);
    }
}
