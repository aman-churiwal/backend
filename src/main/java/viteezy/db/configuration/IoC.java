package viteezy.db.configuration;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.db.jdbi.facebook.ConfigurationDatabaseObjectRepositoryImpl;


@Configuration("configurationDbConfig")
@Import({viteezy.db.jdbi.IoC.class})
public class IoC {

    @Bean("configurationDatabaseObjectRepository")
    public ConfigurationDatabaseObjectRepository configurationDatabaseObjectRepository(Jdbi jdbi) {
        return new ConfigurationDatabaseObjectRepositoryImpl(jdbi);
    }
}