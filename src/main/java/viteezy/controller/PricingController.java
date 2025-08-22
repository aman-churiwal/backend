package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.PricingGetResponse;
import viteezy.controller.dto.PricingPostRequest;
import viteezy.service.pricing.PricingService;
import viteezy.traits.ControllerResponseTrait;

import java.util.UUID;

@Path("pricing")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Pricing")
public class PricingController implements ControllerResponseTrait {

    private final PricingService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingController.class);

    public PricingController(@Qualifier("pricingService") PricingService service) {
        this.service = service;
    }

    @POST
    @Timed
    @Path("/blend/{blendExternalReference}")
    public Response getBlendPricing(@PathParam("blendExternalReference") UUID blendExternalReference, PricingPostRequest pricingPostRequest) {
            return service.getBlendPricing(blendExternalReference, pricingPostRequest.getIngredientIds(), pricingPostRequest.getCouponCode(), pricingPostRequest.getMonthsSubscribed(), pricingPostRequest.getSubscription())
                .map(PricingGetResponse::from)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }
}