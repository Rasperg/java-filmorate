package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<User> getAllUsers(){
        return userMap.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        id++;
        if (!userMap.containsKey(id)){
            user.setId(id);
            userMap.put(id, user);
        } else {
            throw new ValidationException("Проблема с идентификатором пользователя");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user){
        if (userMap.containsKey(user.getId())){
            userMap.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь не найден.");
        }
        return user;
    }

    private void validationUser(User user){
        if(user.getEmail().isEmpty() || user.getEmail() == null){
            throw new ValidationException("Отсутствует название фильма");
        }
    }
}
