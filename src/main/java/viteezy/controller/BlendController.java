package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.BlendGetResponse;
import viteezy.controller.dto.QuizBlendRelationGetResponse;
import viteezy.domain.blend.Blend;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.blend.BlendStatus;
import viteezy.service.blend.BlendService;
import viteezy.service.blend.preview.QuizBlendAggregator;
import viteezy.traits.ControllerResponseTrait;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Path("blends")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Blend")
public class BlendController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlendController.class);

    private final BlendService service;
    private final QuizBlendAggregator quizBlendAggregator;

    @Autowired
    public BlendController(
            @Qualifier("blendService") BlendService service,
            @Qualifier("quizBlendAggregator") QuizBlendAggregator quizBlendAggregator) {
        this.service = service;
        this.quizBlendAggregator = quizBlendAggregator;
    }

    @GET
    @Path("/{externalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BlendGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response get(@PathParam("externalReference") UUID externalReference) {
        return service.find(externalReference)
                .map(BlendGetResponse::from)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @PATCH
    @Path("/{externalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BlendGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response update(
            @PathParam("externalReference") UUID externalReference
    ) {
        return quizBlendAggregator.updateAggregatedV2(externalReference)
                .map(BlendGetResponse::from)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Path("/customer/{customerExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BlendGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByCustomerExternalReference(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return service.findByCustomerExternalReference(customerExternalReference)
                .map(BlendGetResponse::from)
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @GET
    @Timed
    @Path("/payment-plan/{paymentPlanExternalReference}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = QuizBlendRelationGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getByPaymentPlanExternalReference(@PathParam("paymentPlanExternalReference") UUID paymentPlanExternalReference) {
        return quizBlendAggregator.findByPaymentPlanExternalReference(paymentPlanExternalReference)
                .map(buildQuizBlendRelationGetResponse())
                .fold(getFailureResponse(LOGGER), this::getOkResponseWithEntity);
    }

    @POST
    @Path("/create")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BlendGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response create() {
        return service.create(null, null, BlendStatus.CREATED)
                .map(BlendGetResponse::from)
                .fold(getFailureResponse(LOGGER), getCreatedEntityResponse());
    }

    @POST
    @Path("/bundle/{bundleCode}")
    public Response create(@PathParam("bundleCode") String bundleCode) {
        return service.createBundle(bundleCode)
                .map(BlendGetResponse::from)
                .fold(getFailureResponse(LOGGER), getCreatedEntityResponse());
    }

    private Function<Tuple2<Blend, Optional<Quiz>>, QuizBlendRelationGetResponse> buildQuizBlendRelationGetResponse() {
        return tuple -> {
            final UUID quizExternalReference = tuple._2.map(Quiz::getExternalReference).orElse(null);
            return QuizBlendRelationGetResponse.from(tuple._1, quizExternalReference);
        };
    }
}