package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
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
import viteezy.controller.dto.CustomerAddressPostRequest;
import viteezy.controller.dto.CustomerGetResponse;
import viteezy.controller.dto.CustomerReferralGetResponse;
import viteezy.controller.dto.quiz.EmailPostRequest;
import viteezy.domain.Customer;
import viteezy.domain.pricing.ReferralDiscount;
import viteezy.service.CustomerService;
import viteezy.service.pricing.ReferralService;
import viteezy.traits.ControllerResponseTrait;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Customer")
public class CustomerController implements ControllerResponseTrait {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;
    private final ReferralService referralService;
    private final ReferralDiscount referralDiscount;

    @Autowired
    public CustomerController(
            @Qualifier("customerService") CustomerService customerService,
            @Qualifier("referralService") ReferralService referralService
    ) {
        this.customerService = customerService;
        this.referralService = referralService;
        this.referralDiscount = referralService.getReferralDiscount();
    }

    @PUT
    @Timed
    @Path("/customer/blend/{blendExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response updateAddress(@PathParam("blendExternalReference") UUID blendExternalReference,
                                  CustomerAddressPostRequest customer,
                                  @CookieParam("_ga") String gaCookie,
                                  @CookieParam("_fbp") String facebookPixel,
                                  @HeaderParam("User-Agent") String userAgent,
                                  @HeaderParam("X-Forwarded-For") String userIpAddress
    ) {
        String gaId = parseGaCookie(gaCookie);
        return customerService.createCustomer(blendExternalReference, customer, gaId, facebookPixel, userAgent, parseIpAddress(userIpAddress))
                .map(CustomerGetResponse::from)
                .map(getUpdatedEntityResponse())
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @Path("/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response findByCustomer(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return customerService.find(customerExternalReference)
                .map(CustomerGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @Path("/customer/blend/{blendExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response findByBlend(@PathParam("blendExternalReference") UUID blendExternalReference) {
        return customerService.findByBlend(blendExternalReference)
                .map(CustomerGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @Path("/customer/payment-plan/{paymentPlanExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByPaymentPlanExternalReference(@PathParam("paymentPlanExternalReference") UUID paymentPlanExternalReference) {
        Try<Response> foldedEither = customerService.findByPaymentPlanExternalReference(paymentPlanExternalReference)
                .map(CustomerGetResponse::from)
                .map(this::getOkResponseWithEntity);
        return getResponseFromTry(foldedEither, LOGGER);
    }

    @GET
    @Timed
    @Path("/customer/email/{email}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = Boolean.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response findByEmailWithActivePaymentPlan(@PathParam("email") String email) {
        return customerService.findByEmailWithActivePaymentPlan(email)
                .map(__ -> Boolean.TRUE)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Path("/customer/email/quiz/{quizExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getCustomerByQuizExternalReference(@PathParam("quizExternalReference") UUID quizExternalReference) {
        final Try<Response> foldedEither = customerService.findByQuiz(quizExternalReference)
                .map(CustomerGetResponse::from)
                .map(this::getOkResponseWithEntity);
        return getResponseFromTry(foldedEither, LOGGER);
    }

    @GET
    @Path("/customer/referral/{referralCode}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByReferral(@PathParam("referralCode") String referralCode) {
        final Try<Response> foldedEither = referralService.findCustomerByReferralCode(referralCode)
                .map(foldReferralOptional(referralDiscount));
        return getResponseFromTry(foldedEither, LOGGER);
    }

    @POST
    @Path("/customer/email/quiz/{quizExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response saveEmail(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @NotNull @Valid EmailPostRequest emailPostRequest,
            @CookieParam("_ga") String gaCookie,
            @CookieParam("_fbp") String facebookPixel,
            @CookieParam("_fbc") String fbcCookie,
            @HeaderParam("User-Agent") String userAgent,
            @HeaderParam("X-Forwarded-For") String userIpAddress
    ) {
        String gaId = parseGaCookie(gaCookie);
        String facebookClick = fbcCookie != null ? fbcCookie : emailPostRequest.getFbclid();
        return customerService
                .saveEmailIfNotActive(quizExternalReference, emailPostRequest, gaId, facebookPixel, facebookClick, userAgent, parseIpAddress(userIpAddress))
                .map(CustomerGetResponse::from)
                .map(getCreatedEntityResponse())
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @PUT
    @Path("/customer/email/quiz/{quizExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response updateEmail(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @CookieParam("_fbc") String fbcCookie,
            @NotNull @Valid EmailPostRequest emailPostRequest
    ) {
        String facebookClick = fbcCookie != null ? fbcCookie : emailPostRequest.getFbclid();
        final Try<Response> foldedEither = customerService
                .updateEmail(quizExternalReference, emailPostRequest, facebookClick)
                .map(CustomerGetResponse::from)
                .map(getUpdatedEntityResponse());
        return getResponseFromTry(foldedEither, LOGGER);
    }

    private Function<Optional<Customer>, Response> foldReferralOptional(ReferralDiscount referralDiscount) {
        return optional -> optional
                .map(customer -> CustomerReferralGetResponse.from(customer, referralDiscount))
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }
}
