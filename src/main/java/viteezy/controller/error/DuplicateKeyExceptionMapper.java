package viteezy.controller.error;

import io.dropwizard.jersey.errors.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Provider
@Component
public class DuplicateKeyExceptionMapper implements ExceptionMapper<DuplicateKeyException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DuplicateKeyExceptionMapper.class);

    @Override
    public Response toResponse(DuplicateKeyException exception) {
        Response.StatusType type = Response.Status.CONFLICT;
        LOGGER.error("Captured exception at controller level. exception={}", exception.getMessage());
        ErrorMessage errorMessage = buildErrorMessage(exception, type);
        return Response.status(type.getStatusCode())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private ErrorMessage buildErrorMessage(Throwable exception, Response.StatusType type) {
        return new ErrorMessage(type.getStatusCode(), exception.getMessage());
    }
}
