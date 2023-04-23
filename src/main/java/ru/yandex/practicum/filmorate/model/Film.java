package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @Getter
    @Setter
    private int id;

    @NotNull(message = "Имя не может отсутствовать")
    @NotBlank(message = "Некорректное название фильма")
    @Getter
    private String name;

    @Getter
    @Size(min = 1, max = 200, message = "Описание должно содержать от 1 до 200 символов")
    private String description;

    @Getter
    private LocalDate releaseDate;

    @Getter
    @Positive(message = "Продолжительность должны быть положительной")
    private long duration;

    @Getter
    @Setter
    private Mpa mpa;

    @Getter
    @Setter
    private List<Genre> genres;
}