package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.traits.ControllerResponseTrait;

import java.util.Optional;
import java.util.UUID;

@Path("klaviyo")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Klaviyo")
public class KlaviyoController implements ControllerResponseTrait {

    private final KlaviyoService klaviyoService;
    private final CustomerService customerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(KlaviyoController.class);

    public KlaviyoController(@Qualifier("klaviyoService") KlaviyoService klaviyoService,
                             @Qualifier("customerService") CustomerService customerService) {
        this.klaviyoService = klaviyoService;
        this.customerService = customerService;
    }

    @GET
    @Timed
    @Path("/products")
    public Response getAll() {
        return klaviyoService.getProducts()
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @POST
    @Timed
    @Path("/notify-checkout/blend/{blendExternalReference}")
    public Response notifyCheckout(@PathParam("blendExternalReference") UUID blendExternalReference) {
        return customerService.findByBlend(blendExternalReference)
                .filter(customer -> customer.getKlaviyoProfileId() != null)
                .peek(customer -> klaviyoService.createEvent(customer, KlaviyoConstant.SEEN_CHECKOUT, null, Optional.empty()))
                .map(__ -> null)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }
}

