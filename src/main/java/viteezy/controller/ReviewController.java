package viteezy.controller;

import io.dropwizard.jersey.caching.CacheControl;
import io.swagger.annotations.Api;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.service.ReviewService;
import viteezy.traits.ControllerResponseTrait;

@Path("review")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Review")
public class ReviewController implements ControllerResponseTrait {

    private final ReviewService reviewService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    public ReviewController(@Qualifier("reviewService") ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GET
    @CacheControl(maxAge = 86400)
    @Path("/summary/{source}")
    public Response getSummary(@PathParam("source") String source) {
        return reviewService.summary(source)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }
}

