package viteezy.controller;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.service.LoginService;

import java.net.URI;

public class LoginControllerTest {

    private static final String SAMPLE_EMAIL = "example@mail.com";
    private static final String SAMPLE_TOKEN = "1234";

    private final LoginService loginService = Mockito.mock(LoginService.class);
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        Mockito.reset(loginService);
        loginController = new LoginController(loginService);
    }

    @Test
    void authenticate() {
        Mockito.when(loginService.authenticate(SAMPLE_EMAIL, SAMPLE_TOKEN))
                .thenReturn(Mockito.mock(URI.class));

        Assertions.assertDoesNotThrow(() -> {
            Response response = loginController.authenticate(SAMPLE_TOKEN, SAMPLE_EMAIL);
            Assertions.assertEquals(303, response.getStatus());

            Mockito.verify(loginService).authenticate(SAMPLE_EMAIL, SAMPLE_TOKEN);
        });
    }
}
