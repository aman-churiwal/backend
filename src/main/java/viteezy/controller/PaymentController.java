package viteezy.controller;

import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.*;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.service.payment.PaymentCallbackService;
import viteezy.service.payment.PaymentPlanService;
import viteezy.service.payment.PaymentService;
import viteezy.traits.ControllerResponseTrait;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Payment")
public class PaymentController implements ControllerResponseTrait {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final PaymentPlanService paymentPlanService;
    private final PaymentCallbackService paymentCallbackService;

    @Autowired
    public PaymentController(
            @Qualifier("paymentService") PaymentService paymentService,
            @Qualifier("paymentPlanService") PaymentPlanService paymentPlanService,
            @Qualifier("paymentCallbackService") PaymentCallbackService paymentCallbackService) {
        this.paymentService = paymentService;
        this.paymentPlanService = paymentPlanService;
        this.paymentCallbackService = paymentCallbackService;
    }

    @POST
    @Timed
    @Path("/blend/{blendExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = PaymentGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response create(
            @PathParam("blendExternalReference") UUID blendExternalReference,
            @CookieParam("_fbc") String fbcCookie,
            @CookieParam("_ga_44QWTLVVQT") String gaCookie,
            CheckoutPostRequest checkoutPostRequest) {
        String facebookClick = fbcCookie != null ? fbcCookie : checkoutPostRequest.getFbclid();
        return paymentService.create(blendExternalReference, checkoutPostRequest, facebookClick, parseGaCookie(gaCookie))
                .map(adapt())
                .fold(getFailureResponse(LOGGER), getCreatedEntityResponse());
    }

    @POST
    @Timed
    @Path("/retry/{planExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = PaymentGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response createRetryPayment(
            @PathParam("planExternalReference") UUID planExternalReference) {
        return paymentService.createRetryFirstPayment(planExternalReference)
                .map(adapt())
                .fold(getFailureResponse(LOGGER), getCreatedEntityResponse());
    }

    @GET
    @Timed
    @Path("/retry/{planExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = Payment.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response checkRetryPayment(
            @PathParam("planExternalReference") UUID planExternalReference) {
        return paymentService.getRetryPayment(planExternalReference)
                .fold(getFailureResponse(LOGGER), getCreatedEntityResponse());
    }

    @PATCH
    @Timed
    @Path("/blend/{blendExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = PaymentGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response update(@PathParam("blendExternalReference") UUID blendExternalReference, CheckoutPatchRequest checkoutPatchRequest) {
        final Try<Response> foldedEither = paymentPlanService.updateByBlend(blendExternalReference, checkoutPatchRequest)
                .map(getCreatedEntityResponse());
        return getResponseFromTry(foldedEither, LOGGER);
    }

    @GET
    @Timed
    @Path("/blend/{blendExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = PaymentPlanGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getPaymentPlanByBlend(@PathParam("blendExternalReference") UUID blendExternalReference) {
        final Try<Response> foldedEither = paymentPlanService.findActivePaymentPlanByBlendExternalReference(blendExternalReference)
                .map(adaptPlan())
                .map(this::getOkResponseWithEntity);
        return getResponseFromTry(foldedEither, LOGGER);
    }

    @GET
    @Timed
    @Path("/{paymentPlanStatus}/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = PaymentPlanGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getPaymentPlanByStatusAndCustomer(@PathParam("customerExternalReference") UUID customerExternalReference,
                                                      @PathParam("paymentPlanStatus") String paymentPlanStatus) {
        final Try<Response> foldedEither = paymentPlanService.findPaymentPlanByStatusAndCustomerExternalReference(customerExternalReference, PaymentPlanStatus.valueOf(paymentPlanStatus.toUpperCase()))
                .map(adaptPlan())
                .map(this::getOkResponseWithEntity);
        return getResponseFromTry(foldedEither, LOGGER);
    }

    @GET
    @Timed
    @Path("/payments/blend/{blendExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = PaymentsGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getAllCustomerPaymentsByBlend(@PathParam("blendExternalReference") UUID blendExternalReference) {
        final Try<Response> foldedEither = paymentService.getByBlendExternalReference(blendExternalReference)
                .map(foldList());
        return getResponseFromTry(foldedEither, LOGGER);
    }

    @GET
    @Timed
    @Path("/payments/{planExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = PaymentsGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getPaymentsByPlanExternalReference(@PathParam("planExternalReference") UUID planExternalReference) {
        final Try<Response> foldedEither = paymentService.getPayments(planExternalReference)
                .map(foldList());
        return getResponseFromTry(foldedEither, LOGGER);
    }

    @POST
    @Timed
    @Path("/test/callback")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response testCallback(@NotNull @Valid TestCallbackPaymentPostRequest testCallbackPaymentPostRequest) {
        return paymentCallbackService.testProcessCallback(testCallbackPaymentPostRequest)
                .fold(getFailureResponse(LOGGER), getEmptyOkResponse());
    }

    @POST
    @Timed
    @Path("/callback")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response callback(@FormParam("id") String id) {
        return paymentCallbackService.processCallback(id)
                .fold(getFailureResponse(LOGGER), getEmptyOkResponse());
    }

    @GET
    @Timed
    @Path("/plan/{planExternalReference}")
    public Response getPaymentPlan(@PathParam("planExternalReference") UUID planExternalReference) {
        return paymentPlanService.getPaymentPlan(planExternalReference)
                .map(adaptPlan())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @POST
    @Timed
    @Path("/plan/change-delivery-date/{planExternalReference}")
    public Response changeDeliveryDate(@PathParam("planExternalReference") UUID planExternalReference,
                                       @NotNull @Valid PausePaymentPlanPostRequest pausePaymentPlanPostRequest) {
        return paymentPlanService.changeDeliveryDate(planExternalReference, pausePaymentPlanPostRequest.getDeliveryDate().atStartOfDay())
                .map(adaptPlan())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @POST
    @Timed
    @Path("/plan/reactivate/{blendExternalReference}")
    public Response reactivatePaymentPlan(@PathParam("blendExternalReference") UUID blendExternalReference) {
        return paymentPlanService.reactivate(blendExternalReference)
                .map(adaptPlan())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @DELETE
    @Timed
    @Path("/plan/{planExternalReference}")
    public Response stopPaymentPlan(@PathParam("planExternalReference") UUID planExternalReference,
                                    @NotNull @Valid StopPaymentPlanPostRequest stopPaymentPlanPostRequest) {
        return paymentPlanService.stop(planExternalReference, stopPaymentPlanPostRequest.getStopReason())
                .map(adaptPlan())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Timed
    @CacheControl(maxAge = 86400)
    @Path("/methods")
    public Response getPaymentMethods(@NotNull @QueryParam("country") String country) {
        return paymentService.getPaymentMethods(country)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    private <T> Function<T, Response> getEmptyOkResponse() {
        return entity -> Response
                .status(Response.Status.OK)
                .build();
    }

    private Function<List<Payment>, Response> foldList() {
        return list -> {
            final List<PaymentsGetResponse> dtoList = list.stream()
                    .map(PaymentsGetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }

    private Function<PaymentResponse, PaymentGetResponse> adapt() {
        return PaymentGetResponse::from;
    }

    private Function<PaymentPlan, PaymentPlanGetResponse> adaptPlan() {
        return PaymentPlanGetResponse::from;
    }
}
