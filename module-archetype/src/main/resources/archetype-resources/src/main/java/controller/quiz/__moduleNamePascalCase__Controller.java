package ${package}.controller.quiz;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ${package}.controller.dto.quiz.${moduleNamePascalCase}GetResponse;
import ${package}.controller.dto.quiz.${moduleNamePascalCase}PostRequest;
import ${package}.domain.quiz.${moduleNamePascalCase};
import ${package}.service.quiz.${moduleNamePascalCase}Service;

import jakarta.annotation.security.DenyAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import ${package}.traits.ControllerResponseTrait;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("category/${moduleNameKebapCase}")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "${moduleNamePascalCase}")
public class ${moduleNamePascalCase}Controller implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(${moduleNamePascalCase}Controller.class);

    private final ${moduleNamePascalCase}Service service;

    @Autowired
    public ${moduleNamePascalCase}Controller(
            @Qualifier("${moduleNameCamelCase}Service") ${moduleNamePascalCase}Service service
    ) {
        this.service = service;
    }

    @GET
    @Path("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = ${moduleNamePascalCase}GetResponse.class),
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
            @ApiResponse(code = 200, message = "", response = ${moduleNamePascalCase}GetResponse.class, responseContainer = "List"),
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
            @ApiResponse(code = 201, message = "", response = ${moduleNamePascalCase}.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(@NotNull @Valid ${moduleNamePascalCase}PostRequest postRequestDto) {
        final Either<Throwable, Response> foldedEither = service
                .save(postRequestDto.to())
                .map(getCreatedEntityResponse());
        return getResponseFromEither(foldedEither, LOGGER);
    }

    private Function<Optional<${moduleNamePascalCase}>, Response> foldOptional() {
        return optional -> optional
                .map(${moduleNamePascalCase}GetResponse::from)
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }

    private Function<List<${moduleNamePascalCase}>, Response> foldList() {
        return list -> {
            final List<${moduleNamePascalCase}GetResponse> dtoList = list.stream()
                    .map(${moduleNamePascalCase}GetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }
}