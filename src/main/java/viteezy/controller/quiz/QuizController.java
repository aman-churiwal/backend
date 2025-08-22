package viteezy.controller.quiz;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.Tuple2;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.QuizBlendRelationGetResponse;
import viteezy.controller.dto.quiz.QuizGetResponse;
import viteezy.controller.dto.quiz.QuizPostResponse;
import viteezy.domain.blend.Blend;
import viteezy.domain.quiz.Quiz;
import viteezy.service.blend.preview.QuizBlendAggregator;
import viteezy.service.quiz.QuizService;
import viteezy.traits.ControllerResponseTrait;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Path("quiz")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Quiz")
public class QuizController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizController.class);
    private final QuizService service;
    private final QuizBlendAggregator quizBlendAggregator;

    public QuizController(@Qualifier("quizService") QuizService service, QuizBlendAggregator quizBlendAggregator) {
        this.service = service;
        this.quizBlendAggregator = quizBlendAggregator;
    }

    @POST
    @Timed
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = QuizPostResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response saveQuiz(@CookieParam("_fbp") String facebookPixel,
                             @CookieParam("_fbc") String fbcCookie,
                             @HeaderParam("Referer") String referer,
                             @HeaderParam("User-Agent") String userAgent,
                             @HeaderParam("X-Forwarded-For") String userIpAddress
    ) {
        String facebookClick = parseFacebookClick(fbcCookie, referer);
        String utmContent = parseReferer(referer, "utm_content");
        Quiz quiz = createQuizObject(utmContent);
        return service.save(quiz, facebookPixel, facebookClick, userAgent, parseIpAddress(userIpAddress), referer)
                .map(adapt())
                .fold(getFailureResponse(LOGGER), getCreatedEntityResponse());
    }

    @GET
    @Timed
    @Path("/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = QuizGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByCustomerExternalReference(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return service.findByCustomerExternalReference(customerExternalReference)
                .map(adaptOptionalQuiz())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Path("/blend/{blendExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = QuizGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByBlendExternalReference(@PathParam("blendExternalReference") UUID blendExternalReference) {
        return quizBlendAggregator.findByBlendExternalReference(blendExternalReference)
                .map(adaptOptionalQuiz())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Path("/{externalReference}/blends")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = QuizBlendRelationGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByQuizExternalReference(@PathParam("externalReference") UUID externalReference) {
        return quizBlendAggregator
                .findByQuizExternalReference(externalReference)
                .map(buildQuizOptionalBlendRelationGetResponse())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);

    }

    @POST
    @Path("/{externalReference}/blends/v2")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = QuizBlendRelationGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response saveV2(
            @PathParam("externalReference") UUID externalReference
    ) {
        return quizBlendAggregator.saveAggregatedV2(externalReference)
                .map(buildQuizBlendRelationGetResponse(externalReference))
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    private Quiz createQuizObject(String utmContent) {
        return new Quiz(null, UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), null, utmContent);
    }

    private Function<Tuple2<Quiz, Optional<Blend>>, QuizBlendRelationGetResponse> buildQuizOptionalBlendRelationGetResponse() {
        return tuple2 -> {
            final UUID blendExternalReference = tuple2._2.map(Blend::getExternalReference).orElse(null);
            return QuizBlendRelationGetResponse.from(tuple2._1, blendExternalReference);
        };
    }

    private Function<Blend, QuizBlendRelationGetResponse> buildQuizBlendRelationGetResponse(UUID quizExternalReference) {
        return blend -> QuizBlendRelationGetResponse.from(blend, quizExternalReference);
    }

    private Function<Quiz, QuizPostResponse> adapt() {
        return QuizPostResponse::from;
    }

    private Function<Optional<Quiz>, Optional<QuizGetResponse>> adaptOptionalQuiz() {
        return optional -> optional.map(QuizGetResponse::from);
    }
}