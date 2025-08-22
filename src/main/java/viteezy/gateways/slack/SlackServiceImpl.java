package viteezy.gateways.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.SlackConfiguration;
import viteezy.domain.payment.PaymentPlan;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class SlackServiceImpl implements SlackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlackServiceImpl.class);
    private final Slack slack;
    private final SlackConfiguration slackConfiguration;

    public SlackServiceImpl(SlackConfiguration slackConfiguration) {
        slack = Slack.getInstance();
        this.slackConfiguration = slackConfiguration;
    }

    @Override
    public void notify(PaymentPlan paymentPlan, boolean retriedPayment, boolean testMode) {
        if (StringUtils.isNotEmpty(slackConfiguration.getOrdersUrl())) {
            final String firstAmount = getFormat(paymentPlan.firstAmount());
            final String recurringAmount = getFormat(paymentPlan.recurringAmount());
            final String environment = testMode ? "test" : "live";
            final String text = retriedPayment ? String.format(":dollar: :dollar: Successful %s payment request!\n*Amount:* EUR %s", environment, firstAmount) : paymentPlan.recurringMonths().equals(1) ?
                    String.format(":dollar: New %s subscription!\n*First amount:* EUR %s\n*Recurring monthly amount:* EUR %s", environment, firstAmount, recurringAmount) :
                    String.format(":dollar: :dollar: :dollar: New %s subscription!\n*Recurring quarterly amount:* EUR %s", environment, recurringAmount);
            final Payload payload = Payload.builder().text(text).build();
            try {
                slack.send(slackConfiguration.getOrdersUrl(), payload);
            } catch (IOException e) {
                LOGGER.error("Slack", e);
            }
        }
    }

    @Override
    public void notify(Throwable throwable) {
        if (StringUtils.isNotEmpty(slackConfiguration.getErrorsUrl())) {
            final Payload payload = Payload.builder().text(throwable.getMessage()).build();
            try {
                slack.send(slackConfiguration.getErrorsUrl(), payload);
            } catch (IOException e) {
                LOGGER.error("Slack", e);
            }
        }
    }

    @Override
    public void notifyPharmacistRequest(Throwable throwable) {
        if (StringUtils.isNotEmpty(slackConfiguration.getErrorsUrl())) {
            final String environment = slackConfiguration.getEnvironment();
            final String text = MessageFormat.format("{0} {1} apotheker bestelling email error:\n {2}",
                    environment.equals("test") ? ":warning: :grey_exclamation:" : ":warning: :exclamation:",
                    slackConfiguration.getEnvironment(), throwable.getMessage());
            final Payload payload = Payload.builder().text(text).build();
            try {
                slack.send(slackConfiguration.getErrorsUrl(), payload);
            } catch (IOException e) {
                LOGGER.error("Slack", e);
            }
        }
    }

    private String getFormat(BigDecimal amount) {
        return NumberFormat.getNumberInstance(Locale.GERMANY).format(amount);
    }
}
