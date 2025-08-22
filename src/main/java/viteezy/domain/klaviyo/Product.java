package viteezy.domain.klaviyo;

import java.math.BigDecimal;

public class Product {

    private final Long id;
    private final String name;
    private final String description;
    private final String code;
    private final String url;
    private final BigDecimal price;

    public Product(Long id, String name, String description, String code, String url, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
        this.url = url;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
