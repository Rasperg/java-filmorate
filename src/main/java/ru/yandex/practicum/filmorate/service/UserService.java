package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(UserDbStorage userDbStorage, JdbcTemplate jdbcTemplate) {
        this.userDbStorage = userDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<User> getAllUsers() {
        log.info("Список пользователей отправлен");
        return userDbStorage.getAllUsers();
    }

    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.info("Пользователь добавлен");
        return userDbStorage.createUser(user);
    }

    public User updateUser(User user) {
        checkUser(user.getId());
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.info("Пользователь {} обновлен", user.getId());
        return userDbStorage.updateUser(user);
    }

    public Optional<User> getUserById(int id) {
        checkUser(id);
        log.info("Пользователь с id {} передан", id);
        return userDbStorage.getUserById(id);
    }

    public Optional<User> deleteUserById(int id) {
        checkUser(id);
        log.info("Пользователь с id {} удален", id);
        return userDbStorage.deleteUserById(id);
    }

    public List<Integer> followUser(int followingId, int followerId) {
        if (getUserById(followingId).isEmpty() || getUserById(followerId).isEmpty()) {
            log.warn("Пользователи с id {} и(или) {} не найден(ы)", followingId, followerId);
            throw new ObjectNotFoundException("Пользователи не найдены");
        }
        String checkFriendship = "SELECT * FROM FRIENDSHIP WHERE user_id = ? AND friend_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkFriendship, followingId, followerId);
        if (userRows.first()) {
            log.warn("Пользователь уже подписан");
            throw new InternalException("Пользователь уже подписан");
        }
        log.info("Пользователь {} подписался на {}", followingId, followerId);

        return userDbStorage.followUser(followingId, followerId);
    }

    public List<Integer> unfollowUser(int followingId, int followerId) {
        String checkFriendship = "SELECT * FROM FRIENDSHIP WHERE user_id = ? AND friend_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkFriendship, followingId, followerId);
        if (!userRows.first()) {
            log.warn("Пользователь не подписан");
            throw new InternalException("Пользователь не подписан");
        }
        log.info("Пользователь {} отписался от {}", followerId, followingId);

        return userDbStorage.unfollowUser(followingId, followerId);
    }

    public List<User> getFriendsListById(int id) {
        checkUser(id);
        log.info("Запрос получения списка друзей пользователя {} выполнен", id);

        return userDbStorage.getFriendsListById(id);
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        checkUser(firstId);
        checkUser(secondId);
        log.info("Список общих друзей {} и {} отправлен", firstId, secondId);

        return userDbStorage.getCommonFriendsList(firstId, secondId);
    }

    private void checkUser(int id) {
        Optional<User> user = userDbStorage.getUserById(id);
        if (user.isEmpty()) {
            log.warn("Пользователь с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }
}
