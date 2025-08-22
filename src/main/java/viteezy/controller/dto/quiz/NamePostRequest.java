package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class NamePostRequest {

    private final String name;

    @JsonCreator
    public NamePostRequest(
            @JsonProperty(value = "name", required = true) String name
    ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamePostRequest that = (NamePostRequest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NamePostRequest.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}