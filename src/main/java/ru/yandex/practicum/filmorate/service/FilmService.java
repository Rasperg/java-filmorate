package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        log.info("Список фильмов передан.");
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        checkFilm(id);
        log.info("Фильм с id {} передан", id);
        return filmStorage.getFilmById(id);
    }

    public Film deleteFilmById(int id) {
        checkFilm(id);
        log.info("Фильм с id {} удален", id);
        return filmStorage.deleteFilmById(id);
    }

    public Film addLikeToFilm(int filmId, int userId) {
        checkFilm(filmId);
        filmStorage.getFilmById(filmId).getUsersLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
        return filmStorage.getFilmById(filmId);
    }

    public Film removeLikeToFilm(int filmId, int userId) {
        checkFilm(filmId);

        if (!filmStorage.getFilmById(filmId).getUsersLikes().contains(userId)) {
            throw new ObjectNotFoundException("Лайк от пользователя отсутствует");
        }

        filmStorage.getFilmById(filmId).getUsersLikes().remove(userId);
        log.info("Пользователь с id {} удалил лайк фильма с id {}", userId, filmId);

        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getBestFilms(int count) {
        log.info("Вывод 10 наиболее популярных фильмов по количеству лайков");

        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getUsersLikes().size(), o1.getUsersLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkFilm(int id) {
        if (filmStorage.getFilmById(id) == null) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
    }
}
