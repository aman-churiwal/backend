package viteezy.domain.klaviyo.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class Item {
    private final Long id;
    private final String name;
    private final String code;
    private final String imageUrl;
    private final BigDecimal price;

    public Item(@JsonProperty("id") Long id,
                @JsonProperty("name") String name,
                @JsonProperty("code") String code,
                @JsonProperty("image_url") String imageUrl,
                @JsonProperty("price") BigDecimal price) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(code, item.code) && Objects.equals(imageUrl, item.imageUrl) && Objects.equals(price, item.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, imageUrl, price);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Item.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("imageUrl='" + imageUrl + "'")
                .add("price=" + price)
                .toString();
    }
}
