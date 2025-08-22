package viteezy.controller;

import io.vavr.control.Try;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.PaymentPlanGetResponse;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.service.payment.PaymentCallbackService;
import viteezy.service.payment.PaymentPlanService;
import viteezy.service.payment.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

class PaymentControllerTest {

    private static final Long ID = 1L;
    private static final Long CUSTOMER_ID = 1L;
    private static final Long BLEND_ID = 1L;
    private static final UUID BLEND_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final BigDecimal FIRST_AMOUNT = new BigDecimal("10");
    private static final BigDecimal RECURRING_AMOUNT = new BigDecimal("10");
    private static final Integer RECURRING_MONTHS = 12;
    private static final UUID EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final PaymentPlanStatus SUBSCRIPTION_STATUS_ACTIVE = PaymentPlanStatus.ACTIVE;
    private static final PaymentPlan SAMPLE_ENTITY = new PaymentPlan(ID, FIRST_AMOUNT, RECURRING_AMOUNT, RECURRING_MONTHS,
            CUSTOMER_ID,BLEND_ID, EXTERNAL_REFERENCE, LocalDateTime.now(), LocalDateTime.now(),
            SUBSCRIPTION_STATUS_ACTIVE, LocalDateTime.now(), null, LocalDateTime.now(), Optional.empty(),
            Optional.empty(), null);
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());

    private final PaymentService service = Mockito.mock(PaymentService.class);
    private final PaymentPlanService paymentPlanService = Mockito.mock(PaymentPlanService.class);
    private final PaymentCallbackService paymentCallbackService = Mockito.mock(PaymentCallbackService.class);
    private PaymentController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new PaymentController(service, paymentPlanService, paymentCallbackService);
    }

    @Test
    void findByBlendExternalReference() {
        Mockito.when(paymentPlanService.findActivePaymentPlanByBlendExternalReference(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getPaymentPlanByBlend(BLEND_EXTERNAL_REFERENCE);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.externalReference(), ((PaymentPlanGetResponse) response.getEntity()).getExternalReference());

            Mockito.verify(paymentPlanService).findActivePaymentPlanByBlendExternalReference(BLEND_EXTERNAL_REFERENCE);
        });
    }

    @Test
    void findByBlendExternalReferenceException() {
        Mockito.when(paymentPlanService.findActivePaymentPlanByBlendExternalReference(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getPaymentPlanByBlend(BLEND_EXTERNAL_REFERENCE);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(paymentPlanService).findActivePaymentPlanByBlendExternalReference(BLEND_EXTERNAL_REFERENCE);
    }
}