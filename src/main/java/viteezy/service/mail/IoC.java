package viteezy.service.mail;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.configuration.mail.MailConfiguration;
import viteezy.gateways.slack.SlackService;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.pricing.ShippingService;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@EnableAsync
@Configuration("mailConfiguration")
@Import({
        viteezy.service.IoC.class
})
public class IoC {

    private final MailConfiguration mailConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.mailConfiguration = viteezyConfiguration.getMailConfiguration();
    }

    @Bean("javaMailSender")
    public JavaMailSender javaMailSender() {
        final String server = mailConfiguration.getServer();
        final Integer port = mailConfiguration.getPort();
        final String username = mailConfiguration.getUsername();
        final String password = mailConfiguration.getPassword();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties javaMailProperties = mailSender.getJavaMailProperties();
        mailSender.setHost(server);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        javaMailProperties.put("mail.smtp.auth", StringUtils.isNotBlank(password));
        if ((port>25 && port!=1025) || StringUtils.isNotBlank(password)) {
            javaMailProperties.put("mail.smtp.starttls.enable", "true");
            javaMailProperties.put("mail.smtp.starttls.required", "true");
            javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        }
        if (isTestMode()) {
            javaMailProperties.put("mail.debug", "true");
        }
        return mailSender;
    }

    private boolean isTestMode() {
        return mailConfiguration.getFrontendBaseUrl().contains("frontend");
    }

    @Bean("emailSender")
    public EmailSender emailSender (SpringTemplateEngine springTemplateEngine, JavaMailSender javaMailSender,
                                    SlackService slackService) {
        return new EmailSenderImpl(springTemplateEngine, javaMailSender, slackService, mailConfiguration);
    }

    @Bean("emailService")
    public EmailService emailService(ShippingService shippingService, EmailSender emailSender,
                                     BlendIngredientService blendIngredientService) {
        return new EmailServiceImpl(shippingService, emailSender, blendIngredientService);
    }

    @Bean("springTemplateEngine")
    public SpringTemplateEngine springTemplateEngine(SpringResourceTemplateResolver springResourceTemplateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(springResourceTemplateResolver);
        return templateEngine;
    }

    @Bean("springResourceTemplateResolver")
    public SpringResourceTemplateResolver springResourceTemplateResolver(){
        SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
        emailTemplateResolver.setPrefix("classpath:/templates/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode("HTML");
        emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return emailTemplateResolver;
    }
}
