package viteezy.controller;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;
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
import viteezy.controller.dto.BlendIngredientGetResponse;
import viteezy.service.blend.preview.QuizBlendPreviewServiceV2;
import viteezy.traits.ControllerResponseTrait;

import java.util.UUID;

@Path("quiz/{quizExternalReference}/preview")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Quiz aggregated")
public class QuizAggregatedInformationController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizAggregatedInformationController.class);
    private final QuizBlendPreviewServiceV2 quizBlendPreviewServiceV2;

    public QuizAggregatedInformationController(
            @Qualifier("quizAggregatedInformationServiceV2") QuizBlendPreviewServiceV2 quizBlendPreviewServiceV2
    ) {
        this.quizBlendPreviewServiceV2 = quizBlendPreviewServiceV2;
    }

    @GET
    @Path("/blends/v2")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BlendIngredientGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getBlendPreviewByQuizV2(
            @PathParam("quizExternalReference") UUID quizExternalReference) {
        final Try<Response> foldedTry = quizBlendPreviewServiceV2.getBlendPreviewV2(quizExternalReference)
                .map(this::getOkResponseWithEntity);
        return getResponseFromTry(foldedTry, LOGGER);

    }
}