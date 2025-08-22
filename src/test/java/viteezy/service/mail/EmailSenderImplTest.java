package viteezy.service.mail;

import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.codec.Hex;
import org.thymeleaf.spring4.SpringTemplateEngine;
import viteezy.configuration.mail.MailConfiguration;
import viteezy.gateways.slack.SlackService;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class EmailSenderImplTest {

    private final SpringTemplateEngine springTemplateEngine = Mockito.mock(SpringTemplateEngine.class);
    private final JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
    private final SlackService slackService = Mockito.mock(SlackService.class);
    private final MimeMessagePreparator mimeMessagePreparator = Mockito.mock(MimeMessagePreparator.class);

    private final String SERVER = "server";
    private final Integer PORT = 587;
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String SAMPLE_EMAIL = "example@mail.com";
    private final String FRONTEND_BASE_URL = "http://localhost:3000";
    private final String DEFAULT_ORDER_BCC = "";
    private final String PHARMACIST_CSV_RECIPIENT = "test@viteezy.nl";
    private final String PHARMACIST_CSV_SUBJECT = "Test bestanden";

    private final MailConfiguration mailConfiguration = new MailConfiguration(SERVER, PORT, USERNAME, PASSWORD, FRONTEND_BASE_URL, DEFAULT_ORDER_BCC, PHARMACIST_CSV_RECIPIENT, PHARMACIST_CSV_SUBJECT);

    private EmailSender emailSender;

    @BeforeEach
    public void setup() {
        emailSender = new EmailSenderImpl(springTemplateEngine, javaMailSender, slackService, mailConfiguration);
    }

    @Test
    public void sendSuccessfulMagicLink() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);

        Mockito.doNothing().when(javaMailSender).send(mimeMessagePreparator);

        javaMailSender.send(mimeMessagePreparator);

        Try<Void> sendMagicLink = emailSender.sendMagicLink(SAMPLE_EMAIL, String.valueOf(Hex.encode(bytes)));
        assertTrue(sendMagicLink.isSuccess());

        Mockito.verify(javaMailSender).send(mimeMessagePreparator);
    }
}
