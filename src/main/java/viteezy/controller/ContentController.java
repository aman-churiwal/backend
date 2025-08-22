package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
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
import viteezy.service.ContentService;
import viteezy.traits.ControllerResponseTrait;

@Path("content")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Content")
public class ContentController implements ControllerResponseTrait {

    private final ContentService contentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentController.class);

    public ContentController(@Qualifier("contentService") ContentService contentService) {
        this.contentService = contentService;
    }

    @GET
    @Timed
    @Path("/{code}")
    public Response getByCode(@PathParam("code") String code) {
        return contentService.findByCode(code)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }
}

