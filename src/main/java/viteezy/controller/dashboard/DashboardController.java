package viteezy.controller.dashboard;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
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
import viteezy.controller.dto.dashboard.IngredientPostRequest;
import viteezy.controller.dto.dashboard.PaymentPatchRequest;
import viteezy.controller.dto.dashboard.PaymentPlanPatchRequest;
import viteezy.domain.fulfilment.OrderStatus;
import viteezy.service.CustomerService;
import viteezy.service.IngredientService;
import viteezy.service.LoggingService;
import viteezy.service.blend.BlendService;
import viteezy.service.fulfilment.CSVWriterService;
import viteezy.service.fulfilment.OrderService;
import viteezy.service.fulfilment.XMLWriterService;
import viteezy.service.payment.PaymentPlanService;
import viteezy.service.payment.PaymentService;
import viteezy.traits.ControllerResponseTrait;

import java.util.UUID;

@Path("dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Dashboard")
public class DashboardController implements ControllerResponseTrait {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    private final CustomerService customerService;
    private final PaymentService paymentService;
    private final PaymentPlanService paymentPlanService;
    private final OrderService orderService;
    private final BlendService blendService;
    private final CSVWriterService csvWriterService;
    private final XMLWriterService xmlWriterService;
    private final IngredientService ingredientService;
    private final LoggingService loggingService;

    @Autowired
    public DashboardController(
            @Qualifier("customerService") CustomerService customerService,
            @Qualifier("paymentService") PaymentService paymentService,
            @Qualifier("paymentPlanService") PaymentPlanService paymentPlanService,
            @Qualifier("orderService") OrderService orderService,
            @Qualifier("blendService") BlendService blendService,
            @Qualifier("CSVWriterService") CSVWriterService csvWriterService,
            @Qualifier("XMLWriterService") XMLWriterService xmlWriterService,
            @Qualifier("ingredientService") IngredientService ingredientService,
            @Qualifier("loggingService") LoggingService loggingService) {
        this.customerService = customerService;
        this.paymentService = paymentService;
        this.paymentPlanService = paymentPlanService;
        this.orderService = orderService;
        this.blendService = blendService;
        this.csvWriterService = csvWriterService;
        this.xmlWriterService = xmlWriterService;
        this.ingredientService = ingredientService;
        this.loggingService = loggingService;
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response findByCustomer(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return customerService.find(customerExternalReference)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @PATCH
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response updateIfNoActivePaymentPlan(
            @PathParam("customerExternalReference") UUID customerExternalReference,
            CustomerPatchRequest customer) {
        return customerService.update(customerExternalReference, customer)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/customer/email/{email}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response findByEmail(@PathParam("email") String email) {
        return customerService.findByEmailWithNeedle(email)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/customer/phone-number/{phoneNumber}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response findByPhoneNumber(@PathParam("phoneNumber") String phoneNumber) {
        return customerService.findByPhoneNumberWithNeedle(phoneNumber)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/customer/postcode/{postcode}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response findByPostcode(@PathParam("postcode") String postcode) {
        return customerService.findByPostcodeWithNeedle(postcode)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/customer/name/{name}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CustomerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response findByFullName(@PathParam("name") String name) {
        return customerService.findByNameWithNeedle(name)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/payment-plans/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = PaymentPlanGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getAllPaymentPlans(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return paymentPlanService.findPaymentPlans(customerExternalReference)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/logging/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = PaymentPlanGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getAllLogging(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return loggingService.findAll(customerExternalReference)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @DELETE
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/payment-plan/{planExternalReference}")
    public Response stopPaymentPlan(@PathParam("planExternalReference") UUID planExternalReference,
                                    @NotNull @Valid StopPaymentPlanPostRequest stopPaymentPlanPostRequest) {
        return paymentPlanService.stop(planExternalReference, stopPaymentPlanPostRequest.getStopReason())
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @PATCH
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/payment-plan/change-status")
    public Response updatePaymentPlanWithStatus(PaymentPlanPatchRequest paymentPlanPatchRequest) {
        return paymentPlanService.updateStatus(paymentPlanPatchRequest)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @PATCH
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/payment-plan/{planExternalReference}/coupon/{couponCode}")
    public Response updatePaymentPlanWithCoupon(@PathParam("planExternalReference") UUID planExternalReference,
                                                @PathParam("couponCode") String couponCode) {
        return paymentPlanService.applyRecurringCoupon(planExternalReference, couponCode)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/payments/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = PaymentsGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getPaymentsByCustomerExternalReference(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return paymentService.getByCustomerExternalReference(customerExternalReference)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @PATCH
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/payments/change-status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = PaymentsGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response updatePaymentWithStatus(PaymentPatchRequest paymentPatchRequest) {
        return paymentService.updateStatus(paymentPatchRequest)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/orders/customer/{customerExternalReference}")
    public Response getOrdersByCustomerExternalReference(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return orderService.findAll(customerExternalReference)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @POST
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/orders/duplicate/{orderExternalReference}")
    public Response createDuplicateOrder(@PathParam("orderExternalReference") UUID orderExternalReference) {
        return orderService.createDuplicateOrder(orderExternalReference)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @POST
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/orders/cancel/{orderExternalReference}")
    public Response cancelOrder(@PathParam("orderExternalReference") UUID orderExternalReference) {
        return orderService.cancelOrder(orderExternalReference)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/blends/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = PaymentsGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getBlendsByCustomerExternalReference(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return blendService.findAllByCustomerExternalReference(customerExternalReference)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @POST
    @Timed
    @RolesAllowed({"ADMIN"})
    @Path("/pharmacist-request/create")
    public Response createPharmacistRequest() {
        return csvWriterService.createPharmacistRequest()
                .map(pharmacistOrderLines -> xmlWriterService.createDeliveryOrderMessages())
                .map(__ -> null)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Timed
    @RolesAllowed({"ADMIN"})
    @Path("/pharmacist-request/orders")
    public Response getOrdersPackingSlipReady() {
        return orderService.findByStatus(OrderStatus.PACKING_SLIP_READY)
                .map(orders -> (long) orders.size())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @POST
    @Timed
    @RolesAllowed({"ADMIN"})
    @Path("/ingredient")
    public Response createIngredient(IngredientPostRequest ingredientPostRequest) {
        return ingredientService.save(ingredientPostRequest)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @PATCH
    @Timed
    @RolesAllowed({"ADMIN"})
    @Path("/ingredient")
    public Response updateIngredient(IngredientPostRequest ingredientPostRequest) {
        return ingredientService.update(ingredientPostRequest)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Timed
    @RolesAllowed({"ADMIN"})
    @Path("/ingredients")
    public Response getIngredients() {
        return ingredientService.findAll()
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }
}
