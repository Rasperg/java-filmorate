package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Getter
    protected Map<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validationName(user);
        id++;
        if (!userMap.containsKey(id)) {
            user.setId(id);
            userMap.put(id, user);
        } else {
            throw new ValidationException("Проблема с идентификатором пользователя");
        }
        log.info("Добавлен пользователь {} с логином {}", user.getName(), user.getLogin());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validationName(user);
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь не найден.");
        }
        log.info("Информация о пользователе {} с логином {} обновлена", user.getName(), user.getLogin());
        return user;
    }

    protected void validationName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
