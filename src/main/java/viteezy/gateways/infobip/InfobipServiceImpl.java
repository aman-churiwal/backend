package viteezy.gateways.infobip;

import com.google.common.base.Throwables;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.postnl.TrackTrace;

import java.net.URI;
import java.text.MessageFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InfobipServiceImpl implements InfobipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfobipService.class);

    private static final String FROM = "Viteezy";
    private final SmsApi smsApi;

    public InfobipServiceImpl(SmsApi smsApi) {
        this.smsApi = smsApi;
    }

    @Override
    public void sendOrderAtPickUpLocation(Order order) {
        Try.run(() -> {
            final SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                    .messages(List.of(buildSmsTextualMessage(order)));

            final SmsResponse smsResponse = smsApi.sendSmsMessage(smsMessageRequest).execute();
            LOGGER.debug(smsResponse.toString());
        }).onFailure(peekException());
    }

    private SmsTextualMessage buildSmsTextualMessage(Order order) {
        final String message = MessageFormat.format("Hi {0}, je Viteezy pakket is bij een PostNL punt, ze hebben je helaas gemist. Zie: {1}", order.getShipToFirstName(), buildTrackTraceLink(order).getTrackTraceLink());
        return new SmsTextualMessage()
                .from(FROM)
                .addDestinationsItem(new SmsDestination().to(parsePhoneNumber(order)))
                .text(message);
    }

    private TrackTrace buildTrackTraceLink(Order order) {
        return new TrackTrace(URI.create("https://jouw.postnl.nl/track-and-trace/".concat(order.getTrackTraceCode()).concat("-").concat(order.getShipToCountryCode()).concat("-").concat(order.getShipToPostalCode())));
    }

    private String parsePhoneNumber(Order order) {
        return Try.of(() -> {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber parsedPhoneNumber = phoneUtil.parse(order.getShipToPhone(), order.getShipToCountryCode());
            return "" + parsedPhoneNumber.getCountryCode() + parsedPhoneNumber.getNationalNumber();
        }).getOrElseThrow((Supplier<NumberFormatException>) NumberFormatException::new);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
