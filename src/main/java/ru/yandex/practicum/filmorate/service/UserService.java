package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        checkUser(id);
        log.info("Пользователь с id {} передан", id);
        return userStorage.getUserById(id);
    }

    public User deleteUserById(int id) {
        checkUser(id);
        log.info("Пользователь с id {} удален", id);
        return userStorage.deleteUserById(id);
    }

    public List<User> uniteFriends(int firstId, int secondId) {
        if (userStorage.getUserById(firstId) == null || userStorage.getUserById(secondId) == null) {
            throw new ValidationException("Переданы некорректные идентификаторы пользователей");
        }
        if (userStorage.getUserById(firstId).getFriends().contains(secondId)) {
            throw new ValidationException("Пользователи уже добавлены в список друзей");
        }
        userStorage.getUserById(firstId).getFriends().add(secondId);
        userStorage.getUserById(secondId).getFriends().add(firstId);
        log.info("Пользователи {}, {} добавлены в список друзей", userStorage.getUserById(firstId).getName(),
                userStorage.getUserById(secondId).getName());

        return Arrays.asList(userStorage.getUserById(firstId), userStorage.getUserById(secondId));
    }

    public List<User> removeFriends(int firstId, int secondId) {
        if (userStorage.getUserById(firstId) == null || userStorage.getUserById(secondId) == null) {
            throw new ValidationException("Переданы некорректные идентификаторы пользователей");
        }
        if (!userStorage.getUserById(firstId).getFriends().contains(secondId)) {
            throw new ValidationException("Пользователи не состоят в списке друзей");
        }
        userStorage.getUserById(firstId).getFriends().remove(secondId);
        userStorage.getUserById(secondId).getFriends().remove(firstId);
        log.info("Пользователи {} и {} удалены из списка друзей", userStorage.getUserById(firstId).getName(),
                userStorage.getUserById(secondId).getName());

        return Arrays.asList(userStorage.getUserById(firstId), userStorage.getUserById(secondId));
    }

    public List<User> getFriendsListById(int id) {
        checkUser(id);
        log.info("Получен список друзей пользователя с id{}", userStorage.getUserById(id).getName());

        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        checkUser(firstId);
        checkUser(secondId);
        User firstUser = userStorage.getUserById(firstId);
        User secondUser = userStorage.getUserById(secondId);
        log.info("Предоставлен список общих друзей {} и {}", firstUser.getName(), secondUser.getName());

        return firstUser.getFriends().stream()
                .filter(friendId -> secondUser.getFriends().contains(friendId))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    private void checkUser(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new ValidationException("Пользователь не найден");
        }
    }
}
