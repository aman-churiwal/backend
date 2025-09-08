package viteezy.service;

import be.woutschoovaerts.mollie.data.payment.SequenceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import viteezy.configuration.TripleWhaleConfiguration;
import viteezy.domain.Product;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.fulfilment.OrderStatus;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentStatus;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Activates Mockito and creates @Mock objects
@ExtendWith(MockitoExtension.class)
class TripleWhaleServiceImplTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mock
    private TripleWhaleConfiguration mockConfig;

    @Mock
    private HttpClient mockHttpClient;

    private TripleWhaleServiceImpl tripleWhaleService;

    @BeforeEach
    void setUp() throws Exception {
        tripleWhaleService = new TripleWhaleServiceImpl(mockConfig);

        // Inject mocked HttpClient into the private field
        Field httpClientField = TripleWhaleServiceImpl.class.getDeclaredField("httpClient");
        httpClientField.setAccessible(true);
        httpClientField.set(tripleWhaleService, mockHttpClient);
    }

    @Test
    void testSendOrder() {
        // Arrange
        when(mockConfig.getApiUrl()).thenReturn("https://api.triplewhale.com/api/v2/data-in");
        when(mockConfig.getApiKey()).thenReturn("90bcdc8c-c32e-4353-83ec-5da38da65c96");

        Order order = new Order(
                536L,
                UUID.fromString("d7defb58-d807-4083-9616-bcdfee8ca33"),
                "0000310406",
                31040L,
                SequenceType.FIRST,
                30342L,
                75145L,
                69083L,
                1,
                "Test",
                "Test",
                "Spiegelstraat",
                "38",
                null,
                "1405HX",
                "Bussum",
                "NL",
                "0623360738",
                "3094587023457@live.nl",
                null,
                null,
                "02811231317",
                OrderStatus.SHIPPED_TO_POSTNL,
                LocalDateTime.parse("2022-09-07 14:30:00", formatter),
                LocalDateTime.parse("2023-11-28 12:28:00", formatter),
                LocalDateTime.parse("2023-11-28 17:57:59", formatter)
        );

        Payment payment = new Payment(
                6L,
                new BigDecimal("49.99"),
                "tr_mH7U995L",
                null,
                3L,
                LocalDateTime.parse("2021-03-16 11:35:14", formatter),
                null,
                LocalDateTime.parse("2021-03-16 11:35:14", formatter),
                PaymentStatus.paid,
                null,
                SequenceType.FIRST
        );

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(mockHttpResponse));

        // Act
        tripleWhaleService.sendOrder(order, payment);

        // Assert
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(mockHttpClient, timeout(1000).times(1))
                .sendAsync(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

        HttpRequest capturedRequest = requestCaptor.getValue();
        assertEquals("POST", capturedRequest.method());
//        assertEquals(URI.create("https://api.triplewhale.com/orders"), capturedRequest.uri());
//        assertEquals("Bearer test_api_key",
//                capturedRequest.headers().firstValue("Authorization").orElse(null));
    }

    @Test
    void testSendProduct() {
        // Arrange
        when(mockConfig.getApiUrl()).thenReturn("https://api.triplewhale.com");
        when(mockConfig.getApiKey()).thenReturn("test_api_key");

        Product product = new Product(
                2L,
                "Energy Bundel",
                "Vermoeidheid",
                "Zeg dag tegen inkakkers. Met de Energy Bundel kan jij de wereld aan en is je middagdip verleden tijd. " +
                        "De meest krachtige ingrediënten uit de natuur zijn gebundeld in een dagelijkse samenstelling " +
                        "om jouw energieniveau op pijl te houden.",
                "energy-bundle",
                "energie",
                true,
                true,
                LocalDateTime.parse("2022-12-30 20:15:05", formatter),
                LocalDateTime.parse("2022-12-30 20:15:05", formatter)
        );

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(mockHttpResponse));

        // Act
        tripleWhaleService.sendProduct(product);

        // Assert
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(mockHttpClient, timeout(1000).times(1))
                .sendAsync(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

        HttpRequest capturedRequest = requestCaptor.getValue();
        assertEquals("POST", capturedRequest.method());
        assertEquals(URI.create("https://api.triplewhale.com/products"), capturedRequest.uri());
//        assertEquals("Bearer test_api_key",
//                capturedRequest.headers().firstValue("Authorization").orElse(null));
    }
}
