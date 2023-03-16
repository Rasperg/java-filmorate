package ru.yandex.practicum.filmorate.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController controller;
    Film film;

    @BeforeEach
    void filmControllerInit() {
        controller = new FilmController();
        film = new Film("TestFilm", "Test", LocalDate.of(2022, 01, 01), 65);
        controller.createFilm(film);
    }

    @Test
    void createFilmTest() {
        assertEquals(film, controller.getFilmMap().get(film.getId()));
    }

    @Test
    void incorrectDateFilmTest() {
        Film film2 = new Film("TestFilm", "Test", LocalDate.of(1200, 01, 01), 65);
        assertThrows(ValidationException.class, () -> controller.createFilm(film2));
    }
}
