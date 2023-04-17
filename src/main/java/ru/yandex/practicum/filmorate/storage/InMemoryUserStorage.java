package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    protected Map<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    @Override
    public Map<Integer, User> getUserMap() {
        return userMap;
    }

    @Override
    public User createUser(User user) {
        id++;
        if (!userMap.containsKey(id)) {
            ensureNamePresent(user);
            user.setId(id);
            userMap.put(id, user);
        } else {
            throw new ObjectNotFoundException("Проблема с идентификатором пользователя");
        }
        log.info("Добавлен пользователь {} с логином {}", user.getName(), user.getLogin());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUserMap().containsKey(user.getId())) {
            ensureNamePresent(user);
            userMap.put(user.getId(), user);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        log.info("Информация о пользователе {} с логином {} обновлена", user.getName(), user.getLogin());
        return user;
    }

    @Override
    public User getUserById(int id) {
        return userMap.get(id);
    }

    @Override
    public User deleteUserById(int id) {
        User user = userMap.get(id);
        userMap.remove(id);
        return user;
    }

    protected void ensureNamePresent(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
