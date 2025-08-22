package viteezy.controller;

import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;
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
import viteezy.controller.dto.IngredientGetResponse;
import viteezy.controller.dto.IngredientPostRequest;
import viteezy.domain.ingredient.Ingredient;
import viteezy.service.IngredientService;
import viteezy.traits.ControllerResponseTrait;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("ingredients")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Ingredient")
public class IngredientController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientController.class);

    private final IngredientService service;

    @Autowired
    public IngredientController(
            @Qualifier("ingredientService") IngredientService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/{id}")
    @CacheControl(maxAge = 86400)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = IngredientGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getById(@PathParam("id") Long id) {
        final Try<Response> foldedTry = service
                .findWithComponentsAndContent(id)
                .map(this::getOkResponseWithEntity);
        return getResponseFromTry(foldedTry, LOGGER);
    }

    @GET
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = IngredientGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getAll() {
        final Try<Response> foldedTry = service
                .findAllWithComponents()
                .map(this::getOkResponseWithEntity);
        return getResponseFromTry(foldedTry, LOGGER);
    }

    @GET
    @Path("/additional")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = IngredientGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getAdditional() {
        final Try<Response> foldedTry = service
                .findAdditional()
                .map(foldList());
        return getResponseFromTry(foldedTry, LOGGER);
    }

    @POST
    @DenyAll
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = Ingredient.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(@NotNull @Valid IngredientPostRequest postRequestDto) {
        final Try<Response> foldedTry = service
                .save(postRequestDto.to())
                .map(getCreatedEntityResponse());
        return getResponseFromTry(foldedTry, LOGGER);
    }

    private Function<List<Ingredient>, Response> foldList() {
        return list -> {
            final List<IngredientGetResponse> dtoList = list.stream()
                    .map(IngredientGetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }
}