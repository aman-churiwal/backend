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
import viteezy.controller.dto.quiz.AllergyTypeAnswerGetResponse;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AllergyTypeAnswer;
import viteezy.service.quiz.AllergyTypeAnswerService;
import viteezy.traits.ControllerResponseTrait;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("quiz")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "AllergyType answers")
public class AllergyTypeAnswerController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllergyTypeAnswerController.class);

    private final AllergyTypeAnswerService service;

    @Autowired
    public AllergyTypeAnswerController(
            @Qualifier("allergyTypeAnswerService") AllergyTypeAnswerService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/answer/allergy-types/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = AllergyTypeAnswerGetResponse.class),
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
    @Path("/{quizExternalReference}/answer/allergy-types")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = AllergyTypeAnswerGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByQuizExternalReference(@PathParam("quizExternalReference") UUID quizExternalReference) {
        final Either<Throwable, Response> foldedEither = service
                .find(quizExternalReference)
                .map(foldList());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @POST
    @Path("/{quizExternalReference}/answer/allergy-types/{allergyTypeId}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = AllergyTypeAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("allergyTypeId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .save(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(AllergyTypeAnswerGetResponse::from)
                .map(getCreatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @DELETE
    @Path("/{quizExternalReference}/answer/allergy-types/{allergyTypeId}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = AllergyTypeAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response delete(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("allergyTypeId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .delete(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(getUpdatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    private Function<Optional<AllergyTypeAnswer>, Response> foldOptional() {
        return optional -> optional
                .map(AllergyTypeAnswerGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }

    private Function<List<AllergyTypeAnswer>, Response> foldList() {
        return list -> {
            final List<AllergyTypeAnswerGetResponse> dtoList = list.stream()
                    .map(AllergyTypeAnswerGetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }
}