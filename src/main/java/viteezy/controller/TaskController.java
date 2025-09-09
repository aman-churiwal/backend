package viteezy.controller;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import viteezy.db.CouponRepository;
import viteezy.traits.ControllerResponseTrait;

@Path("tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskController implements ControllerResponseTrait {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final CouponRepository couponRepository;

    public TaskController(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @POST
    @Path("/update-coupon-status")
    public Response updateCouponStatus() {
        final Try<Integer> result = couponRepository.updateStatusOfExpiredCoupons();
        return result.map(updatedCount -> getOkResponseWithEntity("{\"updated_coupons\": " + updatedCount + "}"))
                .getOrElseGet(getFailureResponse(LOGGER));
    }
}
