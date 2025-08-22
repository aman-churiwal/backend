package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import viteezy.domain.pricing.IncentiveType;
import viteezy.service.pricing.IncentiveService;
import viteezy.traits.ControllerResponseTrait;

import java.util.UUID;

@Path("/incentive")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Incentive")
public class IncentiveController implements ControllerResponseTrait {
    private static final Logger LOGGER = LoggerFactory.getLogger(IncentiveController.class);

    private final IncentiveService incentiveService;

    @Autowired
    public IncentiveController(IncentiveService incentiveService) {
        this.incentiveService = incentiveService;
    }

    @PATCH
    @Timed
    @Path("/{incentiveType}/{planExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = ""),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response get(@PathParam("incentiveType") IncentiveType incentiveType, @PathParam("planExternalReference") UUID planExternalReference) {
        return incentiveService.save(planExternalReference, incentiveType)
                .map(__ -> null)
                .map(getUpdatedEntityResponse())
                .getOrElseGet(getFailureResponse(LOGGER));
    }
}
