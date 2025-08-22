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
import viteezy.controller.dto.quiz.DailySixAlcoholicDrinksGetResponse;
import viteezy.controller.dto.quiz.DailySixAlcoholicDrinksPostRequest;
import viteezy.domain.quiz.DailySixAlcoholicDrinks;
import viteezy.service.quiz.DailySixAlcoholicDrinksService;
import viteezy.traits.ControllerResponseTrait;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("category/daily-six-alcoholic-drinks")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "DailySixAlcoholicDrinks")
public class DailySixAlcoholicDrinksController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailySixAlcoholicDrinksController.class);

    private final DailySixAlcoholicDrinksService service;

    @Autowired
    public DailySixAlcoholicDrinksController(
            @Qualifier("dailySixAlcoholicDrinksService") DailySixAlcoholicDrinksService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = DailySixAlcoholicDrinksGetResponse.class),
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
            @ApiResponse(code = 200, message = "", response = DailySixAlcoholicDrinksGetResponse.class, responseContainer = "List"),
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
            @ApiResponse(code = 201, message = "", response = DailySixAlcoholicDrinks.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(@NotNull @Valid DailySixAlcoholicDrinksPostRequest postRequestDto) {
        final Either<Throwable, Response> foldedEither = service
                .save(postRequestDto.to())
                .map(getCreatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    private Function<Optional<DailySixAlcoholicDrinks>, Response> foldOptional() {
        return optional -> optional
                .map(DailySixAlcoholicDrinksGetResponse::from)
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }

    private Function<List<DailySixAlcoholicDrinks>, Response> foldList() {
        return list -> {
            final List<DailySixAlcoholicDrinksGetResponse> dtoList = list.stream()
                    .map(DailySixAlcoholicDrinksGetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }
}