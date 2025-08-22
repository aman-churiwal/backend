package viteezy.db.dashboard;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.db.jdbi.dashboard.AuthenticationRepositoryImpl;
import viteezy.db.jdbi.dashboard.NoteRepositoryImpl;
import viteezy.db.jdbi.dashboard.UserRepositoryImpl;

@Configuration("dbDashboardConfig")
@Import({viteezy.db.jdbi.IoC.class})
public class IoC {

    @Bean("userRepository")
    public UserRepository userRepository(Jdbi jdbi) {
        return new UserRepositoryImpl(jdbi);
    }

    @Bean("authenticationRepository")
    public AuthenticationRepository authenticationRepository(Jdbi jdbi) {
        return new AuthenticationRepositoryImpl(jdbi);
    }

    @Bean("noteRepository")
    public NoteRepository noteRepository(Jdbi jdbi) {
        return new NoteRepositoryImpl(jdbi);
    }
}
