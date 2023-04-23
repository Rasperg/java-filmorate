package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(int id);

    Optional<Film> deleteFilmById(int id);

    Optional<Film> addLike(int filmId, int userId);
    Optional<Film> removeLike(int filmId, int userId);
    List<Film> getBestFilms(int count);
}
