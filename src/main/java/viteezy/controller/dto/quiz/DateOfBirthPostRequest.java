package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DateOfBirthPostRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDate date;

    @JsonCreator
    public DateOfBirthPostRequest(
            @JsonProperty(value = "date", required = true) LocalDate date

    ) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}