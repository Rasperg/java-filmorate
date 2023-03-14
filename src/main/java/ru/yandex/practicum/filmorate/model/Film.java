package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.apache.tomcat.jni.Time;

import javax.validation.constraints.*;
import java.time.LocalDate;
@Data
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private final String name;
    @Size(min = 1, max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Min(value = 1)
    private final Long duration;
}