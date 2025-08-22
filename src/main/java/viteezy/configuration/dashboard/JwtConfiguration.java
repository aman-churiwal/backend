package viteezy.configuration.dashboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtConfiguration {

    private final String uri;
    private final String header;
    private final String prefix;
    private final int expiration;
    private final String secret;

    @JsonCreator
    public JwtConfiguration(
            @JsonProperty("uri") String uri,
            @JsonProperty("header") String header,
            @JsonProperty("prefix") String prefix,
            @JsonProperty("expiration") int expiration,
            @JsonProperty("secret") String secret
    ) {
        this.uri = uri;
        this.header = header;
        this.prefix = prefix;
        this.expiration = expiration;
        this.secret = secret;
    }

    public String getUri() {
        return uri;
    }

    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return "JwtConfiguration{" +
                "uri='" + uri + '\'' +
                ", header='" + header + '\'' +
                ", prefix='" + prefix + '\'' +
                ", expiration='" + expiration + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
