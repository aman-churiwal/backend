package viteezy.gateways.postnl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import jakarta.ws.rs.core.UriBuilder;
import kong.unirest.json.JSONObject;
import viteezy.configuration.postnl.PostNLConfiguration;
import viteezy.controller.dto.AddressCheckPostRequest;
import viteezy.domain.postnl.Address;
import viteezy.domain.postnl.AddressBodyKey;
import viteezy.domain.postnl.Shipment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

public class PostNlServiceImpl implements PostNlService {

    private static final String CUSTOMER_NUMBER = "10825993";
    private static final String ADDRESS_PATH = "/address/benelux/v1/validate";

    private final PostNLConfiguration postNlConfiguration;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public PostNlServiceImpl(PostNLConfiguration postNlConfiguration, ObjectMapper objectMapper) {
        this.postNlConfiguration = postNlConfiguration;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public Try<List<Address>> checkAddress(AddressCheckPostRequest addressCheckPostRequest) {
        return Try.of(() -> {
            final URI uri = UriBuilder.fromUri(postNlConfiguration.getUrl().concat(ADDRESS_PATH))
                    .build();

            final String postNlAddressBody = String.valueOf(new JSONObject()
                    .putOpt(AddressBodyKey.CITY, addressCheckPostRequest.getCity())
                    .putOpt(AddressBodyKey.COUNTRY_ISO, addressCheckPostRequest.getCountryIso())
                    .putOpt(AddressBodyKey.POSTAL_CODE, addressCheckPostRequest.getPostalCode())
                    .putOpt(AddressBodyKey.STREET, addressCheckPostRequest.getStreet())
                    .putOpt(AddressBodyKey.HOUSE_NUMBER, addressCheckPostRequest.getHouseNumber())
                    .putOpt(AddressBodyKey.HOUSE_NUMBER_ADDITION, addressCheckPostRequest.getHouseNumberAddition())
            );

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(postNlAddressBody))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("apikey", postNlConfiguration.getApiKey())
                    .build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()>299 || response.body().contains("\"errors\"")) {
                throw new NoSuchElementException(response.body());
            } else {
                return objectMapper.readerForListOf(Address.class).readValue(response.body());
            }
        });
    }

    @Override
    public Try<List<Shipment>> getShippingStatuses() {
        return Try.of(() -> {
            final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
            final String shipmentUrl = MessageFormat.format("/shipment/v2/status/{0}/updatedshipments?period={1}&period={2}", CUSTOMER_NUMBER, now.minusHours(2).toString(), now.toString());
            final URI uri = UriBuilder.fromUri(postNlConfiguration.getUrl().concat(shipmentUrl)).build();

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .setHeader("Accept", "application/json")
                    .setHeader("apikey", postNlConfiguration.getShipmentApiKey())
                    .build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readerForListOf(Shipment.class).readValue(response.body());
        });
    }
}
