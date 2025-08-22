package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.CouponGetResponse;
import viteezy.domain.pricing.Coupon;
import viteezy.service.pricing.CouponService;
import viteezy.traits.ControllerResponseTrait;

import java.util.function.Function;

@Path("/coupon")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Payment")
public class CouponController implements ControllerResponseTrait {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponController.class);

    private final CouponService couponService;

    @Autowired
    public CouponController(
            @Qualifier("couponService") CouponService couponService
    ) {
        this.couponService = couponService;
    }

    @GET
    @Timed
    @Path("/{couponCode}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CouponGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response get(@PathParam("couponCode") String couponCode) {
        return couponService.findValid(couponCode, null, null)
                .map(adapt())
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    private Function<Coupon, CouponGetResponse> adapt() {
        return CouponGetResponse::from;
    }
}
