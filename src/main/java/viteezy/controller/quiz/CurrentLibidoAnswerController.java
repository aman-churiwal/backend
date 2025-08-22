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
import viteezy.controller.dto.quiz.CurrentLibidoAnswerGetResponse;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.CurrentLibidoAnswer;
import viteezy.service.quiz.CurrentLibidoAnswerService;
import viteezy.traits.ControllerResponseTrait;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Path("quiz")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "CurrentLibido answers")
public class CurrentLibidoAnswerController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentLibidoAnswerController.class);

    private final CurrentLibidoAnswerService service;

    @Autowired
    public CurrentLibidoAnswerController(
            @Qualifier("currentLibidoAnswerService") CurrentLibidoAnswerService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/answer/current-libido/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CurrentLibidoAnswerGetResponse.class),
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
    @Path("/{quizExternalReference}/answer/current-libido")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = CurrentLibidoAnswerGetResponse.class),
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
    @Path("/{quizExternalReference}/answer/current-libido/{currentLibidoId}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = CurrentLibidoAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("currentLibidoId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .save(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(CurrentLibidoAnswerGetResponse::from)
                .map(getCreatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @PUT
    @Path("/{quizExternalReference}/answer/current-libido/{currentLibidoId}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = CurrentLibidoAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response update(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("currentLibidoId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .update(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(CurrentLibidoAnswerGetResponse::from)
                .map(getUpdatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    private Function<Optional<CurrentLibidoAnswer>, Response> foldOptional() {
        return optional -> optional
                .map(CurrentLibidoAnswerGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }
}