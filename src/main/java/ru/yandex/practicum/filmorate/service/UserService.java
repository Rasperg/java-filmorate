package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

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
        return userDbStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userDbStorage.createUser(user);
    }

    public User updateUser(User user) {
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
        if (getUserById(id).isEmpty()) {
            log.warn("Пользователь с id {} не найден", id);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        log.info("Запрос получения списка друзей пользователя {} выполнен", id);

        return userDbStorage.getFriendsListById(id);
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        if (getUserById(firstId).isEmpty() || getUserById(secondId).isEmpty()) {
            log.warn("Пользователи с id {} и {} не найдены", firstId, secondId);
            throw new ObjectNotFoundException("Пользователи не найдены");
        }
        log.info("Список общих друзей {} и {} отправлен", firstId, secondId);

        return userDbStorage.getCommonFriendsList(firstId, secondId);
    }

    private void checkUser(int id) {
        if (userDbStorage.getUserById(id) == null) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }
}
