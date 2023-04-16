package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    @NotNull(message = "Имя не может отсутствовать")
    @NotEmpty(message = "Имя не может быть пустым")
    private final String name;
    @Size(min = 1, max = 200, message = "Описание должно содержать от 1 до 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = "Продолжительность должны быть положительной")
    private final int duration;
    private Set<Integer> usersLikes = new HashSet<>();
}