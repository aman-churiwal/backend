package viteezy.controller.dto.klaviyo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.klaviyo.Product;

import java.util.Objects;
import java.util.StringJoiner;

public class ProductGetResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String code;
    private final String url;

    public ProductGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "title", required = true) String name,
            @JsonProperty(value = "description", required = true) String description,
            @JsonProperty(value = "image_link", required = true) String code,
            @JsonProperty(value = "link", required = true) String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
        this.url = url;
    }

    public static ProductGetResponse from(Product that) {
        return new ProductGetResponse(that.getId(), that.getName(), that.getDescription(), that.getCode(), that.getUrl());
    }

    public Long getId() {
        return id;
    }

    @JsonGetter(value = "title")
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @JsonGetter(value = "image_link")
    public String getCode() {
        return code;
    }

    @JsonGetter(value = "link")
    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductGetResponse that = (ProductGetResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(code, that.code) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, code, url);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("code='" + code + "'")
                .add("url='" + url + "'")
                .toString();
    }
}
