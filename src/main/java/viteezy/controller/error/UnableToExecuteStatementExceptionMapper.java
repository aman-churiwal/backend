package viteezy.controller.error;

import io.dropwizard.jersey.errors.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLIntegrityConstraintViolationException;

@Provider
@Component
public class UnableToExecuteStatementExceptionMapper implements ExceptionMapper<UnableToExecuteStatementException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnableToExecuteStatementExceptionMapper.class);

    @Override
    public Response toResponse(UnableToExecuteStatementException exception) {
        final boolean sqlIntegrityConstraintViolationException = exception.getCause() instanceof SQLIntegrityConstraintViolationException;
        LOGGER.error("Captured exception at controller level. exception={}", exception.getMessage());
        if (sqlIntegrityConstraintViolationException) {
            Response.StatusType type = Response.Status.CONFLICT;
            ErrorMessage errorMessage = buildErrorMessage(exception, type);
            return Response.status(type.getStatusCode())
                    .entity(errorMessage)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            Response.StatusType type = Response.Status.BAD_REQUEST;
            ErrorMessage errorMessage = buildErrorMessage(exception, type);
            return Response.status(type.getStatusCode())
                    .entity(errorMessage)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    private ErrorMessage buildErrorMessage(Throwable exception, Response.StatusType type) {
        return new ErrorMessage(type.getStatusCode(), String.valueOf(exception.getCause()));
    }
}