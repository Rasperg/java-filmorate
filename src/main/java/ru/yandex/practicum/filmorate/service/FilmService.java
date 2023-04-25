package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public Collection<Film> getAllFilms() {

        Collection<Film> getAllFilms = filmDbStorage.getAllFilms();

        log.info("Список фильмов передан.");
        return getAllFilms;
    }

    public Film createFilm(Film film) {
        validate(film);
        Film createFilm = filmDbStorage.createFilm(film);
        log.info("Фильм добавлен");
        return createFilm;
    }

    public Film updateFilm(Film film) {
        validate(film);
        checkFilm(film.getId());

        Film updateFilm = filmDbStorage.updateFilm(film);

        log.info("Фильм {} обновлен", film.getId());
        return updateFilm;
    }

    public Optional<Film> getFilmById(int id) {
        checkFilm(id);

        Optional<Film> getFilmById = filmDbStorage.getFilmById(id);

        log.info("Фильм с id {} передан", id);
        return getFilmById;
    }

    public Optional<Film> deleteFilmById(int id) {
        checkFilm(id);

        Optional<Film> deleteFilmById = filmDbStorage.deleteFilmById(id);

        log.info("Фильм с id {} удален", id);
        return deleteFilmById;
    }

    public Optional<Film> addLikeToFilm(int filmId, int userId) {
        checkFilm(filmId);
        if (userDbStorage.getUserById(userId).isEmpty()) {
            log.warn("Пользователь {} не найден.", userId);
            throw new ObjectNotFoundException("Фильм или пользователь не найдены");
        }

        Optional<Film> addLikeToFilm = filmDbStorage.addLike(filmId, userId);

        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);

        return addLikeToFilm;
    }

    public Optional<Film> removeLikeToFilm(int filmId, int userId) {
        checkFilm(filmId);

        if (userDbStorage.getUserById(userId).isEmpty()) {
            log.warn("Пользователь {} не найден.", userId);
            throw new ObjectNotFoundException("Фильм или пользователь не найдены");
        }

        Optional<Film> removeLikeToFilm = filmDbStorage.removeLike(filmId, userId);

        log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);

        return removeLikeToFilm;
    }

    public List<Film> getBestFilms(int count) {

        List<Film> getBestFilms = filmDbStorage.getBestFilms(count);

        log.info("Отправлен список из {} самых популярных фильмов", count);

        return getBestFilms;
    }

    private void checkFilm(int id) {
        Optional<Film> film = filmDbStorage.getFilmById(id);
        if (film.isEmpty()) {
            log.warn("Фильм с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Фильм не найден");
        }
        log.info("Фильм с id {} отправлен", id);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE))
            throw new ValidationException("Указана некорректная дата");
    }
}
