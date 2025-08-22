package viteezy.controller.quiz;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
import jakarta.annotation.security.DenyAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.quiz.AmountOfMeatConsumptionGetResponse;
import viteezy.controller.dto.quiz.AmountOfMeatConsumptionPostRequest;
import viteezy.domain.quiz.AmountOfMeatConsumption;
import viteezy.service.quiz.AmountOfMeatConsumptionService;
import viteezy.traits.ControllerResponseTrait;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("category/amount-of-meat-consumption")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "AmountOfMeatConsumption")
public class AmountOfMeatConsumptionController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmountOfMeatConsumptionController.class);

    private final AmountOfMeatConsumptionService service;

    @Autowired
    public AmountOfMeatConsumptionController(
            @Qualifier("amountOfMeatConsumptionService") AmountOfMeatConsumptionService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = AmountOfMeatConsumptionGetResponse.class),
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = AmountOfMeatConsumptionGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getAll() {
        final Either<Throwable, Response> foldedEither = service
                .findAll()
                .map(foldList());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    @POST
    @DenyAll
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = AmountOfMeatConsumption.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(@NotNull @Valid AmountOfMeatConsumptionPostRequest postRequestDto) {
        final Either<Throwable, Response> foldedEither = service
                .save(postRequestDto.to())
                .map(getCreatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    private Function<Optional<AmountOfMeatConsumption>, Response> foldOptional() {
        return optional -> optional
                .map(AmountOfMeatConsumptionGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }

    private Function<List<AmountOfMeatConsumption>, Response> foldList() {
        return list -> {
            final List<AmountOfMeatConsumptionGetResponse> dtoList = list.stream()
                    .map(AmountOfMeatConsumptionGetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }
}