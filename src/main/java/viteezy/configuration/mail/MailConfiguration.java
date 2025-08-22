package viteezy.configuration.mail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class MailConfiguration {

    private final String server;
    private final Integer port;
    private final String username;
    private final String password;
    private final String frontendBaseUrl;
    private final String defaultOrderBcc;
    private final String pharmacistCsvRecipient;
    private final String pharmacistCsvSubject;

    @JsonCreator
    public MailConfiguration(
            @JsonProperty("server") String server,
            @JsonProperty("port") Integer port,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("frontend_base_url") String frontendBaseUrl,
            @JsonProperty("default_order_bcc") String defaultOrderBcc,
            @JsonProperty("pharmacist_csv_recipient") String pharmacistCsvRecipient,
            @JsonProperty("pharmacist_csv_subject") String pharmacistCsvSubject) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
        this.frontendBaseUrl = frontendBaseUrl;
        this.defaultOrderBcc = defaultOrderBcc;
        this.pharmacistCsvRecipient = pharmacistCsvRecipient;
        this.pharmacistCsvSubject = pharmacistCsvSubject;
    }

    public String getServer() {
        return server;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFrontendBaseUrl() {
        return frontendBaseUrl;
    }

    public String getDefaultOrderBcc() {
        return defaultOrderBcc;
    }

    public String getPharmacistCsvRecipient() {
        return pharmacistCsvRecipient;
    }

    public String getPharmacistCsvSubject() {
        return pharmacistCsvSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailConfiguration that = (MailConfiguration) o;
        return Objects.equals(server, that.server) && Objects.equals(port, that.port) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(frontendBaseUrl, that.frontendBaseUrl) && Objects.equals(defaultOrderBcc, that.defaultOrderBcc) && Objects.equals(pharmacistCsvRecipient, that.pharmacistCsvRecipient) && Objects.equals(pharmacistCsvSubject, that.pharmacistCsvSubject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server, port, username, password, frontendBaseUrl, defaultOrderBcc, pharmacistCsvRecipient, pharmacistCsvSubject);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MailConfiguration.class.getSimpleName() + "[", "]")
                .add("server='" + server + "'")
                .add("port=" + port)
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("frontendBaseUrl='" + frontendBaseUrl + "'")
                .add("defaultOrderBcc='" + defaultOrderBcc + "'")
                .add("pharmacistCsvRecipient='" + pharmacistCsvRecipient + "'")
                .add("pharmacistCsvSubject='" + pharmacistCsvSubject + "'")
                .toString();
    }
}
