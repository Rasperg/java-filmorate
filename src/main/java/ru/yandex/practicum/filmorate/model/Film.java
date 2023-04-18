package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Film {
    @Getter
    @Setter
    private int id;

    @NotNull(message = "Имя не может отсутствовать")
    @NotBlank(message = "Имя не может быть пустым")
    @Getter
    private String name;

    @Size(min = 1, max = 200, message = "Описание должно содержать от 1 до 200 символов")
    private String description;

    @Getter
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должны быть положительной")
    private int duration;

    private Set<Integer> usersLikes = new HashSet<>();
}