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
import viteezy.controller.dto.quiz.HairTypeAnswerGetResponse;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.HairTypeAnswer;
import viteezy.service.quiz.HairTypeAnswerService;
import viteezy.traits.ControllerResponseTrait;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Path("quiz")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "HairType answers")
public class HairTypeAnswerController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(HairTypeAnswerController.class);

    private final HairTypeAnswerService service;

    @Autowired
    public HairTypeAnswerController(
            @Qualifier("hairTypeAnswerService") HairTypeAnswerService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/answer/hair-type/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = HairTypeAnswerGetResponse.class),
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
    @Path("/{quizExternalReference}/answer/hair-type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = HairTypeAnswerGetResponse.class),
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
    @Path("/{quizExternalReference}/answer/hair-type/{hairTypeId}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = HairTypeAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("hairTypeId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .save(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(HairTypeAnswerGetResponse::from)
                .map(getCreatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @PUT
    @Path("/{quizExternalReference}/answer/hair-type/{hairTypeId}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = HairTypeAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response update(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("hairTypeId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .update(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(HairTypeAnswerGetResponse::from)
                .map(getUpdatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    private Function<Optional<HairTypeAnswer>, Response> foldOptional() {
        return optional -> optional
                .map(HairTypeAnswerGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }
}