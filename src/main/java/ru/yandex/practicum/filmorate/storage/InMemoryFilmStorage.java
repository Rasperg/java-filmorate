package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    protected Map<Integer, Film> filmMap = new HashMap<>();
    private int id = 0;
    private static final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    @Override
    public Collection<Film> getAllFilms() {
        return filmMap.values();
    }

    @Override
    public Film createFilm(Film film) {
        validationDate(film);
        id++;
        if (!filmMap.containsKey(id)) {
            film.setId(id);
            filmMap.put(id, film);
        } else {
            throw new ObjectNotFoundException("Проблема с идентификатором фильма");
        }
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validationDate(film);
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
        } else {
            throw new ObjectNotFoundException("Фильм не найден.");
        }
        log.info("Информация о фильме {} обновлена", film.getName());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        return filmMap.get(id);
    }

    @Override
    public Film deleteFilmById(int id) {
        Film film = filmMap.get(id);
        filmMap.remove(id);
        return film;
    }

    protected void validationDate(Film film) {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза не может быть раньше" + minReleaseDate);
        }
    }
}
