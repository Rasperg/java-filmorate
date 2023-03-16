package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    @Getter
    protected Map<Integer, Film> filmMap = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmMap.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validationDate(film);
        id++;
        if (!filmMap.containsKey(id)) {
            film.setId(id);
            filmMap.put(id, film);
        } else {
            throw new ValidationException("Проблема с идентификатором фильма");
        }
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validationDate(film);
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
        } else {
            throw new ValidationException("Фильм не найден.");
        }
        log.info("Информация о фильме {} обновлена", film.getName());
        return film;
    }

    protected void validationDate(Film film) {
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}