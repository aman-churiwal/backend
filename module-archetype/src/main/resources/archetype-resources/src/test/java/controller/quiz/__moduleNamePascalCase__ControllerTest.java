package ${package}.controller.quiz;

import io.vavr.control.Either;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ${package}.controller.dto.quiz.${moduleNamePascalCase}GetResponse;
import ${package}.controller.dto.quiz.${moduleNamePascalCase}PostRequest;
import ${package}.domain.quiz.${moduleNamePascalCase};
import ${package}.service.quiz.${moduleNamePascalCase}Service;

import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class ${moduleNamePascalCase}ControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String CODE = "123";
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final ${moduleNamePascalCase} SAMPLE_ENTITY = new ${moduleNamePascalCase}(ID, NAME, CODE, LocalDateTime.now(), LocalDateTime.now());
    private static final ${moduleNamePascalCase}PostRequest SAMPLE_ENTITY_DTO = new ${moduleNamePascalCase}PostRequest(NAME, CODE);

    private final ${moduleNamePascalCase}Service service = Mockito.mock(${moduleNamePascalCase}Service.class);
    private ${moduleNamePascalCase}Controller controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new ${moduleNamePascalCase}Controller(service);
    }

    @Test
    void find() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.of(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((${moduleNamePascalCase}GetResponse) response.getEntity()).getName());

            Mockito.verify(service).find(ID);
        });
    }

    @Test
    void findNotFound() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.empty()));

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            controller.getById(ID);
        });

        Mockito.verify(service).find(ID);
    }

    @Test
    void findException() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).find(ID);
    }

    @Test
    void findAll() {
        Mockito.when(service.findAll())
                .thenReturn(Either.right(Collections.singletonList(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(1, ((List<${moduleNamePascalCase}GetResponse>) response.getEntity()).size());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((List<${moduleNamePascalCase}GetResponse>) response.getEntity()).get(0).getName());

            Mockito.verify(service).findAll();
        });
    }

    @Test
    void findAllEmpty() {
        Mockito.when(service.findAll())
                .thenReturn(Either.right(Collections.emptyList()));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(0, ((List<${moduleNamePascalCase}>) response.getEntity()).size());

            Mockito.verify(service).findAll();
        });

    }

    @Test
    void findAllException() {
        Mockito.when(service.findAll())
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).findAll();
    }


    @Test
    void save() {
        Mockito.when(service.save(Mockito.any(SAMPLE_ENTITY.getClass())))
                .thenReturn(Either.right(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(SAMPLE_ENTITY_DTO);
            Assertions.assertEquals(201, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((${moduleNamePascalCase}) response.getEntity()).getName());

            Mockito.verify(service).save(Mockito.any(SAMPLE_ENTITY.getClass()));
        });
    }

    @Test
    void saveException() {
        Mockito.when(service.save(Mockito.any(SAMPLE_ENTITY.getClass())))
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(SAMPLE_ENTITY_DTO);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).save(Mockito.any(SAMPLE_ENTITY.getClass()));
    }
}