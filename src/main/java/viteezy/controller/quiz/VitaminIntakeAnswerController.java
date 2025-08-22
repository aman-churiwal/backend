package viteezy.controller.quiz;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.quiz.VitaminIntakeAnswerGetResponse;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.VitaminIntakeAnswer;
import viteezy.service.quiz.VitaminIntakeAnswerService;
import viteezy.traits.ControllerResponseTrait;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Path("quiz")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "VitaminIntake answers")
public class VitaminIntakeAnswerController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitaminIntakeAnswerController.class);

    private final VitaminIntakeAnswerService service;

    @Autowired
    public VitaminIntakeAnswerController(
            @Qualifier("vitaminIntakeAnswerService") VitaminIntakeAnswerService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/answer/vitamin-intake/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = VitaminIntakeAnswerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getById(@PathParam("id") Long id) {
        final Either<Throwable, Response> foldedEither = service
                .find(id)
                .map(foldOptional());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @GET
    @Path("/{quizExternalReference}/answer/vitamin-intake")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = VitaminIntakeAnswerGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByQuizExternalReference(@PathParam("quizExternalReference") UUID quizExternalReference) {
        final Either<Throwable, Response> foldedEither = service
                .find(quizExternalReference)
                .map(foldOptional());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @POST
    @Path("/{quizExternalReference}/answer/vitamin-intake/{vitaminIntakeId}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = VitaminIntakeAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("vitaminIntakeId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .save(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(VitaminIntakeAnswerGetResponse::from)
                .map(getCreatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @PUT
    @Path("/{quizExternalReference}/answer/vitamin-intake/{vitaminIntakeId}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = VitaminIntakeAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response update(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("vitaminIntakeId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .update(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(VitaminIntakeAnswerGetResponse::from)
                .map(getUpdatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    private Function<Optional<VitaminIntakeAnswer>, Response> foldOptional() {
        return optional -> optional
                .map(VitaminIntakeAnswerGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }
}