package viteezy.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FulfilmentConfiguration {
    private final String sftpRemoteHost;
    private final String sftpUsername;
    private final String sftpPrivateKeyFilename;

    @JsonCreator
    public FulfilmentConfiguration(
            @JsonProperty(value = "sftp_remote_host", required = true) String sftpRemoteHost,
            @JsonProperty(value = "sftp_username", required = true) String sftpUsername,
            @JsonProperty(value = "sftp_privatekey_filename", required = true) String sftpPrivateKeyFilename) {
        this.sftpRemoteHost = sftpRemoteHost;
        this.sftpUsername = sftpUsername;
        this.sftpPrivateKeyFilename = sftpPrivateKeyFilename;
    }

    public String getSftpRemoteHost() {
        return sftpRemoteHost;
    }

    public String getSftpUsername() {
        return sftpUsername;
    }

    public String getSftpPrivateKeyFilename() {
        return sftpPrivateKeyFilename;
    }
}
