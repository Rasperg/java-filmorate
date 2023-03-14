package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> filmMap = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<Film> getAllFilms(){
        return filmMap.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film){
        validationFilm(film);
        id++;
        if (!filmMap.containsKey(id)){
            film.setId(id);
            filmMap.put(id, film);
        } else {
            throw new ValidationException("Проблема с идентификатором фильма");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film){
        validationFilm(film);
        if (filmMap.containsKey(film.getId())){
            filmMap.put(film.getId(), film);
        } else {
            throw new ValidationException("Фильм не найден.");
        }
        return film;
    }

    private void validationFilm(Film film){
        int maxLengthDescription = 200;
        LocalDate minReleaseDate = LocalDate.of(1895,12,28);

        if(film.getName() == null || film.getName().isEmpty()){
            throw new ValidationException("Отсутствует название фильма");
        }
        if (film.getDescription().length() > maxLengthDescription){
            throw new ValidationException("Описание фильма не может быть более 200 символов");
        }
        if (film.getReleaseDate().isBefore(minReleaseDate)){
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if(film.getDuration() == null || film.getDuration() < 1){
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}