package viteezy.service.dashboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.configuration.dashboard.JwtConfiguration;
import viteezy.db.dashboard.AuthenticationRepository;
import viteezy.db.dashboard.NoteRepository;
import viteezy.db.dashboard.UserRepository;
import viteezy.service.CustomerService;

@Configuration("servicesDashboardConfig")
public class IoC {

    private final ViteezyConfiguration viteezyConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.viteezyConfiguration = viteezyConfiguration;
    }

    @Bean("authenticationService")
    public AuthenticationService authenticationService(AuthenticationRepository authenticationRepository) {
        return new AuthenticationServiceImpl(authenticationRepository);
    }

    @Bean("jwtTokenService")
    public JwtTokenService jwtTokenService() {
        final JwtConfiguration jwtConfiguration = viteezyConfiguration.getJwtConfiguration();
        return new JwtTokenServiceImpl(jwtConfiguration);
    }

    @Bean("noteService")
    public NoteService noteService(NoteRepository noteRepository, CustomerService customerService) {
        return new NoteServiceImpl(noteRepository, customerService);
    }

    @Bean("userService")
    public UserService userService(UserRepository userRepository, AuthenticationService authenticationService,
                                   JwtTokenService jwtTokenService) {
        return new UserServiceImpl(userRepository, authenticationService, jwtTokenService);
    }
}
