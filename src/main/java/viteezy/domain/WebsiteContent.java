package viteezy.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class WebsiteContent {
    private final Long id;
    private final String code;
    private final String title;
    private final String subtitle;
    private final Boolean isActive;


    private final String buttonText;
    private final String buttonLink;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public WebsiteContent(Long id, String code, String title, String subtitle, Boolean isActive,
                          String buttonText, String buttonLink, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.subtitle = subtitle;
        this.isActive = isActive;
        this.buttonText = buttonText;
        this.buttonLink = buttonLink;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() { return title; }

    public String getSubtitle() { return subtitle; }

    public Boolean getActive() { return isActive; }

    public String getButtonText() { return buttonText; }

    public String getButtonLink() { return buttonLink; }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebsiteContent that = (WebsiteContent) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(title, that.title) && Objects.equals(subtitle, that.subtitle) && Objects.equals(isActive, that.isActive) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, title, subtitle, isActive, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebsiteContent.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("code='" + code + "'")
                .add("title='" + title + "'")
                .add("subtitle='" + subtitle + "'")
                .add("isActive=" + isActive)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}