package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.AddressCheckPostRequest;
import viteezy.gateways.postnl.PostNlService;
import viteezy.traits.ControllerResponseTrait;

@Path("postnl")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Post NL")
public class PostNlController implements ControllerResponseTrait {

    private final PostNlService postNlService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PostNlController.class);

    public PostNlController(@Qualifier("postNlService") PostNlService postNlService) {
        this.postNlService = postNlService;
    }

    @POST
    @Timed
    @Path("/check-address")
    public Response checkAddress(@NotNull @Valid AddressCheckPostRequest addressCheckPostRequest) {
        return postNlService.checkAddress(addressCheckPostRequest)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }
}

