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
import viteezy.controller.dto.quiz.UsageGoalAnswerGetResponse;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.UsageGoalAnswer;
import viteezy.service.quiz.UsageGoalAnswerService;
import viteezy.traits.ControllerResponseTrait;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("quiz")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "UsageGoal answers")
public class UsageGoalAnswerController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageGoalAnswerController.class);

    private final UsageGoalAnswerService service;

    @Autowired
    public UsageGoalAnswerController(
            @Qualifier("usageGoalAnswerService") UsageGoalAnswerService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/answer/usage-goals/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = UsageGoalAnswerGetResponse.class),
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
    @Path("/{quizExternalReference}/answer/usage-goals")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = UsageGoalAnswerGetResponse.class, responseContainer = "List"),
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
    @Path("/{quizExternalReference}/answer/usage-goals/{usageGoalId}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = UsageGoalAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("usageGoalId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .save(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(UsageGoalAnswerGetResponse::from)
                .map(getCreatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @DELETE
    @Path("/{quizExternalReference}/answer/usage-goals/{usageGoalId}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = UsageGoalAnswerGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response delete(
            @PathParam("quizExternalReference") UUID quizExternalReference,
            @PathParam("usageGoalId") Long categoryId
    ) {
        final Either<Throwable, Response> foldedEither = service
                .delete(new CategorizedAnswer(quizExternalReference, categoryId))
                .map(getUpdatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    private Function<Optional<UsageGoalAnswer>, Response> foldOptional() {
        return optional -> optional
                .map(UsageGoalAnswerGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }

    private Function<List<UsageGoalAnswer>, Response> foldList() {
        return list -> {
            final List<UsageGoalAnswerGetResponse> dtoList = list.stream()
                    .map(UsageGoalAnswerGetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }
}