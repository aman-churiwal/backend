package viteezy.controller;

import io.dropwizard.jersey.caching.CacheControl;
import io.swagger.annotations.Api;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.service.BundleService;
import viteezy.traits.ControllerResponseTrait;

@Path("bundle")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Bundle")
public class BundleController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(BundleController.class);

    private final BundleService service;

    @Autowired
    public BundleController(@Qualifier("bundleService") BundleService service) {
        this.service = service;
    }

    @GET
    @CacheControl(maxAge = 3600)
    public Response getBundles() {
        return service.findAllBundles()
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }
}