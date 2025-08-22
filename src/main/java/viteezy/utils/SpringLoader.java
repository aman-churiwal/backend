package viteezy.utils;

import io.dropwizard.core.setup.Environment;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import viteezy.configuration.ViteezyConfiguration;

public class SpringLoader {

    public static AnnotationConfigApplicationContext registerSpringContext(
            ViteezyConfiguration configuration, Environment environment
    ) {
        AnnotationConfigApplicationContext parent = registerParentSpringContext(configuration, environment);
        return registerMainApplicationContext(parent);
    }

    private static AnnotationConfigApplicationContext registerMainApplicationContext(
            AnnotationConfigApplicationContext parent
    ) {
        AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext();
        annotationContext.setParent(parent);
        annotationContext.scan("viteezy");
        annotationContext.refresh();
        annotationContext.registerShutdownHook();
        annotationContext.start();
        return annotationContext;
    }

    private static AnnotationConfigApplicationContext registerParentSpringContext(
            ViteezyConfiguration configuration, Environment environment
    ) {
        AnnotationConfigApplicationContext parent = new AnnotationConfigApplicationContext();
        parent.refresh();

        ConfigurableListableBeanFactory beanFactory = parent.getBeanFactory();
        beanFactory.registerSingleton("configuration", configuration);
        beanFactory.registerSingleton("dropwizardEnvironment", environment);
        beanFactory.registerSingleton("applicationName", environment.getName());
        beanFactory.registerSingleton("metricRegistry", environment.metrics());
        beanFactory.registerSingleton("metricContext", environment.getAdminContext());
        parent.registerShutdownHook();
        parent.start();
        return parent;
    }
}
