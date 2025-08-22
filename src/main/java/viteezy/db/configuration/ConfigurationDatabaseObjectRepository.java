package viteezy.db.configuration;

import io.vavr.control.Try;
import viteezy.domain.ConfigurationDatabaseName;
import viteezy.domain.ConfigurationDatabaseObject;

public interface ConfigurationDatabaseObjectRepository {

    Try<ConfigurationDatabaseObject> find(ConfigurationDatabaseName configurationDatabaseName);

    Try<ConfigurationDatabaseObject> update(ConfigurationDatabaseObject configurationDatabaseObject);
}
