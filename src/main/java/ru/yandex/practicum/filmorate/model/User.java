package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class User {
    @Getter
    @Setter
    private int id;

    @Email(message = "Ошибка в адресе электронной почты")
    private String email;

    @NotBlank(message = "Логин не может быть пустым или содержать пробелы")
    @Getter
    private String login;

    @Getter
    @Setter
    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    @Getter
    private Set<Integer> friends = new HashSet<>();
}
