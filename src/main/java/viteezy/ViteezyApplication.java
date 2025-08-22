package viteezy;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import viteezy.auth.CoreAuthenticator;
import viteezy.auth.CoreAuthorizer;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.db.jdbi.health.DatabaseHealthCheck;
import viteezy.domain.dashboard.User;
import viteezy.service.dashboard.AuthenticationService;
import viteezy.service.dashboard.JwtTokenService;
import viteezy.service.dashboard.UserService;
import viteezy.service.tasks.Task;

import java.lang.annotation.Annotation;
import java.util.logging.Level;

import static viteezy.utils.SpringLoader.registerSpringContext;

public class ViteezyApplication extends Application<ViteezyConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ViteezyApplication().run(args);
    }

    
    @Override
    public String getName() {
        return "Viteezy";
    }

    @Override
    public void initialize(final Bootstrap<ViteezyConfiguration> bootstrap) {
        final ConfigurationSourceProvider configurationSourceProvider = bootstrap.getConfigurationSourceProvider();
        final boolean strictMode = false;
        SubstitutingSourceProvider provider = new SubstitutingSourceProvider(
                configurationSourceProvider, new EnvironmentVariableSubstitutor(strictMode)
        );
        bootstrap.setConfigurationSourceProvider(provider);
    }

    @Override
    public void run(final ViteezyConfiguration configuration,
                    final Environment environment) {
        AnnotationConfigApplicationContext annotationContext = registerSpringContext(configuration, environment);

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        registerJerseyComponentForAnnotation(environment, annotationContext, Path.class);
        registerJerseyComponentForAnnotation(environment, annotationContext, Provider.class);
        registerAdminComponentForAnnotation(environment, annotationContext);
        registerHealthChecks(environment, annotationContext, DatabaseHealthCheck.class);
        registerAuth(environment, annotationContext);
        registerHttpJerseyLogging(environment);
    }

    @SafeVarargs
    private void registerHealthChecks(
            Environment environment, AnnotationConfigApplicationContext annotationContext,
            Class<? extends HealthCheck>... healthChecks
    ) {
        for (Class<? extends HealthCheck> healthCheckClazz : healthChecks) {
            HealthCheck healthCheck = annotationContext.getBean(healthCheckClazz);
            environment.healthChecks().register(healthCheckClazz.getSimpleName(), healthCheck);
        }
    }

    private void registerAuth(Environment environment, AnnotationConfigApplicationContext annotationContext) {
        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new CoreAuthenticator(
                                annotationContext.getBean(AuthenticationService.class),
                                annotationContext.getBean(UserService.class),
                                annotationContext.getBean(JwtTokenService.class)))
                        .setAuthorizer(new CoreAuthorizer())
                        .setPrefix("Bearer")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

    private void registerHttpJerseyLogging(Environment environment) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME);
        LoggingFeature component = new LoggingFeature(logger, Level.FINE, LoggingFeature.Verbosity.PAYLOAD_TEXT, null);
        environment.jersey().register(component);
        environment.jersey().enable(LoggingFeature.DEFAULT_LOGGER_NAME);
    }

    private void registerJerseyComponentForAnnotation(
            Environment environment, AnnotationConfigApplicationContext annotationContext, Class<? extends Annotation> annotationType
    ) {
        annotationContext.getBeansWithAnnotation(annotationType)
                .forEach((key, value) -> environment.jersey().register(value));
    }

    private void registerAdminComponentForAnnotation(
            Environment environment, AnnotationConfigApplicationContext annotationContext
    ) {
        annotationContext.getBeansWithAnnotation(Task.class)
                .forEach((key, value) -> environment.admin().addTask((io.dropwizard.servlets.tasks.Task) value));
    }
}
