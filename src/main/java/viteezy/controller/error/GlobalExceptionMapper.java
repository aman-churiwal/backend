package viteezy.controller.error;

import com.google.common.base.Throwables;
import io.dropwizard.jersey.errors.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;

@Provider
@Component
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionMapper.class);
    private static final List<Class> clientFaultClasses = Collections.singletonList(
            SQLIntegrityConstraintViolationException.class
    );

    @Override
    public Response toResponse(Throwable exception) {
        if (isAClientFaultException(exception)) {
            LOGGER.error("Captured exception at controller level. exception={}", exception.getMessage());
            Response.StatusType statusType = Response.Status.BAD_REQUEST;
            String message = getBadRequestMessage(exception);
            ErrorMessage errorMessage = new ErrorMessage(statusType.getStatusCode(), message);
            return Response.status(statusType)
                    .entity(errorMessage)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        } else {
            LOGGER.error("Captured exception at controller level. exception={}", Throwables.getStackTraceAsString(exception));
            Response.StatusType statusType = Response.Status.INTERNAL_SERVER_ERROR;
            ErrorMessage errorMessage = new ErrorMessage(statusType.getStatusCode(), exception.getMessage());
            return Response.status(statusType)
                    .entity(errorMessage)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }

    private boolean isAClientFaultException(Throwable exception){
        return clientFaultClasses.stream().anyMatch(clazz -> clazz.isInstance(exception.getCause()));
    }

    private String getBadRequestMessage(Throwable exception){
        if(exception.getCause() instanceof SQLIntegrityConstraintViolationException){
            return getSQLIntegrityConstrainViolationMessage((SQLIntegrityConstraintViolationException) exception.getCause());
        } else {
            return exception.getMessage();
        }
    }

    private String getSQLIntegrityConstrainViolationMessage(SQLIntegrityConstraintViolationException exception) {
        if (exception.getMessage().contains("Duplicate entry"))
            return "Entity already exists";
        else {
            return exception.getMessage();
        }
    }
}
