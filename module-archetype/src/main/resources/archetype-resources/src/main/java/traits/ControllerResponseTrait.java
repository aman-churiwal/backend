package ${package}.traits;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.function.Function;

public interface ControllerResponseTrait {
    default Response getResponseFromEither(Either<Throwable, Response> foldedEither, Logger LOGGER) {
        return foldedEither.getOrElseGet(getFailureResponse(LOGGER));
    }

    default Response getResponseFromTry(Try<Response> foldedTry, Logger LOGGER) {
        return foldedTry.getOrElseGet(getFailureResponse(LOGGER));
    }

    default <T> Function<T, Response> getCreatedEntityResponse() {
        return entity -> Response
                .status(Response.Status.CREATED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(entity)
                .build();
    }


    default <T> Function<T, Response> getUpdatedEntityResponse() {
        return entity -> Response
                .status(Response.Status.ACCEPTED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(entity)
                .build();
    }

    default <T> Response getOkResponseWithEntity(T entity) {
        return Response
                .status(Response.Status.OK)
                .entity(entity)
                .build();
    }

    default Function<Throwable, Response> getFailureResponse(Logger LOGGER) {
        return throwable -> {
            final Response.Status statusCode;
            if (throwable instanceof DuplicateKeyException || (throwable.getCause() != null && throwable.getCause() instanceof SQLIntegrityConstraintViolationException)) {
                statusCode = Response.Status.CONFLICT;
            } else
            if (throwable instanceof NoSuchElementException || (throwable instanceof IllegalStateException && throwable.getMessage().equals("Expected one element, but found none"))) {
                statusCode = Response.Status.NOT_FOUND;
            } else {
                statusCode = Response.Status.BAD_REQUEST;
                // only log when not user-related error
                LOGGER.error("throwable", throwable);
            }
            return Response
                    .status(statusCode)
                    .entity(new ErrorMessage(statusCode.getStatusCode(), throwable.getClass().getSimpleName()))
                    .build();
        };
    }
}
