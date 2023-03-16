package ru.yandex.practicum.filmorate.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class UserControllerTest {
    UserController controller;
    User user;

    @BeforeEach
    void UserControllerInit() {
        controller = new UserController();
        user = new User("test@test.ru", "Testlogin", LocalDate.of(1990, 01, 01));
        controller.createUser(user);
    }

    @Test
    void createUserTest() {
        user.setName("Test");
        assertEquals(user, controller.getUserMap().get(user.getId()));
    }

    @Test
    void addNameNullTest() {
        assertEquals("Testlogin", controller.getUserMap().get(user.getId()).getName());
    }

    @Test
    void updateUserTest() {
        user.setName("Test");
        controller.updateUser(user);
        assertEquals("Test", controller.getUserMap().get(user.getId()).getName());
    }
}
