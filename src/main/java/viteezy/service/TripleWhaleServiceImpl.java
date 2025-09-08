package viteezy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.TripleWhaleConfiguration;
import viteezy.domain.Product;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.payment.Payment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class TripleWhaleServiceImpl implements TripleWhaleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripleWhaleServiceImpl.class);
    private final TripleWhaleConfiguration configuration;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TripleWhaleServiceImpl(TripleWhaleConfiguration configuration) {
        this.configuration = configuration;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void sendOrder(Order order, Payment payment) {
        try {
            // Create a map to build the JSON payload for the Triple Whale API
            String jsonBody = objectMapper.writeValueAsString(Map.of(
                    "order_id", order.getOrderNumber(),
                    "total", payment.getAmount(), // Using the amount from the Payment object
                    "email", order.getShipToEmail()
                    // You can add more fields here as required by Triple Whale's documentation
                    // e.g., "customer_id", "items", "currency", etc.
            ));

            String finalUrl = configuration.getApiUrl() + "/orders";
            LOGGER.info("@@@Constructed Triple Whale URL: {}", finalUrl);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(configuration.getApiUrl() + "/orders"))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", configuration.getApiKey())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Send the request asynchronously to avoid blocking the main thread
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() >= 300) {
                            LOGGER.error("Failed to send order to Triple Whale. Status: {}, Body: {}", response.statusCode(), response.body());
                        } else {
                            LOGGER.info("Successfully sent order {} to Triple Whale.", order.getOrderNumber());
                        }
                    });
        } catch (Exception e) {
            LOGGER.error("Error occurred while sending order to Triple Whale", e);
        }
    }

    @Override
    public void sendProduct(Product product) {
        try {
            String jsonBody = objectMapper.writeValueAsString(Map.of(
                    "product_id", product.getId(),
                    "name", product.getName(),
                    "category", product.getCategory()
                    // Add other relevant product fields
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(configuration.getApiUrl() + "/products"))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", configuration.getApiKey())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() >= 300) {
                            LOGGER.error("Failed to send product to Triple Whale. Status: {}, Body: {}", response.statusCode(), response.body());
                        } else {
                            LOGGER.info("Successfully sent product {} to Triple Whale.", product.getName());
                        }
                    });
        } catch (Exception e) {
            LOGGER.error("Error occurred while sending product to Triple Whale", e);
        }
    }
}