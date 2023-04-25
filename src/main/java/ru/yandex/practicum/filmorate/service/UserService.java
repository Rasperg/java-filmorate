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

        Collection<User> getAllUsers = userDbStorage.getAllUsers();
        if (getAllUsers.isEmpty()) {
            log.warn("Ошибка отправки списка пользователей");
            throw new ObjectNotFoundException("Ошибка отправки списка пользователей");
        }
        log.info("Список пользователей отправлен");
        return getAllUsers;
    }

    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        User createdUser = userDbStorage.createUser(user);

        if (createdUser == null) {
            log.warn("Пользователь не создан");
            throw new ObjectNotFoundException("Пользователь не создан");
        }
        log.info("Пользователь добавлен");
        return createdUser;
    }

    public User updateUser(User user) {
        checkUser(user.getId());
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        User updateUser = userDbStorage.updateUser(user);

        if (updateUser == null) {
            log.warn("Пользователь {} не обновлён", user.getId());
            throw new ObjectNotFoundException("Пользователь не обновлён " + user.getId());
        }
        log.info("Пользователь {} обновлен", user.getId());
        return updateUser;
    }

    public Optional<User> getUserById(int id) {
        checkUser(id);
        Optional<User> getUserById = userDbStorage.getUserById(id);

        if (getUserById == null) {
            log.warn("Пользователь {} не передан", id);
            throw new ObjectNotFoundException("Пользователь не передан " + id);
        }
        log.info("Пользователь с id {} передан", id);
        return getUserById;
    }

    public Optional<User> deleteUserById(int id) {
        checkUser(id);
        Optional<User> deletedUser = userDbStorage.deleteUserById(id);
        if (!deletedUser.isPresent()) {
            log.warn("Не удалось удалить пользователя {}", id);
            throw new ObjectNotFoundException("Не удалось удалить пользователя " + id);
        }
        log.info("Пользователь с id {} удален", id);
        return deletedUser;
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

        List<Integer> result = userDbStorage.followUser(followingId, followerId);
        if (result.isEmpty()) {
            log.warn("Пользователи не добавлены в друзья");
            throw new ObjectNotFoundException("Пользователи не добавлены в друзья");
        }
        log.info("Пользователь {} подписался на {}", followingId, followerId);
        return result;
    }

    public List<Integer> unfollowUser(int followingId, int followerId) {
        String checkFriendship = "SELECT * FROM FRIENDSHIP WHERE user_id = ? AND friend_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkFriendship, followingId, followerId);
        if (!userRows.first()) {
            log.warn("Пользователь не подписан");
            throw new InternalException("Пользователь не подписан");
        }

        List<Integer> result = userDbStorage.unfollowUser(followingId, followerId);

        if (result.isEmpty()) {
            log.warn("Изменения не внесены");
            throw new ObjectNotFoundException("Изменения не внесены");
        }
        log.info("Пользователь {} отписался от {}", followerId, followingId);

        return result;
    }

    public List<User> getFriendsListById(int id) {
        checkUser(id);

        List<User> getFriendsListById = userDbStorage.getFriendsListById(id);
        if (getFriendsListById.isEmpty()) {
            log.warn("Общие друзья не найдены");
            throw new ObjectNotFoundException("Общие друзья не найдены");
        }
        log.info("Запрос получения списка друзей пользователя {} выполнен", id);

        return getFriendsListById;
    }

    public List<User> getCommonFriendsList(int firstId, int secondId) {
        checkUser(firstId);
        checkUser(secondId);

        List<User> getCommonFriendsList = userDbStorage.getCommonFriendsList(firstId, secondId);

        if (getCommonFriendsList.isEmpty()) {
            log.warn("Друзья не найдены");
            throw new ObjectNotFoundException("Друзья не найдены");
        }
        log.info("Список общих друзей {} и {} отправлен", firstId, secondId);

        return getCommonFriendsList;
    }

    private void checkUser(int id) {
        Optional<User> user = userDbStorage.getUserById(id);
        if (user.isEmpty()) {
            log.warn("Пользователь с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }
}
